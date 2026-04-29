package com.example.registrenumerique.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrenumerique.data.model.Seller
import com.example.registrenumerique.data.repository.SellerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SellerViewModel(private val repository: SellerRepository = SellerRepository()) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategory = MutableStateFlow("Tous")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    private val _saveSuccess = MutableSharedFlow<Boolean>()
    val saveSuccess: SharedFlow<Boolean> = _saveSuccess.asSharedFlow()

    val sellers: StateFlow<List<Seller>> = repository.getSellersFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredSellers: StateFlow<List<Seller>> = combine(sellers, _searchQuery, _selectedCategory) { sellers, query, category ->
        sellers.filter { seller ->
            val matchesQuery = query.isBlank() || 
                seller.name.contains(query, ignoreCase = true) || 
                seller.table_number.contains(query, ignoreCase = true)
            
            val matchesCategory = category == "Tous" || seller.category == category
            
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
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
                _saveSuccess.emit(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.emit(e.localizedMessage ?: "Erreur lors de l'ajout")
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
                _saveSuccess.emit(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.emit(e.localizedMessage ?: "Erreur lors de la modification")
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
                _saveSuccess.emit(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.emit(e.localizedMessage ?: "Erreur lors de la suppression")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
