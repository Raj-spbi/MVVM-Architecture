package com.example.grocerysystem.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import com.example.grocerysystem.R
import com.example.grocerysystem.databinding.ActivitySplashScreenBinding
import com.example.grocerysystem.login.LoginActivity
import com.example.grocerysystem.showProducts.AdminDashboardActivity
import com.example.grocerysystem.util.Const
import com.example.grocerysystem.util.UserManager

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private var animation: Animation? = null
    private val handler = Handler()
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var userManager: UserManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        userManager = UserManager(this)

        animation = AnimationUtils.loadAnimation(applicationContext, R.anim.enter_from_left)
        binding.image.startAnimation(animation)

        observeData()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeData() {

        this.userManager.userIdFlow.asLiveData().observe(this) {
            handler.postDelayed({
                if (it.toString().isNotBlank()) {
                    val intent = Intent(applicationContext, AdminDashboardActivity::class.java)
                    intent.putExtra(Const.isSeller, Const.USER)
                    overridePendingTransition(R.anim.enetr_from_right, R.anim.exit_to_left)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    overridePendingTransition(R.anim.enetr_from_right, R.anim.exit_to_left)
                    startActivity(intent)
                    finish()
                }
            }, 2000)
        }

    }
}