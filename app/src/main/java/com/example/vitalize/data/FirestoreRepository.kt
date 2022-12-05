package com.example.vitalize.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    suspend fun registerNewUser(userId: String): Resource<DocumentReference>
    suspend fun getDataUser(userId: String, item: String): Resource<String>
    suspend fun setDataUser(userId: String, item: String, newValue: String): Resource<String>
    suspend fun getFood(barcodeId: String): Resource.Success<Task<DocumentSnapshot>?>
}