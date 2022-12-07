package com.example.vitalize.adapter

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vitalize.Food
import com.example.vitalize.R
import com.example.vitalize.SearchViewModel
import com.example.vitalize.user.UserViewModel


class CardViewAdapter(private var searchViewModel: SearchViewModel, private val foodList: ArrayList<Food>) :
    RecyclerView.Adapter<CardViewAdapter.ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.food_search_element, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = foodList[position]
        viewHolder.itemView.setOnClickListener {
            if(searchViewModel.searchProduct){
                searchViewModel.selectedProduct = currentItem
                Navigation.createNavigateOnClickListener(R.id.action_searchFood_to_homeSession).onClick(viewHolder.itemView)
            }
        }
        if (currentItem.photo != Uri.EMPTY){
            Glide.with(viewHolder.itemView.context).asBitmap().load(currentItem.photo).into(viewHolder.foodImage)
        }
        viewHolder.foodName.text = currentItem.name.replaceFirstChar { it.uppercaseChar() }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = foodList.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val foodImage: ImageView = itemView.findViewById(R.id.imagen_producto)
        val foodName: TextView = itemView.findViewById(R.id.nombre_producto)
    }

}
