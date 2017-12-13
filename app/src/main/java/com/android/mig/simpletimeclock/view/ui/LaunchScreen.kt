package com.android.mig.simpletimeclock.view.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class LaunchScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}