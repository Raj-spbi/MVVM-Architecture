package com.example.grocerysystem.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.grocerysystem.Helper
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.R
import com.example.grocerysystem.RegistrationActivity
import com.example.grocerysystem.databinding.ActivityLoginBinding
import com.example.grocerysystem.model.LoginRequest
import com.example.grocerysystem.model.RegisterRequest
import com.example.grocerysystem.showProducts.AdminDashboardActivity
import com.example.grocerysystem.util.FirebaseUtil

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!


    private var _context: Context? = null
    private val context get() = _context!!

    private lateinit var currentUserDetails: RegisterRequest
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        _context = this
        loginViewModel =
            ViewModelProvider(this, LoginVMFactory(LoginRepository())).get(
                LoginViewModel::class.java
            )


        binding.seller.setOnClickListener {
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
        }

        binding.signup.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)

        }

        binding.login.setOnClickListener {
            val validateUser = validateUserInput()
            if (validateUser.first) {
                loginViewModel.loginUser(getUserRequest())
            } else {
                Toast.makeText(context, validateUser.second, Toast.LENGTH_SHORT).show()
            }
        }


        bindObservals()


    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return loginViewModel.validateCredentialsLogin(
            userRequest.email,
            userRequest.password,
        )
    }


    private fun getUserRequest(): LoginRequest {
        val emailAddress = binding.email.editText?.editableText.toString()
        val password = binding.password.editText?.editableText.toString()
        return LoginRequest(emailAddress, password)
    }

    private fun bindObservals() {
        loginViewModel.loginLiveData.observe(this, Observer {

            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    currentUserDetails = it.data!!


//                    CoroutineScope(Dispatchers.IO).launch {
//                        Log.e("cbvhgdjk", "bindObservals: " + currentUserDetails.username)
//                        userDetails.storeUser(
//                            currentUserDetails.username.toString()
//                        )
//                    }
                    gotoMainPage(context, currentUserDetails)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Helper.showLoadingDialog(context)
                }
            }
        })
    }

    private fun gotoMainPage(context: Context, currentUserDetails: RegisterRequest) {
        Helper.showToast(context, "Hello ${currentUserDetails.username}")
    }

}