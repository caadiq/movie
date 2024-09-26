package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.beemer.movie.databinding.FragmentSearchBinding
import com.beemer.movie.model.entity.SearchHistoryEntity
import com.beemer.movie.view.adapter.SearchHistoryAdapter
import com.beemer.movie.viewmodel.SearchHistoryViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchHistoryViewModel by viewModels<SearchHistoryViewModel>()

    private val searchHistoryAdapter = SearchHistoryAdapter()

    private val imm by lazy { requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager }

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
        binding.editSearch.apply {
            requestFocus()
            setOnEditorActionListener { _, _, _ ->
                search(text.toString().trim())
                true
            }
        }

        binding.txtDeleteAll.setOnClickListener {
            searchHistoryViewModel.deleteAllHistory()
        }
    }

    private fun setupRecyclerView() {
        searchHistoryAdapter.apply {
//            setItemList(listOf(
//                "베테랑2", "사랑의 하츄핑", "브레드이발소: 빵스타의 탄생", "비긴 어게인", "정국: 아이 엠 스틸"
//            ))

            setOnItemClickListener { item, _ ->
                binding.editSearch.setText(item)
                search(item)
            }
        }

        binding.recyclerView.apply {
            adapter = searchHistoryAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
        }
    }

    private fun setupViewModel() {
        searchHistoryViewModel.apply {
            searchHistory.observe(viewLifecycleOwner) { history ->
                binding.txtRecent.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
                binding.txtDeleteAll.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
                searchHistoryAdapter.setItemList(history.map { it.title })
            }

            isTitleExists.observe(viewLifecycleOwner) { exists ->
                val title = binding.editSearch.text.toString()
                if (exists)
                    searchHistoryViewModel.deleteHistoryByTitle(title)
                searchHistoryViewModel.insertHistory(SearchHistoryEntity(title = title))
            }
        }
    }

    private fun search(query: String) {
        imm.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)
        binding.editSearch.clearFocus()
        searchHistoryViewModel.checkTitleExists(query)
        // TODO: 검색
    }
}