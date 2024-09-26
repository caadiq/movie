package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.fragment.app.Fragment
import com.beemer.movie.databinding.FragmentSearchBinding
import com.beemer.movie.view.adapter.SearchHistoryAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setupRecyclerView() {
        searchHistoryAdapter.apply {
            setItemList(listOf(
                "베테랑2", "사랑의 하츄핑", "브레드이발소: 빵스타의 탄생", "비긴 어게인", "정국: 아이 엠 스틸"
            ))

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

    private fun search(query: String) {
        imm.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)
        binding.editSearch.clearFocus()
        // TODO: 검색
    }
}