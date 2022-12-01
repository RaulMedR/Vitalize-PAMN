package com.example.vitalize.data

import com.example.vitalize.data.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firestoreRepository: FirestoreRepository) : AuthRepository {
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
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            firestoreRepository.registerNewUser(result.user!!.uid)
            Resource.Success(result.user!!)

        } catch (e: FirebaseAuthException){
            e.printStackTrace()
            Resource.Failure(e.errorCode)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun setNameUser(newName: String): Resource<String>{
        return try{
            currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(newName).build())
                ?.await()
            Resource.Success(newName)

        } catch (e: FirebaseAuthException){
            e.printStackTrace()
            Resource.Failure(e.errorCode)
        }

    }
}