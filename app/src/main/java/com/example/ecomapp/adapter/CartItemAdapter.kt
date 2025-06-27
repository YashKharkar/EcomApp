package com.example.ecomapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomapp.databinding.ItemCartBinding
import com.example.ecomapp.model.CartItem
class CartItemAdapter(
    private var cartItems: List<CartItem>,
    private val onAddClick: (CartItem) -> Unit,
    private val onRemoveClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.tvTitle.text = item.product.title
            binding.tvPrice.text = "₹${item.product.price}"
            binding.tvQuantity.text = item.quantity.toString()



            Glide.with(binding.ivProductImage.context)
                .load(item.product.image)
                .into(binding.ivProductImage)

            binding.btnAdd.setOnClickListener { onAddClick(item) }
            binding.btnRemove.setOnClickListener { onRemoveClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount() = cartItems.size

    fun updateList(newList: List<CartItem>) {
        cartItems = newList.toList()
        notifyDataSetChanged()
    }
}
