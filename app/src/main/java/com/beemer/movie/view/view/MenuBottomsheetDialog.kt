package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beemer.movie.databinding.DialogBottomsheetMenuBinding
import com.beemer.movie.model.dto.BottomsheetMenuListDto
import com.beemer.movie.view.adapter.BottomsheetMenuAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetDialog(private val list: List<BottomsheetMenuListDto>, private val onItemClick: (item: BottomsheetMenuListDto, position: Int) -> Unit) : BottomSheetDialogFragment() {
    private var _binding: DialogBottomsheetMenuBinding? = null
    private val binding get() = _binding!!

    private val bottomsheetMenuAdapter = BottomsheetMenuAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogBottomsheetMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = bottomsheetMenuAdapter
            setHasFixedSize(true)
        }

        bottomsheetMenuAdapter.apply {
            setItemList(list)

            setOnItemClickListener { item, position ->
                onItemClick(item, position)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}