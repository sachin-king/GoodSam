package com.sachin.goodsam.presentation.otp

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sachin.goodsam.GoodSamApp
import com.sachin.goodsam.databinding.ActivityOtpBinding
import com.sachin.goodsam.presentation.list.UserListActivity
import com.sachin.goodsam.util.UiState
import kotlinx.coroutines.launch

class OtpActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOBILE = "extra_mobile"
        const val EXTRA_PASSWORD = "extra_password"
    }

    private lateinit var binding: ActivityOtpBinding

    private val viewModel: OtpViewModel by viewModels {
        viewModelFactory {
            initializer {
                val c = (application as GoodSamApp).container
                OtpViewModel(c.verifyOtpUseCase, c.signInUseCase)
            }
        }
    }

    private val boxes by lazy { listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mobile = intent.getStringExtra(EXTRA_MOBILE).orEmpty()
        val password = intent.getStringExtra(EXTRA_PASSWORD).orEmpty()
        viewModel.setup(mobile, password)

        binding.tvSentOn.text = "Sent on +91 $mobile"
        setupOtpBoxes()

        binding.btnLogin.setOnClickListener {
            val otp = boxes.joinToString("") { it.text.toString() }
            if (otp.length != 4) {
                Toast.makeText(this, "Enter the 4-digit OTP", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.verify(otp)
            }
        }
        binding.tvResend.setOnClickListener { viewModel.resend() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progress.visibility =
                        if (state is UiState.Loading) View.VISIBLE else View.GONE
                    binding.btnLogin.isEnabled = state !is UiState.Loading
                    when (state) {
                        is UiState.Success -> {
                            startActivity(
                                Intent(this@OtpActivity, UserListActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            )
                        }
                        is UiState.Error -> {
                            Toast.makeText(this@OtpActivity, state.message, Toast.LENGTH_LONG).show()
                            viewModel.consumed()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setupOtpBoxes() {
        boxes.forEachIndexed { i, et ->
            et.doOnTextChanged { text, _, _, _ ->
                if (text?.length == 1 && i < boxes.lastIndex) boxes[i + 1].requestFocus()
            }
            et.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN
                    && et.text.isEmpty() && i > 0
                ) {
                    boxes[i - 1].setText("")
                    boxes[i - 1].requestFocus()
                    true
                } else false
            }
        }
    }
}