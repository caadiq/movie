package com.beemer.movie.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.beemer.movie.R
import com.beemer.movie.databinding.FragmentBookmarkBinding
import com.beemer.movie.model.dto.BookmarkListDto
import com.beemer.movie.model.dto.BottomsheetMenuListDto
import com.beemer.movie.view.adapter.BookmarkAdapter
import com.beemer.movie.viewmodel.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment(), BookmarkAdapter.OnMenuClickListener, BookmarkAdapter.OnDetailsClickListener {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    private lateinit var bookmarkAdapter : BookmarkAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
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

    override fun setOnMenuClick(item: BookmarkListDto) {
        MenuBottomSheetDialog(
            list = listOf(
                BottomsheetMenuListDto(R.drawable.icon_bookmark, "북마크에서 삭제")
            ),
            onItemClick = { _, position ->
                when (position) {
                    0 -> {
                        DefaultDialog(
                            title = null,
                            message = "북마크에서 삭제하시겠습니까?",
                            onConfirm = {
                                bookmarkViewModel.deleteBookmarkByCode(item.movieCode)
                            }
                        ).show(childFragmentManager, "DeleteBookmarkDialog")
                    }
                }
            }
        ).show(childFragmentManager, "MenuBottomSheetDialog")
    }

    override fun setOnDetailsClick(item: BookmarkListDto) {
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra("code", item.movieCode)
        startActivity(intent)
    }

    private fun setupView() {
        binding.btnDeleteAll.setOnClickListener {
            DefaultDialog(
                title = null,
                message = "모든 북마크를 삭제하시겠습니까?",
                onConfirm = {
                    bookmarkViewModel.deleteAllBookmark()
                }
            ).show(childFragmentManager, "DeleteAllBookmarkDialog")
        }
    }

    private fun setupRecyclerView() {
        bookmarkAdapter = BookmarkAdapter(this, this)

        binding.recyclerView.apply {
            adapter = bookmarkAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupViewModel() {
        bookmarkViewModel.apply {
            bookmark.observe(viewLifecycleOwner) { list ->
                binding.btnDeleteAll.visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE
                binding.txtEmptyList.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

                bookmarkAdapter.setItemList(list.map {
                    BookmarkListDto(
                        movieCode = it.movieCode,
                        movieName = it.movieName,
                        posterUrl = it.posterUrl,
                        genre = it.movieGenre
                    )
                })
            }

            deleteResult.observe(viewLifecycleOwner) { result ->
                if (result) {
                    Toast.makeText(requireContext(), "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            deleteAllResult.observe(viewLifecycleOwner) { result ->
                if (result) {
                    Toast.makeText(requireContext(), "모든 북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}