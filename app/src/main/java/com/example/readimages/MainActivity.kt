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

        /**
         * パーミッションの許可が無い場合、許可ダイアログを表示
         */
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

    /**
     * 許可ダイアログの結果を受け取る
     */
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

    /**
     * 画像をリスト表示
     */
    private fun listImages() {

        //contentResolver.query()で抽出したい項目
        val cols = arrayOf<String>(MediaStore.Images.Thumbnails.DATA)

        //Cursor
        //(検索したいURI, 抽出したい項目, 絞り込み条件, 絞り込みパラメータ, ソート)
        rs = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            cols, null, null, null
        )!!
        //!!、nullの場合はNullPointerExceptionを発生させる

        //画像をGridViewへ渡す
        gridview.adapter = ImageAdapter(applicationContext)

        //GridViewの項目をクリックした時
        gridview.setOnItemClickListener { adapterView, view, i, l ->
            //Cursor位置を変更
            rs.moveToPosition(i)

            //現在のCursor位置のURIをIntentに渡してDisplayImageActivityを起動
            val path = rs.getString(0)
            val intent = Intent(applicationContext, DisplayImageActivity::class.java)
            intent.putExtra("path", path)
            startActivity(intent)
        }
    }

    inner class ImageAdapter(private val context: Context) : BaseAdapter() {

        // ImageViewを作成
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val iv = ImageView(context)
            rs.moveToPosition(p0)
            val path = rs.getString(0)
            val bitmap = BitmapFactory.decodeFile(path)
            iv.setImageBitmap(bitmap)
            iv.layoutParams = AbsListView.LayoutParams(300, 300)
            return iv
        }

        //indexを返す
        override fun getItem(p0: Int): Any {
            return p0
        }

        //特別なIDをindexの他に返す
        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        //要素数を返す
        override fun getCount(): Int {
            return rs.count
        }
    }
}
