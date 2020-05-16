package com.github.weg_li_android

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.floor


class PhotoRecyclerViewAdapter internal constructor(
    private val context: Context?,
    private val data: MutableList<Uri>
) :
    RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder>() {
    private val resources: Resources = context?.resources ?: throw Exception("No context found.")
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    // inflates the cell layout from xml when needed
    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = mInflater.inflate(R.layout.photo_list_item, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(
        @NonNull holder: ViewHolder,
        position: Int
    ) {
        val image : Bitmap? = getThumbnail(data[position])
        holder.myAppCompatImageView.background = image!!.toDrawable(resources)
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun getThumbnail(uri: Uri?): Bitmap? {
        val thumbnailSize = convertDpToPixel(120f, context!!).toInt()
        val thumbnail: Bitmap? =
            uri?.let {
                context.contentResolver!!.loadThumbnail(
                    it, Size(thumbnailSize, thumbnailSize), null)
            }.run {
                ThumbnailUtils.extractThumbnail(this, thumbnailSize, thumbnailSize);
            }
        if (thumbnail != null) {
            Timber.e(thumbnail.width.toString()+"-"+thumbnail.height.toString()+"shouldbe"+thumbnailSize.toString())
        }
        return thumbnail
    }

    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
        val k = Integer.highestOneBit(floor(ratio).toInt())
        return if (k == 0) 1 else k
    }


    // total number of cells
    override fun getItemCount(): Int {
        return data.size
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var myAppCompatImageView: AppCompatImageView = itemView.findViewById(R.id.photo_list_item)
        override fun onClick(view: View?) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }
    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}