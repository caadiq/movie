package com.beemer.movie.view.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beemer.movie.R
import com.beemer.movie.databinding.FragmentSearchBinding
import com.beemer.movie.view.adapter.SearchAdapter
import com.beemer.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel by viewModels<MovieViewModel>()

    private val searchAdapter = SearchAdapter()

    private var searchQuery = ""
    private var isLoading = false
    private var isRefreshed = false

    private val startForRegisterResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val query = result.data?.getStringExtra("query")
            query?.let {
                searchAdapter.setItemList(emptyList())

                searchQuery = it
                movieViewModel.getSearchList(0, 20, it, true)
                binding.txtSearch.apply {
                    text = it
                    setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.txtSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putExtra("query", searchQuery)
            startForRegisterResult.launch(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (searchQuery.trim().isNotEmpty())
                movieViewModel.getSearchList(0, 20, searchQuery, true)
            else
                binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = searchAdapter
            setHasFixedSize(true)
            itemAnimator = null

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = recyclerView.adapter?.itemCount ?: 0

                    if (totalItemCount > 0 && !isLoading && lastVisibleItemPosition >= totalItemCount - 3) {
                        movieViewModel.page.value?.let { page ->
                            page.nextPage?.let {
                                movieViewModel.getSearchList(it, 20, searchQuery, false)
                            }
                        }
                    }
                }
            })
        }

        searchAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(requireContext(), DetailsActivity::class.java)
            intent.putExtra("code", item.movieCode)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        movieViewModel.apply {
            searchList.observe(viewLifecycleOwner) { searchList ->
                binding.swipeRefreshLayout.isRefreshing = false
                setLoading(false)

                binding.txtEmptyList.visibility = if (searchList.isEmpty()) View.VISIBLE else View.GONE
                searchAdapter.setItemList(searchList)

                if (this@SearchFragment.isRefreshed)
                    binding.recyclerView.scrollToPosition(0)
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                this@SearchFragment.isLoading = isLoading

                if (isLoading)
                    searchAdapter.showProgress()
                else
                    searchAdapter.hideProgress()
            }

            isRefreshed.observe(viewLifecycleOwner) { isRefreshed ->
                this@SearchFragment.isRefreshed = isRefreshed
            }
        }
    }
}