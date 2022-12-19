package com.example.vitalize.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalize.dataClasses.Food
import com.example.vitalize.data.AuthRepository
import com.example.vitalize.data.FirestoreRepository
import com.example.vitalize.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreroomViewModel @Inject constructor(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {

    private val _storeroomList = MutableLiveData<Resource<ArrayList<Food>>>()
    val storeroomList: LiveData<Resource<ArrayList<Food>>> = _storeroomList
    private val _userid = authRepository.currentUser!!.uid
    val userId = _userid


    init {
        inicializacionDatos()
    }

    private fun inicializacionDatos() = viewModelScope.launch {
        _storeroomList.value = firestoreRepository.storeroomList(_userid)
    }

    fun removeFood(food: Food) = viewModelScope.launch{
        _storeroomList.value.let {
            when(it){
                is Resource.Success -> {
                    it.result.remove(food)
                    firestoreRepository.updateStoreroom(userId, it.result)
                }

                else -> {}
            }
        }
    }


    fun updateFoodCuantity(food: Food, cuantity: Float) = viewModelScope.launch{
        _storeroomList.value.let {
            when(it){
                is Resource.Success -> {
                    food.cuantity = cuantity
                    firestoreRepository.updateStoreroom(userId, it.result)
                }

                else -> {}
            }
        }
    }
    fun addFood(food: Food) = viewModelScope.launch{
        if(food.cuantity != 0.0f){
            _storeroomList.value.let {
                when(it){
                    is Resource.Success -> {
                        it.result.add(food)
                        firestoreRepository.updateStoreroom(userId, it.result)
                    }

                    else -> {}
                }
            }
        }
    }
    fun getStoreRoomList():ArrayList<Food>{
        var result = ArrayList<Food>()
        storeroomList.value.let {
            when(it){
                is Resource.Success -> {
                    return it.result
                }
                else -> {
                    return result
                }
            }
        }
    }

    fun updateStoreroomAfterGen() {
        val removeList = ArrayList<Food>()
        _storeroomList.value.let {
            when (it) {
                is Resource.Success -> {
                    for (foodStore in it.result) {
                        if (foodStore.cuantity!! == 0.0f) {
                            removeList.add(foodStore)
                        }
                    }
                }
                else -> {}
            }
        }
        for(food in removeList){
            removeFood(food)
        }
    }

}
