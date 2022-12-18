package com.example.vitalize.adapter

import android.app.AlertDialog
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
import com.example.vitalize.dataClasses.Food
import com.example.vitalize.R
import com.example.vitalize.viewModels.StoreroomViewModel

class StoreroomFoodCardAdapter(private val foodList: ArrayList<Food>, private val storeroomViewModel: StoreroomViewModel):
    RecyclerView.Adapter<StoreroomFoodCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StoreroomFoodCardAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.food_storeroom_element, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = foodList[position]

        viewHolder.foodGrams.text = currentItem.cuantity.toString() + " gramos"
        if (currentItem.urlPhoto != "null"){
            Glide.with(viewHolder.itemView.context).asBitmap().load(currentItem.urlPhoto).into(viewHolder.foodImage)
        }
        viewHolder.foodName.text = currentItem.name!!.replaceFirstChar { it.uppercaseChar() }
        viewHolder.plutButton.setOnClickListener {
            val builder = AlertDialog.Builder(viewHolder.itemView.context)
            builder.setTitle("Modificar/Eliminar producto")
            var inputString: String = ""
            val input = EditText(viewHolder.itemView.context)
            input.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            input.setText(currentItem.cuantity.toString())
            builder.setView(input)
            builder.setPositiveButton("Modificar"
            ) { dialog, which -> run {
                inputString = input.text.toString()
                var inputFloat = currentItem.cuantity!!
                if (inputString != ""){
                    inputFloat = inputString.toFloat()
                }
                if(inputFloat == 0.0f){
                    storeroomViewModel.removeFood(currentItem)
                } else {
                    storeroomViewModel.updateFoodCuantity(currentItem, inputFloat)
                    currentItem.cuantity = inputFloat
                }
                Navigation.createNavigateOnClickListener(R.id.userStoreroom).onClick(viewHolder.itemView)


            } }
            builder.setNegativeButton("Eliminar"
            ) { dialog, which -> run {
                storeroomViewModel.removeFood(currentItem)
                Navigation.createNavigateOnClickListener(R.id.userStoreroom).onClick(viewHolder.itemView)
            } }
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val foodImage: ImageView = itemView.findViewById(R.id.imagen_producto)
        val foodName: TextView = itemView.findViewById(R.id.nombre_producto)
        val foodGrams: TextView = itemView.findViewById(R.id.peso_producto)
        val plutButton: TextView = itemView.findViewById(R.id.mas_ampliar_storeroom)
    }


}