package com.example.ecomapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.R
import com.example.ecomapp.adapter.CartItemAdapter
import com.example.ecomapp.databinding.ActivityCartBinding
import com.example.ecomapp.model.Product
import com.example.ecomapp.viewmodel.MainViewModel

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: CartItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CartItemAdapter(emptyList(),
            onAddClick = { viewModel.addToCart(it.product) },
            onRemoveClick = { viewModel.removeFromCart(it.product) })

        binding.recyclerCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerCart.adapter = adapter

        viewModel.cartItems.observe(this) { list ->
            Log.d("CartActivity", "Cart size: ${list.size}")
            adapter.updateList(list)
            binding.tvTotalPrice.text = "Total: ₹${viewModel.getTotalPrice()}"
        }

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(this, "Checkout clicked", Toast.LENGTH_SHORT).show()
        }

    }

}
