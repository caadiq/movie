package com.beemer.movie.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.movie.R
import com.beemer.movie.databinding.RowChartBinding
import com.beemer.movie.model.dto.ChartListDto
import com.beemer.movie.view.diff.ChartListDiffUtil
import com.beemer.movie.view.utils.DateTimeConverter.convertDate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.Locale

class ChartAdapter : RecyclerView.Adapter<ChartAdapter.ViewHolder>() {
    private var itemList = mutableListOf<ChartListDto>()
    private var onItemClickListener: ((ChartListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowChartBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowChartBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: ChartListDto) {
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

            binding.imgRank.drawable
            binding.txtRank.text = item.rank.toString()
            binding.txtTitle.text = item.movieName
            binding.txtGenre.text = item.genre
            binding.txtOpenDate.text = convertDate(item.openDate, "yyyy-MM-dd", "yyyy.MM.dd", Locale.KOREA)

            when {
                item.rankIncrement > 0 -> {
                    binding.imgRank.setImageResource(R.drawable.icon_triangle)
                    binding.imgRank.setColorFilter(binding.root.context.getColor(R.color.blue))
                    binding.imgRank.rotation = 0f
                }
                item.rankIncrement < 0 -> {
                    binding.imgRank.setImageResource(R.drawable.icon_triangle)
                    binding.imgRank.setColorFilter(binding.root.context.getColor(R.color.red))
                    binding.imgRank.rotation = 180f
                }
                else -> {
                    binding.imgRank.setImageResource(R.drawable.icon_horizontal_line)
                    binding.imgRank.setColorFilter(binding.root.context.getColor(R.color.gray))
                    binding.imgRank.rotation = 0f
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (ChartListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<ChartListDto>) {
        val diffCallBack = ChartListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}