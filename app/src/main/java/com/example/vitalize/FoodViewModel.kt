package com.example.vitalize

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalize.data.AuthRepository
import com.example.vitalize.data.FirestoreRepository
import com.example.vitalize.data.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) : ViewModel() {

    private val _foodDataSnapshot = MutableLiveData<Resource.Success<Task<DocumentSnapshot>?>>()
    val foodDataSnapshot: LiveData<Resource.Success<Task<DocumentSnapshot>?>> = _foodDataSnapshot

    fun buscarComida(barcodeId: String) = viewModelScope.launch  {
        val result = firestoreRepository.getFood(barcodeId)
        _foodDataSnapshot.value = result
    }


}