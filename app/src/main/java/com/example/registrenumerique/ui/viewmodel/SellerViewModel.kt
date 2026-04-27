package com.example.registrenumerique.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrenumerique.data.model.Seller
import com.example.registrenumerique.data.repository.SellerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SellerViewModel(private val repository: SellerRepository = SellerRepository()) : ViewModel() {

    private val _sellers = MutableStateFlow<List<Seller>>(emptyList())
    val sellers: StateFlow<List<Seller>> = _sellers

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val filteredSellers: StateFlow<List<Seller>> = combine(_sellers, _searchQuery) { sellers, query ->
        if (query.isBlank()) {
            sellers
        } else {
            sellers.filter {
                it.name.contains(query, ignoreCase = true) || 
                it.table_number.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchSellers()
    }

    fun fetchSellers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _sellers.value = repository.getAllSellers()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addSeller(seller: Seller, imageByteArray: ByteArray? = null, fileName: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                var finalSeller = seller
                if (imageByteArray != null && fileName != null) {
                    val imageUrl = repository.uploadImage(imageByteArray, fileName)
                    finalSeller = seller.copy(image_url = imageUrl)
                }
                repository.insertSeller(finalSeller)
                fetchSellers()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSeller(id: Int, seller: Seller, imageByteArray: ByteArray? = null, fileName: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                var finalSeller = seller
                if (imageByteArray != null && fileName != null) {
                    val imageUrl = repository.uploadImage(imageByteArray, fileName)
                    finalSeller = seller.copy(image_url = imageUrl)
                }
                repository.updateSeller(id, finalSeller)
                fetchSellers()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteSeller(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteSeller(id)
                fetchSellers()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
