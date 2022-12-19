package com.example.vitalize.viewModels

import android.util.Log
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
    private val _dailykcal = MutableLiveData<Int>()
    val dailykcal: LiveData<Int> = _dailykcal
    private val _cantidadObjetivo = MutableLiveData<String>()
    val cantidadObjetivo: LiveData<String> = _cantidadObjetivo


    init {
        inicializacionDatos()
    }

     fun inicializacionDatos() = viewModelScope.launch{
        val calendar = Calendar.getInstance()
        firestoreRepository.getDailyDietDate(userId).let {
            when(it){
                is Resource.Success -> {
                    if( calendar.get(Calendar.YEAR) > it.result.get(Calendar.YEAR) ||
                        calendar.get(Calendar.MONTH) > it.result.get(Calendar.MONTH) ||
                        calendar.get(Calendar.DAY_OF_MONTH) > it.result.get(Calendar.DAY_OF_MONTH)){
                        firestoreRepository.resetDailyDiet(userId, calendar)

                    }
                }
                else -> {}
            }
        }
        _breakfastList.value = firestoreRepository.dailyDiet("breakfast", userId)
        _lunchList.value = firestoreRepository.dailyDiet("lunch", userId)
        _dinnerList.value = firestoreRepository.dailyDiet("dinner", userId)

        _cantidadObjetivo.value = firestoreRepository.getKcalObjective(userId)

        actualizarCalorias()


    }

    private fun actualizarCalorias() = viewModelScope.launch{
        _dailykcal.value = 0
        contarCalorias(_breakfastList.value as Resource<ArrayList<Food>>)
        contarCalorias(_lunchList.value as Resource<ArrayList<Food>>)
        contarCalorias(_dinnerList.value as Resource<ArrayList<Food>>)
    }

    private fun contarCalorias(foodList: Resource<ArrayList<Food>>) {

        foodList.let {
            when(it){
                is Resource.Success -> {
                    for(food in it.result){
                        _dailykcal.value = _dailykcal.value?.plus(((food.cuantity!! / 100) * food.kcal!!).toInt())
                    }

                }
                else -> {}
            }
        }
    }

    fun addFood(food: Food, type: String) = viewModelScope.launch{
        if(food.cuantity != 0.0f){
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
            actualizarCalorias()
        }
    }
    fun removeFood(food: Food, type: String) = viewModelScope.launch{
        when(type){
            "breakfast"-> {
                _breakfastList.value.let {
                    when(it){
                        is Resource.Success -> {
                            it.result.remove(food)
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
                            it.result.remove(food)
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
                            it.result.remove(food)
                            firestoreRepository.updateDailyDiet(userId, type, it.result)
                        }
                        else -> {}
                    }
                }
            }
        }
        actualizarCalorias()
    }

    fun updateFoodCuantity(food: Food, cuantity: Float, type: String) = viewModelScope.launch{
        when(type){
            "breakfast"-> {
                _breakfastList.value.let {
                    when(it){
                        is Resource.Success -> {
                            food.cuantity = cuantity
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
                            food.cuantity = cuantity
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
                            food.cuantity = cuantity
                            firestoreRepository.updateDailyDiet(userId, type, it.result)
                        }
                        else -> {}
                    }
                }
            }
        }
        actualizarCalorias()

    }

     fun updateCantidadObjetivo(cantidad: Int)= viewModelScope.launch {
        firestoreRepository.setKcalObjective(userId, cantidad)
        _cantidadObjetivo.value = cantidad.toString()
    }

    fun genDiet(storeRoomList: ArrayList<Food>, vararg listas: ArrayList<Food>){
        var foodEliminate: Food? = null
        if(storeRoomList.isEmpty()){
            return
        }
        val kcalGuide = (cantidadObjetivo.value!!.toInt() - dailykcal.value!!.toInt())/listas.size
        for(lista in listas){
            var kcalDiet = kcalGuide
            while(kcalDiet > 0){
                foodEliminate = null
                var grams: Float
                var countFoodNumber = storeRoomList.size
                for (food in storeRoomList){
                    grams = food.cuantity!!
                    while(grams/100 * food.kcal!! >= kcalDiet && grams > 0.0f){
                        if(grams < 25){
                            grams = 0.0f
                        }
                        grams -= 25
                    }
                    if(grams > 0.0f) {
                        val foodState = food.copy()
                        if(food.cuantity == grams){
                            foodEliminate = food
                        } else {
                            foodState.cuantity = grams
                        }
                        lista.add(foodState)
                        food.cuantity = food.cuantity!! - grams
                        kcalDiet -= (grams/100 * food.kcal!!).toInt()

                    }
                    countFoodNumber -= 1
                    if(countFoodNumber == 0){
                        kcalDiet = 0
                    }
                }
                if(foodEliminate != null){
                    storeRoomList.remove(foodEliminate)
                    if(storeRoomList.isEmpty()){
                        return
                    }

                }

            }
        }
    }

    fun updateDailyDiet(type: String, lista: ArrayList<Food>) = viewModelScope.launch{
        firestoreRepository.updateDailyDiet(userId, type, lista)

    }
}


