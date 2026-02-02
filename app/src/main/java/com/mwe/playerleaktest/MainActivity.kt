package com.mwe.playerleaktest

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.camera).setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        findViewById<TextView>(R.id.video).setOnClickListener {
            checkFile()
        }
    }

    fun checkFile() {
        lifecycleScope.launch {
            val file =
                File(externalCacheDir, "video.mp4")
            if (!file.exists()) {
                withContext(Dispatchers.IO) {
                    assets.open("video.mp4").use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
            startActivity(Intent(this@MainActivity, PlayerActivity::class.java).apply {
                putExtra("file", file)
            })
        }
    }
}