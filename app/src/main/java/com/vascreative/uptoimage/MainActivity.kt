package com.vascreative.uptoimage

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_PERMISSION = 0
        const val PERMISSIONS_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        val PERMISSIONS_CONTACT = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private lateinit var imageUrls: Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isPermissionGranted(PERMISSIONS_READ_EXTERNAL_STORAGE)) {
            batchRequestPermissions(PERMISSIONS_CONTACT, REQUEST_PERMISSION)
        }else{
            getImageFilesPath()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION) {
            Log.i(TAG, "Received response for READ_EXTERNAL_STORAGE permission request.")
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                getImageFilesPath()
            }else{
                Snackbar.make(mainLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show()
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getImageFilesPath(){
        val selectionArgs = arrayOf("image/jpeg", "image/png")
        val cursor: Cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                selectionArgs,
                MediaStore.Images.Media.DATE_MODIFIED)

        if(cursor.count > 0){
            var i = 0
            imageUrls = arrayOfNulls(cursor.count)
            while (cursor.moveToNext()){
                val path: String = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                imageUrls[i++] = path
                Log.d(TAG, "path = $path")
            }
        }
        cursor.close()

        showImageList()
    }

    private fun showImageList(){
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ListAdapter(this, imageUrls)
    }
}
