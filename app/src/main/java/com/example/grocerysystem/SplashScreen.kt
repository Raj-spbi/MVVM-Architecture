package com.example.grocerysystem

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.grocerysystem.databinding.ActivitySplashScreenBinding
import com.example.grocerysystem.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {


    private var animation: Animation? = null
    private val handler = Handler()
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        animation = AnimationUtils.loadAnimation(applicationContext, R.anim.enter_from_left)
        binding.image.startAnimation(animation)


        handler.postDelayed({
            val intent = Intent(applicationContext, LoginActivity::class.java)
            overridePendingTransition(R.anim.enetr_from_right, R.anim.exit_to_left)
            startActivity(intent)
            finish()
        }, 2000)
    }
}