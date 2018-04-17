package com.vascreative.uptoimage

import android.app.DialogFragment
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView

class ImageDialogFragment: DialogFragment(){
    companion object {
        private const val TAG = "ImageDialogFragment"
    }

    private lateinit var imageView: ImageView
    private lateinit var smallButton: ImageButton
    private lateinit var bigButton: ImageButton
    private lateinit var url: String

    private var drawableWidth: Int = 0
    private var drawableHeight: Int = 0
    private var zoomWidth: Float = 10F
    private var zoomHeight: Float = 0F
    private var zoomMinValue = 0

    override fun setArguments(args: Bundle) {
        url = args.getString("url")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog.setTitle(getString(R.string.scale))
        return inflater.inflate(R.layout.image_controller_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageView)
        smallButton = view.findViewById(R.id.smallButton)
        bigButton = view.findViewById(R.id.bigButton)

        if(url.isNotEmpty()){
            imageView.setImageURI(Uri.parse(url))
        }

        smallButton.setOnClickListener(ButtonClickListener())
        bigButton.setOnClickListener(ButtonClickListener())
    }

    private inner class ButtonClickListener: View.OnClickListener {
        override fun onClick(view: View) {
            if(url.isNotEmpty()){
                val drawable = imageView.drawable
                val width = drawable.bounds.width()
                val height = drawable.bounds.height()
                val zoomMin = if(drawableWidth < drawableHeight) width else height

                if(drawable.bounds.left == 0){
                    zoomHeight = zoomWidth / width * height
                    drawableWidth = width
                    drawableHeight = height
                    zoomMinValue = if(drawableWidth < drawableHeight) width / 2 else height / 2
                }

                Log.d(TAG, "zoomMin = $zoomMin, zoomMinValue = $zoomMinValue")

                if(view.id == R.id.smallButton) {
                    if (zoomMin > zoomMinValue) {
                        drawable.setBounds((drawableWidth - width) / 2 + zoomWidth.toInt(), (drawableHeight - height) / 2 + zoomHeight.toInt(),
                                (width + drawable.bounds.left) - zoomWidth.toInt(), (height + drawable.bounds.top) - zoomHeight.toInt())
                    }
                }else{
                    drawable.setBounds((drawableWidth - width) / 2 - zoomWidth.toInt(), (drawableHeight - height) / 2 - zoomHeight.toInt(),
                            (width + drawable.bounds.left) + zoomWidth.toInt(), (height + drawable.bounds.top) + zoomHeight.toInt())
                }

                Log.d(TAG, "left = " + drawable.bounds.left + ", top = " + drawable.bounds.top
                        + ", right = " + drawable.bounds.right + ", bottom = " + drawable.bounds.bottom)
            }
        }
    }
}