package com.example.vitalize

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalize.data.FirestoreRepository
import com.example.vitalize.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    private val _allProducts = MutableLiveData<Resource<ArrayList<Food>>>()
    val allProducts: LiveData<Resource<ArrayList<Food>>> = _allProducts
    var selectedProduct: Food? = null
    var selectedType: String = ""
    var searchProduct: String = ""

    init {
        getAllProducts()


    }

    private fun getAllProducts() = viewModelScope.launch{
        val result = firestoreRepository.foodToArray()
        _allProducts.value = result
    }




}
