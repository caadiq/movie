package com.beemer.movie.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.movie.R
import com.beemer.movie.databinding.RowBookmarkBinding
import com.beemer.movie.model.dto.BookmarkListDto
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class BookmarkAdapter(
    private val listenerMenu: OnMenuClickListener,
    private val listenerDetails: OnDetailsClickListener
) : PagingDataAdapter<BookmarkListDto, BookmarkAdapter.ViewHolder>(DIFF_CALLBACK) {
    interface OnMenuClickListener {
        fun setOnMenuClick(item: BookmarkListDto)
    }

    interface OnDetailsClickListener {
        fun setOnDetailsClick(item: BookmarkListDto)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookmarkListDto>() {
            override fun areItemsTheSame(oldItem: BookmarkListDto, newItem: BookmarkListDto): Boolean {
                return oldItem.movieCode == newItem.movieCode
            }

            override fun areContentsTheSame(oldItem: BookmarkListDto, newItem: BookmarkListDto): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowBookmarkBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: RowBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookmarkListDto) {
            Glide.with(binding.root)
                .load(item.posterUrl)
                .error(R.drawable.icon_no_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        binding.progressIndicator.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        binding.progressIndicator.visibility = View.GONE
                        return false
                    }
                })
                .into(binding.imgPoster)

            binding.txtTitle.text = item.movieName
            binding.txtGenre.text = item.genre

            binding.imgMenu.setOnClickListener {
                listenerMenu.setOnMenuClick(item)
            }

            binding.btnDetails.setOnClickListener {
                listenerDetails.setOnDetailsClick(item)
            }
        }
    }
}