package com.example.ecomapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager


import com.example.ecomapp.adapter.CategoryAdapter
import com.example.ecomapp.adapter.ProductAdapter
import com.example.ecomapp.databinding.ActivityMainBinding
import com.example.ecomapp.model.Product
import com.example.ecomapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private val productDetailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val product = result.data?.getSerializableExtra("added_product") as? Product
            if (product != null) {
                viewModel.addToCart(product)
                Toast.makeText(this, "${product.title} added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        categoryAdapter = CategoryAdapter { category ->
            viewModel.fetchProductsByCategory(category)
        }
        productAdapter = ProductAdapter { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            productDetailLauncher.launch(intent)
        }

        viewModel.products.observe(this) { products ->
            productAdapter.submitList(products)
        }

        binding.categoryRecyclerView.adapter = categoryAdapter
        binding.productRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.productRecyclerView.adapter = productAdapter

        viewModel.categories.observe(this) { categories ->
            categoryAdapter.submitList(categories)
        }

        viewModel.products.observe(this) { products ->
            productAdapter.submitList(products)
        }


        viewModel.fetchCategories()
        viewModel.fetchAllProducts()

        binding.cartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}
