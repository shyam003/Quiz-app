package com.example.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SplashScreen : AppCompatActivity() {
    lateinit var cl:ConstraintLayout
    lateinit var tv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash_screen)
          tv = findViewById(R.id.textView4)
        cl = findViewById(R.id.splash)
         Handler(Looper.getMainLooper()).postDelayed({
                 val intnt: Intent = Intent(this@SplashScreen,MainActivity::class.java)
             startActivity(intnt)
             finish()
         },5000)
        anime()
    }

    private fun anime() {
        val anim:Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        cl.animation=anim
        val anim2:Animation = AnimationUtils.loadAnimation(applicationContext,R.anim.myanime)
        tv.animation=anim2
    }
}