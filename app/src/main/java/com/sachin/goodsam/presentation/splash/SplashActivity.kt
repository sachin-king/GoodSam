package com.sachin.goodsam.presentation.splash

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
import com.sachin.goodsam.databinding.ActivitySplashBinding
import com.sachin.goodsam.presentation.login.LoginActivity
import com.sachin.goodsam.util.UiState
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val viewModel: SplashViewModel by viewModels {
        viewModelFactory {
            initializer {
                SplashViewModel((application as GoodSamApp).container.registerDeviceUseCase)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRetry.setOnClickListener { viewModel.start() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progress.visibility =
                        if (state is UiState.Loading) View.VISIBLE else View.GONE
                    binding.btnRetry.visibility =
                        if (state is UiState.Error) View.VISIBLE else View.GONE
                    when (state) {
                        is UiState.Success -> {
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                            finish()
                        }
                        is UiState.Error ->
                            Toast.makeText(this@SplashActivity, state.message, Toast.LENGTH_LONG).show()
                        else -> Unit
                    }
                }
            }
        }

        if (savedInstanceState == null) viewModel.start()
    }
}