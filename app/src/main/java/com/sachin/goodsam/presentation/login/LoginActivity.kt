package com.sachin.goodsam.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sachin.goodsam.GoodSamApp
import com.sachin.goodsam.databinding.ActivityLoginBinding
import com.sachin.goodsam.presentation.otp.OtpActivity
import com.sachin.goodsam.util.UiState
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory {
            initializer {
                LoginViewModel((application as GoodSamApp).container.signInUseCase)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val mobile = binding.etMobile.text.toString().trim()
            if (mobile.length != 10) {
                binding.etMobile.error = "Enter a 10-digit mobile number"
                return@setOnClickListener
            }
            viewModel.login(mobile)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progress.visibility =
                        if (state is UiState.Loading) View.VISIBLE else View.GONE
                    binding.btnLogin.isEnabled = state !is UiState.Loading
                    when (state) {
                        is UiState.Success -> {
                            val intent = Intent(this@LoginActivity, OtpActivity::class.java).apply {
                                putExtra(OtpActivity.EXTRA_MOBILE, binding.etMobile.text.toString().trim())
                                putExtra(OtpActivity.EXTRA_PASSWORD, state.data)
                            }
                            viewModel.consumed()
                            startActivity(intent)
                        }
                        is UiState.Error -> {
                            Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_LONG).show()
                            viewModel.consumed()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}