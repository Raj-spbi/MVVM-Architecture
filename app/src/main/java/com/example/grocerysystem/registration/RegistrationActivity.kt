package com.example.grocerysystem.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.databinding.ActivityRegistrationBinding
import com.example.grocerysystem.login.LoginActivity
import com.example.grocerysystem.model.RegisterRequest
import com.example.grocerysystem.util.Helper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private var _binding: ActivityRegistrationBinding? = null
    private val binding get() = _binding!!
    private var _context: Context? = null
    private val context get() = _context!!
    private val registrationViewModel: RegistrationViewModel by viewModels<RegistrationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _context = this

        /*  registrationViewModel = ViewModelProvider(
              this, RegisterVMFactory(
                  RegistrationRepository()
              )
          )[RegistrationViewModel::class.java]*/

        binding.register.setOnClickListener {

            val validateUser = validateUserInput()
            if (validateUser.first) {
                registrationViewModel.registerUser(getUserRequest())
            } else {
                Toast.makeText(context, validateUser.second, Toast.LENGTH_SHORT).show()
            }
        }


        binding.login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        bindingObservables()
    }

    private fun bindingObservables() {
        registrationViewModel.userResponseLiveData.observe(this, Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    Toast.makeText(context, it.data.toString(), Toast.LENGTH_SHORT).show()
                    binding.name.editText?.editableText?.clear()
                    binding.email.editText?.editableText?.clear()
                    binding.password.editText?.editableText?.clear()
                    binding.name.editText?.requestFocus()
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


    private fun getUserRequest(): RegisterRequest {
        val userName = binding.name.editText?.editableText.toString()
        val emailAddress = binding.email.editText?.editableText.toString()
        val password = binding.password.editText?.editableText.toString()
        return RegisterRequest(userName, emailAddress, password, "", "")
    }


    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return registrationViewModel.validateCredentials(
            userRequest.username!!,
            userRequest.email!!,
            userRequest.password!!,
            userRequest.cnfPassword!!
        )
    }
}