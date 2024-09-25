package com.beemer.movie.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.movie.model.dto.MovieReleaseListDto

class MovieReleaseListDiffUtil(private val oldList: List<MovieReleaseListDto>, private val newList: List<MovieReleaseListDto>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].movieCode == newList[newItemPosition].movieCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].movieCode == newList[newItemPosition].movieCode
    }
}