package com.vascreative.uptoimage

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class ListAdapter(private val context: Context, private val dataSet: Array<String?>) : RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    companion object {
        private const val DIALOG_FRAGMENT_TAG = "dialogFragment"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            //nothing
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context)
        val width : Int = parent.resources.displayMetrics.widthPixels / 2
        val height : Int = width / 4 * 3
        imageView.layoutParams = ViewGroup.LayoutParams(width, height)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        return ViewHolder(imageView)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageView: ImageView = holder.itemView as ImageView
        imageView.setImageURI(Uri.parse(dataSet[position]))
        imageView.setOnClickListener {
            val dialogFragment = ImageDialogFragment()
            val bundle = Bundle()
            bundle.putString("url", dataSet[position])
            dialogFragment.arguments = bundle
            dialogFragment.show((context as Activity).fragmentManager, DIALOG_FRAGMENT_TAG)
        }
    }
}