package com.example.readimages

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_display_image.*

class DisplayImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        //画像のURIを取り出す
        val path = intent.getStringExtra("path")
        //画像をBitmap形式に変換
        val bitmap = BitmapFactory.decodeFile(path)
        //ImageViewに画像をセット
        imageView.setImageBitmap(bitmap)
    }
}
