package com.example.vitalize.data

import com.example.vitalize.Food
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    suspend fun registerNewUser(userId: String): Resource<DocumentReference>
    suspend fun getDataUser(userId: String, item: String): Resource<String>
    suspend fun foodToArray(): Resource<ArrayList<Food>>
}