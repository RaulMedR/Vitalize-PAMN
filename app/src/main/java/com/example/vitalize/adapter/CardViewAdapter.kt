package com.example.vitalize.adapter

import android.app.AlertDialog
import android.net.Uri
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vitalize.Food
import com.example.vitalize.R
import com.example.vitalize.SearchViewModel


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
            if(searchViewModel.searchProduct == "storeroom" || searchViewModel.searchProduct == "home"){
                val builder = AlertDialog.Builder(viewHolder.itemView.context)
                builder.setTitle("Cantidad de producto en gramos")
                var inputString: String = ""

                val input = EditText(viewHolder.itemView.context)
                input.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

                builder.setView(input)
                builder.setPositiveButton("Añadir"
                ) { dialog, which -> run {
                    inputString = input.text.toString()
                    var inputFloat = 0.0f
                    if (inputString != ""){
                        inputFloat = inputString.toFloat()
                    }
                    currentItem.cuantity = inputFloat
                    searchViewModel.selectedProduct = currentItem
                    if(searchViewModel.searchProduct == "storeroom"){
                        Navigation.createNavigateOnClickListener(R.id.action_searchFood_to_userStoreroom).onClick(viewHolder.itemView)
                    } else {
                        Navigation.createNavigateOnClickListener(R.id.action_searchFood_to_homeSession).onClick(viewHolder.itemView)
                    }

                } }
                builder.setNegativeButton("Cancelar"
                ) { dialog, which -> dialog.cancel() }
                builder.show()



            }
        }
        if (currentItem.urlPhoto != null){
            Glide.with(viewHolder.itemView.context).asBitmap().load(currentItem.urlPhoto).into(viewHolder.foodImage)
        }
        viewHolder.foodName.text = currentItem.name?.replaceFirstChar { it.uppercaseChar() }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = foodList.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val foodImage: ImageView = itemView.findViewById(R.id.imagen_producto)
        val foodName: TextView = itemView.findViewById(R.id.nombre_producto)
    }

}
