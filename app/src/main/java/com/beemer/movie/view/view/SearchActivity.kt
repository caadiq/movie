package com.beemer.movie.view.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.movie.databinding.ActivitySearchBinding
import com.beemer.movie.model.entity.SearchHistoryEntity
import com.beemer.movie.view.adapter.SearchHistoryAdapter
import com.beemer.movie.viewmodel.SearchHistoryViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }

    private val searchHistoryViewModel by viewModels<SearchHistoryViewModel>()

    private val searchHistoryAdapter = SearchHistoryAdapter()

    private val imm by lazy { getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager }

    private val query by lazy { intent.getStringExtra("query") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupView() {
        imm.showSoftInput(binding.editSearch, InputMethodManager.SHOW_IMPLICIT)

        binding.editSearch.apply {
            setText(query)
            requestFocus()
            setOnEditorActionListener { _, _, _ ->
                if (text.toString().trim().isNotBlank())
                    searchHistoryViewModel.checkTitleExists(text.toString().trim())
                else
                    Toast.makeText(this@SearchActivity, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                true
            }
        }

        binding.txtDeleteAll.setOnClickListener {
            DefaultDialog(
                title = null,
                message = "검색 기록을 삭제하시겠습니까?",
                onConfirm = {
                    searchHistoryViewModel.deleteAllHistory()
                }
            ).show(supportFragmentManager, "defaultDialog")
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = searchHistoryAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
        }

        searchHistoryAdapter.apply {
            setOnItemClickListener { item, _ ->
                binding.editSearch.setText(item)
                searchHistoryViewModel.checkTitleExists(item)
            }
        }
    }

    private fun setupViewModel() {
        searchHistoryViewModel.apply {
            searchHistory.observe(this@SearchActivity) { history ->
                binding.txtRecent.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
                binding.txtDeleteAll.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
                searchHistoryAdapter.setItemList(history.map { it.title })
            }

            isTitleExists.observe(this@SearchActivity) { exists ->
                val query = binding.editSearch.text.toString()
                if (exists)
                    searchHistoryViewModel.deleteHistoryByTitle(query)
                searchHistoryViewModel.insertHistory(SearchHistoryEntity(title = query))
                setResult(RESULT_OK, intent.putExtra("query", query)).also { finish() }
            }
        }
    }
}