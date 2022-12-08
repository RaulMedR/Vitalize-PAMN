package com.example.vitalize

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalize.data.AuthRepository
import com.example.vitalize.data.FirestoreRepository
import com.example.vitalize.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {


    private val _breakfastList = MutableLiveData<Resource<ArrayList<Food>>>()
    val breakfastList: LiveData<Resource<ArrayList<Food>>> = _breakfastList
    private val _lunchList = MutableLiveData<Resource<ArrayList<Food>>>()
    val lunchList: LiveData<Resource<ArrayList<Food>>> = _lunchList
    private val _dinnerList = MutableLiveData<Resource<ArrayList<Food>>>()
    val dinnerList: LiveData<Resource<ArrayList<Food>>> = _dinnerList
    private val _userid = authRepository.currentUser!!.uid
    val userId = _userid


    init {
        inicializacionDatos()
    }

    private fun inicializacionDatos() = viewModelScope.launch{
        val calendar = Calendar.getInstance()
        firestoreRepository.getDailyDietDate(userId).let {
            when(it){
                is Resource.Success -> {
                    if( calendar > it.result){
                        firestoreRepository.resetDailyDiet(userId, calendar)
                        _breakfastList.value = firestoreRepository.dailyDiet("breakfast", userId)
                        _lunchList.value = firestoreRepository.dailyDiet("lunch", userId)
                        _dinnerList.value = firestoreRepository.dailyDiet("dinner", userId)
                    }
                    else {
                        _breakfastList.value = firestoreRepository.dailyDiet("breakfast", userId)
                        _lunchList.value = firestoreRepository.dailyDiet("lunch", userId)
                        _dinnerList.value = firestoreRepository.dailyDiet("dinner", userId)
                    }
                }
                else -> {}
            }
        }

    }

    fun addFood(food: Food, type: String) = viewModelScope.launch{
        when(type){
            "breakfast"-> {
                _breakfastList.value.let {
                    when(it){
                        is Resource.Success -> {
                            it.result.add(food)
                            firestoreRepository.updateDailyDiet(userId, type, it.result)
                        }

                        else -> {}
                    }
                }
            }
            "lunch"->{
                _lunchList.value.let {
                    when(it){
                        is Resource.Success -> {
                            it.result.add(food)
                            firestoreRepository.updateDailyDiet(userId, type, it.result)
                        }

                        else -> {}
                    }
                }

            }
            "dinner"->{
                _dinnerList.value.let {
                    when(it){
                        is Resource.Success -> {
                            it.result.add(food)
                            firestoreRepository.updateDailyDiet(userId, type, it.result)
                        }

                        else -> {}
                    }
                }

            }

        }

    }


}