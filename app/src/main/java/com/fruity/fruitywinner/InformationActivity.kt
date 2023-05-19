package com.fruity.fruitywinner

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
    }
}