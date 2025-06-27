package com.example.ecomapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ecomapp.R
import com.example.ecomapp.databinding.ActivityProductDetailBinding
import com.example.ecomapp.model.Product

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("product") as? Product
        if (product == null) {
            Toast.makeText(this, "Product data not available", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.tvTitle.text = product.title
        binding.tvPrice.text = "₹${product.price}"
        binding.tvDescription.text = product.description
        Glide.with(this).load(product.image).into(binding.ivProduct)

        binding.btnAddToCart.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("added_product", product)
            setResult(Activity.RESULT_OK, resultIntent)
            Toast.makeText(this, "${product.title} added to cart", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
