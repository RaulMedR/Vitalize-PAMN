package com.example.vitalize.data

import android.util.Log
import android.widget.Toast
import com.example.vitalize.data.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch(e: FirebaseAuthException){
            e.printStackTrace()
            Resource.Failure(e.errorCode)
        }

    }

    override suspend fun signup(name: String, email: String, password: String, ): Resource<FirebaseUser> {
        return try{
            Log.d("viewmodelregister", "he4")
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Resource.Success(result.user!!)
        } catch (e: FirebaseAuthException){
            Log.d("viewmodelregister", "he5")
            e.printStackTrace()
            Resource.Failure(e.errorCode)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}