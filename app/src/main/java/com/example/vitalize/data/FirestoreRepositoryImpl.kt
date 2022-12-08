package com.example.vitalize.data

import android.util.Log
import androidx.core.net.toUri
import com.example.vitalize.Food
import com.example.vitalize.data.utils.await
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.Calendar
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
                    fats = doc.data["fats"].toString().toFloat(), photo = doc.data["urlPhoto"].toString().toUri(), cuantity = 0.0f))
            }
            Resource.Success(returnResult)

        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }

    override suspend fun dailyDiet(type: String, uid: String): Resource<ArrayList<Food>> {
        return try{
            val result: ArrayList<Food> = ArrayList()
            val query = dataBase?.collection("dailydiet")?.document(uid)?.get()?.await()
            val data = query?.data?.get(type) as ArrayList<*>
            Log.d("dailydiet", data.toString())

            for(food in data){
                val foodMap = food as HashMap<*, *>
                Log.d("dailydiet", food.toString())
                result.add(Food(carbohydrates = foodMap["carbohydrates"].toString().toFloat(), kcal = foodMap["kcal"].toString().toInt(),
                    cuantity = foodMap["cuantity"].toString().toFloat(), fats = foodMap["fats"].toString().toFloat(),
                    proteins = foodMap["proteins"].toString().toFloat(), photo = foodMap["photo"].toString().toUri(),
                    name = foodMap["name"].toString()))
            }

            Resource.Success(result)
        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }

    override suspend fun getDailyDietDate(uid: String): Resource<Calendar>{
        return try{
            var result = Calendar.getInstance()
            result.set(1900, 1, 1)
            val query = dataBase?.collection("dailydiet")?.document(uid)?.get()?.await()
            val date = query?.data?.get("date") as? HashMap<*, *>
            if(date != null) {
                result.set(date["year"].toString().toInt(), date["month"].toString().toInt(), date["day"].toString().toInt())
            }
            Resource.Success(result)



        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }

    override suspend fun resetDailyDiet(uid: String, date: Calendar){
        val query = dataBase?.collection("dailydiet")?.document(uid)?.set(hashMapOf(
            "date" to hashMapOf(
                "year" to date.get(Calendar.YEAR),
                "month" to date.get(Calendar.MONTH),
                "day" to date.get(Calendar.DAY_OF_MONTH)
            )
        ))
    }

    override suspend fun updateDailyDiet(uid: String, type: String, foodList: ArrayList<Food>){
        val query = dataBase?.collection("dailydiet")?.document(uid)?.update(type, foodList)

    }






}