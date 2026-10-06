package com.sachin.goodsam.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.sachin.goodsam.databinding.ItemUserBinding
import com.sachin.goodsam.domain.model.User

class UserAdapter : ListAdapter<User, UserAdapter.VH>(Diff) {

    inner class VH(private val b: ItemUserBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(user: User) {
            b.tvName.text = user.fullName
            b.tvEmail.text = user.email
            b.tvPhone.text = user.phone
            b.tvCompany.text = user.company
            b.ivUser.load(user.imageUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    object Diff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(a: User, b: User) = a.id == b.id
        override fun areContentsTheSame(a: User, b: User) = a == b
    }
}