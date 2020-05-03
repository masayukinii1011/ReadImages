package com.example.readimages

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var rs: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                121
            )
        }
        listImages()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 121 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            listImages()
        }
    }

    private fun listImages() {

        var cols = arrayOf<String>(MediaStore.Images.Thumbnails.DATA)

        rs = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            cols, null, null, null
        )!!

        gridview.adapter = ImageAdapter(applicationContext)
        gridview.setOnItemClickListener { adapterView, view, i, l ->
            rs.moveToPosition(i)
            var path = rs.getString(0)
            var i = Intent(applicationContext, DisplayImageActivity::class.java)
            i.putExtra("path", path)
            startActivity(i)
        }
    }

    inner class ImageAdapter : BaseAdapter {

        lateinit var context: Context

        constructor(context: Context) {
            this.context = context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var iv = ImageView(context)
            rs.moveToPosition(p0)
            var path = rs.getString(0)
            var bitmap = BitmapFactory.decodeFile(path)
            iv.setImageBitmap(bitmap)
            iv.layoutParams = AbsListView.LayoutParams(300, 300)
            return iv
        }

        override fun getItem(p0: Int): Any {
            return p0
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return rs.count
        }
    }
}
