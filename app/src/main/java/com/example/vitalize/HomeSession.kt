package com.example.vitalize

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vitalize.adapter.CardViewAdapter
import com.example.vitalize.adapter.HomeFoodCardAdapter
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentHomeBinding
import com.example.vitalize.user.UserViewModel
import kotlin.system.exitProcess

class HomeSession : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var searchViewModel : SearchViewModel
    private lateinit var dietViewModel: DietViewModel
    private lateinit var breakfastArrayList: ArrayList<Food>
    private lateinit var lunchArrayList: ArrayList<Food>
    private lateinit var dinnerArrayList: ArrayList<Food>
    private lateinit var breakfastRecyclerView: RecyclerView
    private lateinit var lunchRecyclerView: RecyclerView
    private lateinit var dinnerRecyclerView: RecyclerView
    private lateinit var foodAdapter: HomeFoodCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            activity?.finishActivity(0)
            exitProcess(0)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        dietViewModel = ViewModelProvider(requireActivity())[DietViewModel::class.java]
        analizarEscenario()
        establecerClickListeners()

        breakfastRecyclerView = binding.foodsBreakfast
        breakfastRecyclerView.layoutManager = LinearLayoutManager(activity)
        breakfastRecyclerView.setHasFixedSize(true)
        lunchRecyclerView = binding.foodsLunchs
        lunchRecyclerView.layoutManager = LinearLayoutManager(activity)
        lunchRecyclerView.setHasFixedSize(true)
        dinnerRecyclerView = binding.foodsDinners
        dinnerRecyclerView.layoutManager = LinearLayoutManager(activity)
        dinnerRecyclerView.setHasFixedSize(true)
        getFoodArrays()


        //binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun analizarEscenario(){
        if(searchViewModel.searchProduct){
            dietViewModel.addFood(searchViewModel.selectedProduct!!, searchViewModel.selectedType)
            searchViewModel.searchProduct = false
            searchViewModel.selectedProduct = null
        }
    }
    private fun establecerClickListeners() {
        binding.masAmpliarDesayuno.setOnClickListener {
            searchViewModel.selectedType = "breakfast"
            searchViewModel.searchProduct = true
            findNavController().navigate(R.id.searchFood)
        }
        binding.masAmpliarAlmuerzo.setOnClickListener {
            searchViewModel.selectedType = "lunch"
            searchViewModel.searchProduct = true
            findNavController().navigate(R.id.searchFood)
        }
        binding.masAmpliarCena.setOnClickListener {
            searchViewModel.selectedType = "dinner"
            searchViewModel.searchProduct = true
            findNavController().navigate(R.id.searchFood)
        }

    }

    private fun getFoodArrays() {
        dietViewModel.breakfastList.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    Log.d("searchview", it.result.size.toString())
                    breakfastArrayList = it.result
                    foodAdapter = HomeFoodCardAdapter(breakfastArrayList)
                    breakfastRecyclerView.adapter = foodAdapter
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        dietViewModel.lunchList.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    lunchArrayList = it.result
                    foodAdapter = HomeFoodCardAdapter(lunchArrayList)
                    lunchRecyclerView.adapter = foodAdapter
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        dietViewModel.dinnerList.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    dinnerArrayList = it.result
                    foodAdapter = HomeFoodCardAdapter(dinnerArrayList)
                    dinnerRecyclerView.adapter = foodAdapter
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}