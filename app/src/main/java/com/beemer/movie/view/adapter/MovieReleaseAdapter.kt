package com.beemer.movie.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.movie.databinding.RowHomeMovieReleaseBinding
import com.beemer.movie.model.dto.MovieReleaseListDto
import com.beemer.movie.view.diff.MovieReleaseListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MovieReleaseAdapter : RecyclerView.Adapter<MovieReleaseAdapter.ViewHolder>() {
    private var itemList = mutableListOf<MovieReleaseListDto>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeMovieReleaseBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeMovieReleaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieReleaseListDto) {
            Glide.with(binding.root)
                .load(item.poster)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
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

            binding.txtTitle.text = item.title
            binding.txtOpenDate.text = item.openDate
        }
    }

    fun setItemList(list: List<MovieReleaseListDto>) {
        val diffCallBack = MovieReleaseListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}