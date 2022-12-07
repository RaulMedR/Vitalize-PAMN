package com.example.vitalize.data

import androidx.core.net.toUri
import com.example.vitalize.Food
import com.example.vitalize.data.utils.await
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
            if(item == "weight"){
                Resource.Success(data + "kg")
            } else {
                Resource.Success(data + "cm")
            }
        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }

    }



    override suspend fun foodToArray(): Resource<ArrayList<Food>> {
        return try{
            val result = dataBase?.collection("foods")?.get()!!.await()
            var returnResult: ArrayList<Food> = arrayListOf<Food>()
            for(doc in result){
                returnResult.add(Food(name = doc.data["name"].toString(), carbohydrates =  doc.data["carbohydrates"].toString().toFloat(),
                    kcal = doc.data["kcal"].toString().toInt(), proteins = doc.data["proteins"].toString().toFloat(),
                    fats = doc.data["fats"].toString().toFloat(), photo = doc.data["photoURL"].toString().toUri(), cuantity = 0.0f))
            }
            Resource.Success(returnResult)

        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }

    override suspend fun dailyDiet(type: String, uid: String): Resource<ArrayList<Food>> {
        return try{
            var result: ArrayList<Food> = ArrayList()
            val query = dataBase?.collection("dailydiet")?.document(uid)?.get()?.await()
            val data = query?.data?.get(type) as? Map<*, *>
            if(data != null){
                for(key in data.keys){
                    val food = dataBase?.collection("foods")?.document(key.toString())?.get()?.await()
                    result.add(Food(name = food?.data?.get("name").toString(), carbohydrates = food?.data?.get("carbohydrates").toString().toFloat(),
                        kcal = food?.data?.get("kcal").toString().toInt(), proteins = food?.data?.get("proteins").toString().toFloat(), fats = food?.data?.get("fats").toString().toFloat(),
                        photo = food?.data?.get("photoURL").toString().toUri(), cuantity = data[key].toString().toFloat()))
                }
            }

            Resource.Success(result)
        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }


}