package com.example.vitalize.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vitalize.R
import com.example.vitalize.adapter.CardViewAdapter
import com.example.vitalize.dataClasses.Food
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentSearchFoodBinding
import com.example.vitalize.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchFood : Fragment() {

    private lateinit var searchViewModel : SearchViewModel
    private lateinit var binding: FragmentSearchFoodBinding
    private lateinit var foodArrayList: ArrayList<Food>
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodAdapter: CardViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
                goBack()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_food, container, false)
        // below line is to call set on query text listener method.
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backArrow.setOnClickListener { goBack() }
        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        foodRecyclerView = binding.foodsSearchRecycler
        foodRecyclerView.layoutManager = LinearLayoutManager(activity)
        foodRecyclerView.setHasFixedSize(true)

        binding.searchView.setOnClickListener({
            binding.searchView.setIconified(false)
        })

        getFoodArray()

    }

    private fun getFoodArray(){
        searchViewModel.allProducts.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    foodArrayList = it.result
                    foodAdapter = CardViewAdapter(searchViewModel, foodArrayList)
                    foodRecyclerView.adapter = foodAdapter
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goBack() {
        findNavController().navigate(R.id.action_searchFood_to_homeSession)
    }

    private fun filter(text: String) {
        val filteredlist = ArrayList<Food>()
        for (item in foodArrayList) {
            if (item.name?.lowercase()?.contains(text.lowercase(Locale.getDefault())) == true) {
                filteredlist.add(item)
            }
        }
        foodAdapter = CardViewAdapter(searchViewModel, filteredlist)
        foodRecyclerView.adapter = foodAdapter
        if (filteredlist.isEmpty()) {
            binding.noEncontradoProducto.visibility = View.VISIBLE
        }
        else{
            binding.noEncontradoProducto.visibility = View.INVISIBLE
        }
    }

}

