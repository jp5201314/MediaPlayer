package com.jp.mediaplayer

import android.Manifest
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_texture_view.*

class TextureViewMainActivity : AppCompatActivity() {
    private val mediaPlayer: MediaPlayer by lazy {
        MediaPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texture_view)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
        mediaPlayer.setScreenOnWhilePlaying(true)
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.setOnCompletionListener {
            btn_play.text = "开始"
            mediaPlayer.reset()
        }
        val textureView = TextureView(this)
        frameLayout.addView(textureView)
        btn_play.setOnClickListener {
            if (btn_play.text == "开始") {
                mediaPlayer.reset()
                btn_play.text = "停止"
                val openNonAssetFd = assets.openFd("test.mp4")
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    mediaPlayer.setDataSource(openNonAssetFd)
                } else {
                    mediaPlayer.setDataSource(
                        Environment.getExternalStorageDirectory()
                            .absolutePath + "/Movies/test.mp4"
                    )
                }
                val surface = Surface(textureView.surfaceTexture)
                mediaPlayer.setSurface(surface)
                mediaPlayer.prepareAsync()
            } else {
                btn_play.text = "开始"
                mediaPlayer.stop()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}