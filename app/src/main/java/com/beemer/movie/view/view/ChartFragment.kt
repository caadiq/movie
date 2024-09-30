package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beemer.movie.databinding.FragmentChartBinding
import com.beemer.movie.model.dto.ChartListDto
import com.beemer.movie.view.adapter.ChartAdapter
import com.beemer.movie.view.adapter.ChartItem

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private val chartAdapter = ChartAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
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

    private fun setupRecyclerView() {
        val chartItems = mutableListOf<ChartItem>()

        val items = listOf(
            ChartListDto(
                movieCode = "20239670",
                movieName = "베테랑2",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022660.jpg",
                openDate = "2024-09-13",
                genre = "액션, 범죄",
                rank = 1,
                rankIncrement = 0
            ),
            ChartListDto(
                movieCode = "20242541",
                movieName = "룩백",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/99/18/52/tn_DPF029868.jpg",
                openDate = "2024-09-05",
                genre = "애니메이션",
                rank = 2,
                rankIncrement = 4
            ),
            ChartListDto(
                movieCode = "20149629",
                movieName = "비긴 어게인",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/00/02/90/tn_DPF007914.JPG",
                openDate = "2014-08-13",
                genre = "멜로/로맨스",
                rank = 3,
                rankIncrement = 1
            ),
            ChartListDto(
                movieCode = "20249188",
                movieName = "에이리언: 로물루스",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/99/18/51/tn_DPF029803.jpg",
                openDate = "2024-08-14",
                genre = "공포(호러), SF",
                rank = 4,
                rankIncrement = 3
            ),
            ChartListDto(
                movieCode = "20243721",
                movieName = "정국: 아이 엠 스틸",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPA002033.jpg",
                openDate = "2024-09-18",
                genre = "공연",
                rank = 5,
                rankIncrement = 0
            ),
            ChartListDto(
                movieCode = "20242794",
                movieName = "임영웅│아임 히어로 더 스타디움",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/99/18/51/tn_DPA002017.jpg",
                openDate = "2024-08-28",
                genre = "공연, 다큐멘터리",
                rank = 6,
                rankIncrement = 2
            ),
            ChartListDto(
                movieCode = "20200162",
                movieName = "소년시절의 너",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/00/05/59/tn_DPF020714.jpg",
                openDate = "2020-07-09",
                genre = "멜로/로맨스, 드라마, 범죄, 가족",
                rank = 7,
                rankIncrement = 6
            ),
            ChartListDto(
                movieCode = "20240925",
                movieName = "안녕, 할부지",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPA002035.jpg",
                openDate = "2024-09-04",
                genre = "다큐멘터리, 애니메이션",
                rank = 8,
                rankIncrement = 2
            ),
            ChartListDto(
                movieCode = "20242495",
                movieName = "브레드이발소: 빵스타의 탄생",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022665.jpg",
                openDate = "2024-09-14",
                genre = "한국, 애니메이션",
                rank = 9,
                rankIncrement = -6
            ),
            ChartListDto(
                movieCode = "20249733",
                movieName = "사랑의 하츄핑",
                posterUrl = "http://file.koreafilm.or.kr/thm/02/99/18/48/tn_DPK022363.jpg",
                openDate = "2024-08-07",
                genre = "애니메이션",
                rank = 10,
                rankIncrement = -8
            )
        )

        chartItems.add(ChartItem.Header("2024.09.19 기준"))
        chartItems.addAll(items.map { ChartItem.Chart(it) })

        chartAdapter.setItemList(chartItems)

        binding.recyclerView.apply {
            adapter = chartAdapter
            setHasFixedSize(true)
        }
    }
}