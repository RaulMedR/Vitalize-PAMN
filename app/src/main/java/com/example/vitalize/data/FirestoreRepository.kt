package com.example.vitalize.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    suspend fun registerNewUser(userId: String): Resource<DocumentReference>
}