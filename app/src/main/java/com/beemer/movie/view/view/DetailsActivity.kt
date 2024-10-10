package com.beemer.movie.view.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.movie.databinding.ActivityDetailsBinding
import com.beemer.movie.model.entity.BookmarkEntity
import com.beemer.movie.view.adapter.GenreAdapter
import com.beemer.movie.view.utils.DateTimeConverter.convertDate
import com.beemer.movie.viewmodel.BookmarkViewModel
import com.beemer.movie.viewmodel.MainViewModel
import com.beemer.movie.viewmodel.MovieViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailsBinding.inflate(layoutInflater) }

    private val bookmarkViewModel by viewModels<BookmarkViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()
    private val movieViewModel by viewModels<MovieViewModel>()

    private val genreAdapter = GenreAdapter()

    private val movieCode by lazy { intent.getStringExtra("code") }

    private var isBookmarked = false
    private var bookmark = BookmarkEntity(movieCode = "", movieName = "", posterUrl = "", movieGenre = "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupView() {
        binding.lottie.setOnClickListener {
            if (isBookmarked) {
                DefaultDialog(
                    title = null,
                    message = "북마크에서 삭제하시겠습니까?",
                    onConfirm = {
                        bookmarkViewModel.deleteBookmarkByCode(movieCode ?: "")
                    }
                ).show(supportFragmentManager, "DefaultDialog")
            } else if (bookmark.movieName.isNotEmpty() && !isBookmarked) {
                bookmarkViewModel.insertBookmark(bookmark)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = genreAdapter
            itemAnimator = null
        }
    }

    private fun setupViewModel() {
        bookmarkViewModel.apply {
            movieCode?.let { checkBookmarkExists(it) }

            isBookmarkExists.observe(this@DetailsActivity) { exists ->
                mainViewModel.setIsBookmarked(exists)

                if (exists)
                    binding.lottie.playAnimation()
            }

            insertResult.observe(this@DetailsActivity) { result ->
                if (result) {
                    mainViewModel.setIsBookmarked(true)
                    binding.lottie.playAnimation()
                    Toast.makeText(this@DetailsActivity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailsActivity, "북마크 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            deleteResult.observe(this@DetailsActivity) { result ->
                if (result) {
                    mainViewModel.setIsBookmarked(false)
                    binding.lottie.setProgress(0.0f)
                    binding.lottie.cancelAnimation()
                    Toast.makeText(this@DetailsActivity, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailsActivity, "북마크 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        mainViewModel.apply {
            isBookmarked.observe(this@DetailsActivity) { isBookmarked ->
                this@DetailsActivity.isBookmarked = isBookmarked
            }
        }

        movieViewModel.apply {
            movieCode?.let { getMovieDetails(it) }

            details.observe(this@DetailsActivity) { details ->
                bookmark = BookmarkEntity(
                    movieCode = movieCode ?: "",
                    movieName = details.movieName,
                    posterUrl = details.posterUrl ?: "",
                    movieGenre = details.genres.joinToString(", ") { it }
                )

                binding.txtTitle.text = details.movieName
                binding.txtOpenDate.text = details.openDate?.let { convertDate(it, "yyyy-MM-dd", "yyyy.MM.dd", Locale.KOREA) }
                binding.txtRunTime.text = details.runTime?.let { "${it}분" } ?: "알 수 없음"
                binding.txtNation.text = details.nation ?: "알 수 없음"
                binding.txtGrade.text = when {
                    details.grade?.contains("전체") == true -> "전체"
                    details.grade?.contains("국민학생") == true -> "12세 이상"
                    details.grade?.contains("중학생") == true -> "12세 이상"
                    details.grade?.contains("12세") == true -> "12세 이상"
                    details.grade?.contains("15세") == true -> "15세 이상"
                    details.grade?.contains("18세") == true -> "19세 이상"
                    details.grade?.contains("청소년") == true -> "19세 이상"
                    details.grade?.contains("연소자관람가") == true -> "전체"
                    details.grade?.contains("연소자관람불가") == true -> "19세 이상"
                    details.grade?.contains("연소자불가") == true -> "19세 이상"
                    details.grade?.contains("모든") == true -> "전체"
                    else -> details.grade
                }
                binding.txtPlot.text = details.plot ?: "줄거리 정보가 없습니다."

                if (details.posterUrl.isNullOrEmpty()) {
                    binding.imgPoster.visibility = View.GONE
                } else {
                    binding.imgPoster.visibility = View.VISIBLE
                    Glide.with(binding.root).load(details.posterUrl).centerCrop().into(binding.imgPoster)
                }

                if (details.genres.isNotEmpty()) {
                    binding.recyclerView.visibility = View.VISIBLE
                    genreAdapter.setItemList(details.genres)
                } else {
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }
    }
}