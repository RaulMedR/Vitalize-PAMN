package com.example.vitalize.dataClasses

data class Food(var name: String ?= null,
                var kcal: Int ?= null,
                var fats: Float  ?= null,
                var carbohydrates: Float ?= null,
                var proteins: Float ?= null,
                var urlPhoto: String ?= null,
                var cuantity: Float ?= null,
                ) : java.io.Serializable