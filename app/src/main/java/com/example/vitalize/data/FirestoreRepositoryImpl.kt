package com.example.vitalize.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.vitalize.data.utils.await
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore): FirestoreRepository {
    override val dataBase: FirebaseFirestore?
        get() = firebaseFirestore


    override suspend fun registerNewUser(userId: String): Resource<DocumentReference> {
        val data = hashMapOf(
            "age" to 0,
            "height" to 0,
            "weight" to 0,
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
            if(item == "weight"){
                val data = docRef.data?.get(item).toString()
                Resource.Success(data + " kg")
            }
            else {
                val data = docRef.data?.get(item).toString()
                Resource.Success(data + " cm")
            }
        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun setDataUser(userId: String, item: String, newValue: String): Resource<String>{
        return try {
            if(item == "weight" ){
                val docRef = dataBase?.collection("users")?.document(userId)!!.update(item, newValue.toDouble())
                Resource.Success(newValue + " kg")
            }
            else {
                val docRef = dataBase?.collection("users")?.document(userId)!!.update(item, newValue.toInt())
                Resource.Success(newValue + " cm")
            }
        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }

    }

    override suspend fun getFood(barcodeId: String): Resource.Success<Task<DocumentSnapshot>?> {
        val docRef = dataBase?.collection("foods")?.document(barcodeId)?.get()
        docRef?.await()
        return Resource.Success(docRef)
    }

}