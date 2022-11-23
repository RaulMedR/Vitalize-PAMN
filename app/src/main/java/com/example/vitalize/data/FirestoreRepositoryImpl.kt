package com.example.vitalize.data

import com.example.vitalize.data.utils.await
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore): FirestoreRepository {
    override val dataBase: FirebaseFirestore?
        get() = firebaseFirestore

    override suspend fun registerNewUser(userId: String): Resource<DocumentReference> {
        val data = hashMapOf(
            "age" to 0,
            "height" to 0,
            "weight" to 0
        )
        return try{
            val document = dataBase?.collection("users")?.document(userId)!!
            document.set(data).await()
            Resource.Success(document)

        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }

    override suspend fun getDataUser(userId: String, item: String): Resource<String>{
        return try {
            val docRef = dataBase?.collection("users")?.document(userId)!!.get().await()
            val data = docRef.data?.get(item).toString()
            Resource.Success(data + "cm")

        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }

    }


}