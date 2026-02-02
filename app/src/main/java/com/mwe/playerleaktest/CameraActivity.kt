package com.mwe.playerleaktest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.VideoResult
import java.io.File

class CameraActivity : AppCompatActivity() {
    lateinit var cameraView: CameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_camera)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        cameraView = findViewById<CameraView>(R.id.camera).apply {
            setLifecycleOwner(this@CameraActivity)
        }

        cameraView.addCameraListener(object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {
                super.onCameraOpened(options)
                cameraView.postDelayed({
                    takeVideo()
                },2000)
            }

            override fun onVideoTaken(result: VideoResult) {
                startActivity(Intent(this@CameraActivity, PlayerActivity::class.java).apply {
                    putExtra("file", result.file)
                })
                finish()
            }
        })

        findViewById<Button>(R.id.video).setOnClickListener {

        }

    }

    private fun takeVideo() {
        val file = File(externalCacheDir, "${System.currentTimeMillis()}.mp4")
        cameraView.takeVideo(file, 3000)
    }
}