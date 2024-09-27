package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beemer.movie.R
import com.beemer.movie.databinding.FragmentBookmarkBinding
import com.beemer.movie.model.dto.BookmarkListDto
import com.beemer.movie.model.dto.BottomsheetMenuListDto
import com.beemer.movie.view.adapter.BookmarkAdapter

class BookmarkFragment : Fragment(), BookmarkAdapter.OnViewClickListener {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarkAdapter : BookmarkAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setOnViewClick(item: BookmarkListDto) {
        MenuBottomSheetDialog(
            list = listOf(
                BottomsheetMenuListDto(R.drawable.icon_bookmark, "북마크에서 삭제")
            ),
            onItemClick = { menu, _ ->
                when (menu.text) {
                    "북마크에서 삭제" -> {
                        // TODO: 북마크에서 삭제
                    }
                }
            }
        ).show(childFragmentManager, "MenuBottomSheetDialog")
    }

    private fun setupRecyclerView() {
        bookmarkAdapter = BookmarkAdapter(this)

        bookmarkAdapter.setItemList(listOf(
            BookmarkListDto("20239670", "베테랑2", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022660.jpg", "액션, 범죄"),
            BookmarkListDto("20249733", "사랑의 하츄핑", "http://file.koreafilm.or.kr/thm/02/99/18/48/tn_DPK022363.jpg", "애니메이션"),
            BookmarkListDto("20242495", "브레드이발소: 빵스타의 탄생", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022665.jpg", "애니메이션"),
            BookmarkListDto("20149629", "비긴 어게인", "http://file.koreafilm.or.kr/thm/02/00/02/90/tn_DPF007914.JPG", "멜로/로맨스"),
            BookmarkListDto("20243721", "정국: 아이 엠 스틸", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPA002033.jpg", "공연")
        ))

        binding.recyclerView.apply {
            adapter = bookmarkAdapter
            setHasFixedSize(true)
        }
        
        binding.btnDeleteAll.visibility = View.VISIBLE // 임시
    }
}