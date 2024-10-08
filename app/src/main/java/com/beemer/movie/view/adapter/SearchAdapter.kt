package com.beemer.movie.view.adapter

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.movie.R
import com.beemer.movie.databinding.RowProgressBinding
import com.beemer.movie.databinding.RowSearchBinding
import com.beemer.movie.model.dto.SearchList
import com.beemer.movie.view.diff.SearchListDiffUtil
import com.beemer.movie.view.utils.DateTimeConverter.convertDate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.Locale

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<SearchList>()
    private var onItemClickListener: ((SearchList, Int) -> Unit)? = null
    private var isLoading = false

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun getItemCount(): Int = if (isLoading) itemList.size + 1 else itemList.size

    override fun getItemViewType(position: Int): Int = if (isLoading && position == itemList.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = RowSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        } else {
            val binding = RowProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(itemList[position])
        }
    }

    inner class ViewHolder(private val binding: RowSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: SearchList) {
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

            binding.txtGrade.apply {
                text = when (item.grade) {
                    "전체관람가" -> "ALL"
                    "12세이상관람가" -> "12"
                    "15세이상관람가" -> "15"
                    "18세이상관람가" -> "18"
                    "청소년관람불가" -> "19"
                    "연소자관람가" -> "ALL"
                    "연소자관람불가" -> "19"
                    else -> item.grade
                }
                backgroundTintList = when (item.grade) {
                    "전체관람가" -> ColorStateList.valueOf(context.getColor(R.color.grade_all))
                    "12세이상관람가" -> context.getColorStateList(R.color.grade_12)
                    "15세이상관람가" -> context.getColorStateList(R.color.grade_15)
                    "18세이상관람가" -> context.getColorStateList(R.color.grade_18)
                    "청소년관람불가" -> context.getColorStateList(R.color.grade_19)
                    "연소자관람가" -> context.getColorStateList(R.color.grade_all)
                    "연소자관람불가" -> context.getColorStateList(R.color.grade_19)
                    else -> context.getColorStateList(R.color.grade_all)
                }
                visibility = if (item.grade == null) View.GONE else View.VISIBLE
            }
            binding.txtTitle.text = item.movieName
            binding.txtGenre.text = item.genre
            binding.txtOpenDate.text = item.openDate?.let { convertDate(it, "yyyy-MM-dd", "yyyy.MM.dd", Locale.KOREA) }
        }
    }

    inner class LoadingViewHolder(binding: RowProgressBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickListener(listener: (SearchList, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<SearchList>) {
        val diffCallback = SearchListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun showProgress() {
        isLoading = true
        notifyItemInserted(itemList.size)
    }

    fun hideProgress() {
        isLoading = false
        notifyItemRemoved(itemList.size)
    }
}