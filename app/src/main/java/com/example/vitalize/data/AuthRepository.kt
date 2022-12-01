package com.example.vitalize.data

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    fun logout()
    suspend fun setNameUser(newName: String): Resource<String>
    suspend fun setPhotoUrl(newPhoto: Uri): Resource<String>

}