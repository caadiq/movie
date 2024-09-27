package com.beemer.movie.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.movie.databinding.RowBottomsheetMenuBinding
import com.beemer.movie.model.dto.BottomsheetMenuListDto
import com.beemer.movie.view.diff.BottomsheetMenuListDiffUtil
import com.bumptech.glide.Glide

class BottomsheetMenuAdapter : RecyclerView.Adapter<BottomsheetMenuAdapter.ViewHolder>() {
    private var itemList = mutableListOf<BottomsheetMenuListDto>()
    private var onItemClickListener: ((BottomsheetMenuListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowBottomsheetMenuBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowBottomsheetMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: BottomsheetMenuListDto) {
            Glide.with(binding.root).load(item.icon).into(binding.imgMenu)
            binding.txtMenu.text = item.text
        }
    }

    fun setOnItemClickListener(listener: (BottomsheetMenuListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<BottomsheetMenuListDto>) {
        val diffCallBack = BottomsheetMenuListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}