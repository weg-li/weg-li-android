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
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.github.weg_li_android.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.photo_list_item.view.*
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.IOException


class PhotoRecyclerViewAdapter internal constructor(
    private val context: Context?,
    private var data: MutableList<Uri>,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder>() {

    private val resources: Resources = context?.resources ?: throw Exception("No context found.")
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    var deletePhotoSet: ObservableArrayList<Int> = ObservableArrayList()
    /*init {
        deletePhotoSet.addOnListChangedCallback(
            RecyclerViewAdapterOnListChangedCallback<ObservableList<Int>>(deletePhotoSet)
        )
    }*/

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = mInflater.inflate(R.layout.photo_list_item, parent, false)
        view.photo_selected_check.visibility = View.VISIBLE
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(
        @NonNull holder: ViewHolder,
        position: Int
    ) {
        val image : Bitmap? = getThumbnail(data[position])
        holder.myAppCompatImageView.background = image?.toDrawable(resources)
        if(position in deletePhotoSet) {
            holder.selectedCheckAppCompatImageView.visibility = View.VISIBLE
        }
        else holder.selectedCheckAppCompatImageView.visibility = View.GONE
    }
/*
    private class RecyclerViewAdapterOnListChangedCallback<Int>(myAppCompatImageView: ObservableArrayList<kotlin.Int>) : ObservableList.OnListChangedCallback<ObservableList<Int>>() {
        val mAppCompatImageView = myAppCompatImageView
        override fun onChanged(sender: ObservableList<Int>?) {

            Timber.e("yooo changed!")
            //adapter.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(sender: ObservableList<Int>?, positionStart: kotlin.Int, itemCount: kotlin.Int) {
            Timber.e("yooo range removed!")
            //adapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<Int>?, fromPosition: kotlin.Int, toPosition: kotlin.Int,  itemCount: kotlin.Int) {
            Timber.e("yooo range mooved!")
        }

        override fun onItemRangeInserted(sender: ObservableList<Int>?, positionStart: kotlin.Int, itemCount: kotlin.Int) {
            Timber.e("yooo range inserted!")
            Timber.e("This is the new list"+sender.toString())
            //adapter.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(sender: ObservableList<Int>?, positionStart: kotlin.Int, itemCount: kotlin.Int) {
            Timber.e("yooo range changed!")
            //adapter.notifyItemRangeChanged(positionStart, itemCount)
        }

    }
*/
    @Throws(FileNotFoundException::class, IOException::class)
    fun getThumbnail(uri: Uri?): Bitmap? {
        if(context !=null) {
            val thumbnailSize = 120f.convertDpToPixel(context).toInt()
            val thumbnail: Bitmap? =
                uri?.let {
                    context.contentResolver.loadThumbnail(
                        it, Size(thumbnailSize, thumbnailSize), null
                    )
                }.run {
                    ThumbnailUtils.extractThumbnail(this, thumbnailSize, thumbnailSize)
                }
            if (thumbnail != null) {
                Timber.e(thumbnail.width.toString() + "-" + thumbnail.height.toString() + "shouldbe" + thumbnailSize.toString())
            }
            return thumbnail
        }
        else return null
    }

    // total number of cells
    override fun getItemCount(): Int {
        return data.size
    }

    fun removeAt(index : Int) {
        //data.removeAt(index)
        mainViewModel.removeViolationPhoto(index)
    }

    private fun Float.convertDpToPixel(context: Context): Float {
        return this * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        var myAppCompatImageView: AppCompatImageView = itemView.findViewById(R.id.photo_list_item)
        var selectedCheckAppCompatImageView: AppCompatImageView = itemView.findViewById(R.id.photo_selected_check)
        override fun onClick(view: View?) {
            mClickListener?.onItemClick(view, adapterPosition)
        }
        override fun onLongClick(view: View?): Boolean {
            mClickListener?.onItemLongClick(view, adapterPosition)
            return true
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }
    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

}