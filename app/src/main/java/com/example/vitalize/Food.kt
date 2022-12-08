package com.example.vitalize

import android.net.Uri

data class Food(val name: String, val photo: Uri, val kcal: Int, val fats: Float, val carbohydrates: Float, val proteins: Float, var cuantity: Float)