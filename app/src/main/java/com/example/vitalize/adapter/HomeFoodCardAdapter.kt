package com.example.vitalize.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vitalize.Food
import com.example.vitalize.R

class HomeFoodCardAdapter(private val foodList: ArrayList<Food>) :
    RecyclerView.Adapter<HomeFoodCardAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomeFoodCardAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.food_element, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: HomeFoodCardAdapter.ViewHolder, position: Int) {
        val currentItem = foodList[position]

        viewHolder.foodGrams.text = currentItem.cuantity.toString() + " gramos"
        if (currentItem.photo != Uri.EMPTY){
            Glide.with(viewHolder.itemView.context).asBitmap().load(currentItem.photo).into(viewHolder.foodImage)
        }
        viewHolder.foodName.text = currentItem.name.replaceFirstChar { it.uppercaseChar() }



    }

    override fun getItemCount() = foodList.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val foodImage: ImageView = itemView.findViewById(R.id.imagen_producto)
        val foodName: TextView = itemView.findViewById(R.id.nombre_producto)
        val foodGrams: TextView = itemView.findViewById(R.id.peso_producto)
    }
}