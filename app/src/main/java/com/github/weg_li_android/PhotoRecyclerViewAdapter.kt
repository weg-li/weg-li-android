package com.github.weg_li_android

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView


class PhotoRecyclerViewAdapter internal constructor(
    context: Context?,
    data: MutableList<Bitmap>
) :
    RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder>() {
    private val mData: MutableList<Bitmap> = data
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
        holder.myAppCompatImageView.background = mData[position].toDrawable(resources)
    }

    // total number of cells
    override fun getItemCount(): Int {
        return mData.size
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

    fun getItem(id: Int): Bitmap {
        return mData[id]
    }

    fun addItem(image: Bitmap) : Int {
        mData.add(image)
        return mData.lastIndex
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