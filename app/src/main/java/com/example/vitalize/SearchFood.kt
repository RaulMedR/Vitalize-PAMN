package com.example.vitalize

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vitalize.adapter.CardViewAdapter
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentSearchFoodBinding
import com.example.vitalize.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backArrow.setOnClickListener { goBack() }
        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        foodRecyclerView = binding.foodsSearchRecycler
        foodRecyclerView.layoutManager = LinearLayoutManager(activity)
        foodRecyclerView.setHasFixedSize(true)



        getFoodArray()

    }

    private fun getFoodArray(){
        searchViewModel.allProducts.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    Log.d("searchview", it.result.size.toString())
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

}