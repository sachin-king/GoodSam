package com.sachin.goodsam.presentation.list

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin.goodsam.GoodSamApp
import com.sachin.goodsam.databinding.ActivityUserListBinding
import com.sachin.goodsam.util.UiState
import kotlinx.coroutines.launch

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private val adapter = UserAdapter()

    private val viewModel: UserListViewModel by viewModels {
        viewModelFactory {
            initializer {
                UserListViewModel((application as GoodSamApp).container.getUsersUseCase)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progress.visibility =
                        if (state is UiState.Loading) View.VISIBLE else View.GONE
                    when (state) {
                        is UiState.Success -> adapter.submitList(state.data)
                        is UiState.Error ->
                            Toast.makeText(this@UserListActivity, state.message, Toast.LENGTH_LONG).show()
                        else -> Unit
                    }
                }
            }
        }
    }
}