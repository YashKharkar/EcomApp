package com.example.ecomapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.model.CartItem
import com.example.ecomapp.model.Product
import com.example.ecomapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
object CartRepository {
    val cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
}

class MainViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    val cartItems: LiveData<MutableList<CartItem>> = CartRepository.cartItems

    fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = RetrofitInstance.api.getCategories()
        }
    }
    fun fetchAllProducts() {
        viewModelScope.launch {
            _products.value = RetrofitInstance.api.getProducts()
        }
    }
    fun fetchProductsByCategory(category: String)
    {
        viewModelScope.launch {
            _products.value = RetrofitInstance.api.getProductsByCategory(category)
        }
    }fun addToCart(product: Product) {
        val currentList = CartRepository.cartItems.value ?: mutableListOf()
        val itemIndex = currentList.indexOfFirst { it.product.id == product.id }

        if (itemIndex >= 0) {
            currentList[itemIndex].quantity++
        } else {

            currentList.add(CartItem(product, 1))
        }
        CartRepository.cartItems.value = currentList.toMutableList()

        Log.d("ViewModel", "Added product: ${product.title}, cart size now: ${CartRepository.cartItems.value?.size}")
    }
    fun removeFromCart(product: Product) {
        val list = CartRepository.cartItems.value ?: mutableListOf()
        val item = list.find { it.product.id == product.id }
        if (item != null) {
            item.quantity--
            if (item.quantity <= 0) list.remove(item)
        }
        CartRepository.cartItems.value = list.toMutableList()

        Log.d("ViewModel", "Removed product: ${product.title}, cart size now: ${CartRepository.cartItems.value?.size}")
    }
    fun getTotalPrice(): String {
        val list = CartRepository.cartItems.value ?: listOf()
        val total = list.sumOf { it.product.price * it.quantity }
        return "%.2f".format(total)
    }
}