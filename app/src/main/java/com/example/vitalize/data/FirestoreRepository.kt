package com.example.vitalize.data

import com.example.vitalize.Food
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    suspend fun registerNewUser(userId: String): Resource<DocumentReference>
    suspend fun getDataUser(userId: String, item: String): Resource<String>
    suspend fun foodToArray(): Resource<ArrayList<Food>>
    suspend fun dailyDiet(type: String, uid: String): Resource<ArrayList<Food>>
    suspend fun getDailyDietDate(uid: String): Resource<Calendar>
    suspend fun resetDailyDiet(uid: String, date: Calendar)
    suspend fun updateDailyDiet(uid: String, type: String, foodList: ArrayList<Food>)
}