package com.beemer.movie.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.movie.view.adapter.ChartItem

class ChartListDiffUtil(private val oldList: List<ChartItem>, private val newList: List<ChartItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is ChartItem.Chart && newItem is ChartItem.Chart) {
            oldItem.chartListDto.movieCode == newItem.chartListDto.movieCode
        } else {
            oldItem::class == newItem::class
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}