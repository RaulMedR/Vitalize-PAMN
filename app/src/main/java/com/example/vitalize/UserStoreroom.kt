package com.example.vitalize

import android.os.Bundle
import android.util.Log
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
import com.example.vitalize.adapter.StoreroomFoodCardAdapter
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentUserStoreroomBinding
import java.util.*

class UserStoreroom : Fragment() {
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentUserStoreroomBinding
    private lateinit var storeroomViewModel: StoreroomViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var storeroomRecyclerView: RecyclerView
    private lateinit var storeroomList: ArrayList<Food>
    private lateinit var foodAdapter: StoreroomFoodCardAdapter

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_storeroom, container, false)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
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
        storeroomViewModel = ViewModelProvider(requireActivity())[StoreroomViewModel::class.java]


        storeroomRecyclerView = binding.foodsStoreRoom
        storeroomRecyclerView.layoutManager = LinearLayoutManager(activity)
        storeroomRecyclerView.setHasFixedSize(true)
        binding.linearLayoutAdicion.setOnClickListener {
            searchViewModel.searchProduct = "storeroom"
            findNavController().navigate(R.id.searchFood)
        }
        binding.searchView.setOnClickListener({
            binding.searchView.setIconified(false)
        })
        analizarEscenario()
        getFoodArray()

    }

    private fun getFoodArray() {
        storeroomViewModel.storeroomList.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    Log.d("searchview", it.result.size.toString())
                    storeroomList = it.result
                    foodAdapter = StoreroomFoodCardAdapter(storeroomList, storeroomViewModel)
                    storeroomRecyclerView.adapter = foodAdapter
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goBack(){
        findNavController().navigate(R.id.action_userStoreroom_to_homeSession)
    }

    private fun analizarEscenario(){
        if(searchViewModel.searchProduct == "storeroom"){
            if(searchViewModel.selectedProduct != null){
                storeroomViewModel.addFood(searchViewModel.selectedProduct!!)
                searchViewModel.searchProduct = ""
                searchViewModel.selectedProduct = null

            }
        }
    }

    private fun filter(text: String) {
        val filteredlist = java.util.ArrayList<Food>()
        for (item in storeroomList) {
            if (item.name?.lowercase()?.contains(text.lowercase(Locale.getDefault())) == true) {
                filteredlist.add(item)
            }
        }
        foodAdapter = StoreroomFoodCardAdapter(filteredlist, storeroomViewModel)
        storeroomRecyclerView.adapter = foodAdapter

    }

}