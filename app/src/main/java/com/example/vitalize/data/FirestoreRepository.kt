package com.example.vitalize.data

import com.google.android.gms.tasks.Task
import com.example.vitalize.Food
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    suspend fun registerNewUser(userId: String): Resource<DocumentReference>
    suspend fun getDataUser(userId: String, item: String): Resource<String>
    suspend fun setDataUser(userId: String, item: String, newValue: String): Resource<String>
    suspend fun getFood(barcodeId: String): Resource.Success<Task<DocumentSnapshot>?>
    suspend fun foodToArray(): Resource<ArrayList<Food>>
    suspend fun dailyDiet(type: String, uid: String): Resource<ArrayList<Food>>
    suspend fun getDailyDietDate(uid: String): Resource<Calendar>
    suspend fun resetDailyDiet(uid: String, date: Calendar)
    suspend fun updateDailyDiet(uid: String, type: String, foodList: ArrayList<Food>)
    suspend fun storeroomList(uid: String): Resource<ArrayList<Food>>
    suspend fun updateStoreroom(uid: String, foodList: ArrayList<Food>)
    suspend fun getKcalObjective(uid: String): String
}