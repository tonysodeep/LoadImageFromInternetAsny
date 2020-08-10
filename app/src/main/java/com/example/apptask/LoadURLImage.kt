package com.example.apptask

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.lang.ref.WeakReference
import java.net.URL

class LoadURLImage : AsyncTask<String,Void, Bitmap>() {
    private lateinit var loadInterface : ClickEvent
    lateinit var imageBitmap: Bitmap
    fun setInterface(inter: ClickEvent){
        this.loadInterface = inter
    }
    override fun doInBackground(vararg params: String?): Bitmap {

        val url = URL(params[0])
        val inputStream = url.openConnection().getInputStream()
        val bitmap : Bitmap = BitmapFactory.decodeStream(inputStream)

        return bitmap
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        if (result != null) {
            this.loadInterface.btLoadImageOnClick(result)
            this.imageBitmap = result
        }
    }


}