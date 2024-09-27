package com.beemer.movie.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.movie.model.dto.BottomsheetMenuListDto

class BottomsheetMenuListDiffUtil(private val oldList: List<BottomsheetMenuListDto>, private val newList: List<BottomsheetMenuListDto>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].text == newList[newItemPosition].text
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].text == newList[newItemPosition].text
    }
}