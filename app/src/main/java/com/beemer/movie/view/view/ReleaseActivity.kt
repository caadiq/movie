package com.beemer.movie.view.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beemer.movie.databinding.ActivityReleaseBinding
import com.beemer.movie.view.adapter.ReleaseAdapter
import com.beemer.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReleaseActivity : AppCompatActivity() {
    private val binding by lazy { ActivityReleaseBinding.inflate(layoutInflater) }

    private val movieViewModel by viewModels<MovieViewModel>()
    
    private val releaseAdapter = ReleaseAdapter()

    private val type by lazy { intent.getStringExtra("type") }

    private var isLoading = false
    private var isRefreshed = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupView() {
        binding.txtTitle.text = if (type == "recent") "최근 개봉작" else "개봉 예정작"
        binding.txtEmptyList.text = if (type == "recent") "최근 개봉작이 없습니다." else "개봉 예정작이 없습니다."

        binding.swipeRefreshLayout.setOnRefreshListener {
            type?.let {
                movieViewModel.getReleaseList(0, 20, it, true)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = releaseAdapter
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
                                type?.let { type ->
                                    movieViewModel.getReleaseList(it, 20, type, false)
                                }
                            }
                        }
                    }
                }
            })
        }

        releaseAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("code", item.movieCode)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        movieViewModel.apply {
            type?.let {
                getReleaseList(0, 20, it, true)
            }

            releaseList.observe(this@ReleaseActivity) { releaseList ->
                binding.swipeRefreshLayout.isRefreshing = false
                setLoading(false)

                binding.txtEmptyList.visibility = if (releaseList.isEmpty()) View.VISIBLE else View.GONE
                releaseAdapter.setItemList(releaseList)

                if (this@ReleaseActivity.isRefreshed)
                    binding.recyclerView.scrollToPosition(0)
            }

            isLoading.observe(this@ReleaseActivity) { isLoading ->
                this@ReleaseActivity.isLoading = isLoading

                if (isLoading)
                    releaseAdapter.showProgress()
                else
                    releaseAdapter.hideProgress()
            }

            isRefreshed.observe(this@ReleaseActivity) { isRefreshed ->
                this@ReleaseActivity.isRefreshed = isRefreshed
            }
        }
    }
}