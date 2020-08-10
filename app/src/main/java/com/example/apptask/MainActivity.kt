package com.example.apptask

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.net.HttpCookie
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), ClickEvent {
    var loadURLImage: LoadURLImage = LoadURLImage()
    lateinit var imageUrl: Bitmap
    var permission = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var imageLink : String = "https://c8.alamy.com/comp/F1WJN3/full-moon-harvest-moon-large-file-size-from-the-archives-of-press-F1WJN3.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bt_xuly.setOnClickListener {
            loadURLImage.setInterface(this)
            progress_circular.visibility = View.VISIBLE
            loadURLImage.execute(imageLink)
        }

        share_image.setOnClickListener {
            Log.d("AAA", this.imageUrl.toString())
            checkPermission()


        }

    }
    fun getImageUri(context: Context, inImage: Bitmap):Uri?{
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver,inImage,"Title",null)
        return Uri.parse(path)
    }


    override fun btLoadImageOnClick(image: Bitmap) {
        image_main.setImageBitmap(image)
        progress_circular.visibility = View.GONE
        this.imageUrl = image

    }
    fun checkPermission(){
        var result: Int
        val listPermissionsNeed: MutableList<String> = ArrayList()
        for (p in permission){
            result = ContextCompat.checkSelfPermission(this,p)
            if(result != PackageManager.PERMISSION_GRANTED)
                listPermissionsNeed.add(p)
        }
        if (listPermissionsNeed.isNotEmpty()){
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeed.toTypedArray(),
                100
            )
        }
        else{
            Log.d("AAA","image have been save in storega")
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/*"
            share.putExtra(Intent.EXTRA_STREAM,getImageUri(this,this.imageUrl) )
            startActivity(Intent.createChooser(share,"Share to"))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100){
            when{
                grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ->{
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "image/*"
                    share.putExtra(Intent.EXTRA_STREAM,getImageUri(this,this.imageUrl) )
                    startActivity(Intent.createChooser(share,"Share to"))
                }
            }
            return
        }

    }

}