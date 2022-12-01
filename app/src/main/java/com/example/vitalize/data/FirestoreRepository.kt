package com.example.vitalize.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    suspend fun registerNewUser(userId: String): Resource<DocumentReference>
    suspend fun getDataUser(userId: String, item: String): Resource<String>
    suspend fun setDataUser(userId: String, item: String, newValue: String): Resource<String>
}