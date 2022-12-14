package com.example.vitalize.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vitalize.dataClasses.Food
import com.example.vitalize.data.utils.await
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
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
            "weight" to 0,
            "kcal_objective" to 2000
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



    override suspend fun foodToArray(): Resource<ArrayList<Food>> {
        return try{
            val result = dataBase?.collection("foods")?.get()!!.await()
            val returnResult: ArrayList<Food> = arrayListOf<Food>()
            for(doc in result){
                returnResult.add(
                    Food(name = doc.data["name"].toString(), carbohydrates =  doc.data["carbohydrates"].toString().toFloat(),
                    kcal = doc.data["kcal"].toString().toInt(), proteins = doc.data["proteins"].toString().toFloat(),
                    fats = doc.data["fats"].toString().toFloat(), urlPhoto = doc.data["urlPhoto"].toString(), cuantity = 0.0f)
                )
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
            val data = query?.data?.get(type) as? ArrayList<*>

            if(data != null) {
                for(food in data){
                    val foodMap = food as HashMap<*, *>
                    result.add(
                        Food(carbohydrates = foodMap["carbohydrates"].toString().toFloat(), kcal = foodMap["kcal"].toString().toInt(),
                        cuantity = foodMap["cuantity"].toString().toFloat(), fats = foodMap["fats"].toString().toFloat(),
                        proteins = foodMap["proteins"].toString().toFloat(), urlPhoto = foodMap["urlPhoto"].toString(),
                        name = foodMap["name"].toString())
                    )
                }
            }

            Resource.Success(result)
        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }

    override suspend fun storeroomList(uid: String): Resource<ArrayList<Food>> {
        return try{
            val result: ArrayList<Food> = ArrayList()
            val query = dataBase?.collection("storeroom")?.document(uid)?.get()?.await()
            val data = query?.data?.get("storeroomList") as? ArrayList<*>

            if(data != null) {
                for(food in data){
                    val foodMap = food as HashMap<*, *>
                    result.add(
                        Food(carbohydrates = foodMap["carbohydrates"].toString().toFloat(), kcal = foodMap["kcal"].toString().toInt(),
                        cuantity = foodMap["cuantity"].toString().toFloat(), fats = foodMap["fats"].toString().toFloat(),
                        proteins = foodMap["proteins"].toString().toFloat(), urlPhoto = foodMap["urlPhoto"].toString(),
                        name = foodMap["name"].toString())
                    )
                }
            }

            Resource.Success(result)
        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }

    override suspend fun updateStoreroom(uid: String, foodList: ArrayList<Food>){
        dataBase?.collection("storeroom")?.document(uid)?.set(hashMapOf("storeroomList" to foodList))

    }

    override suspend fun getKcalObjective(uid: String): String {
        val query = dataBase?.collection("users")?.document(uid)?.get()?.await()
        return query?.data?.get("kcal_objective").toString()

    }

    override suspend fun setKcalObjective(userId: String, cantidad: Int): Resource<String> {
        return try {
            val docRef = dataBase?.collection("users")?.document(userId)!!.update("kcal_objective", cantidad)
            Resource.Success(cantidad.toString() + " kcal")
        } catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            Resource.Failure(e.message!!)
        }
    }

    override suspend fun getDailyDietDate(uid: String): Resource<Calendar>{
        return try{
            val result = Calendar.getInstance()
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





    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun setDataUser(userId: String, item: String, newValue: String): Resource<String>{
        return try {
            if(item == "weight" ){
                val docRef = dataBase?.collection("users")?.document(userId)!!.update(item, newValue.toDouble())
                Resource.Success(newValue + " kg")
            }
            else {
                val docRef = dataBase?.collection("users")?.document(userId)!!.update(item, newValue.toDouble())
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