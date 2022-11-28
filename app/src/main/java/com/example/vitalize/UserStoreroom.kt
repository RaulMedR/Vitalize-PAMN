package com.example.vitalize

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.vitalize.databinding.FragmentUserProfileBinding
import com.example.vitalize.databinding.FragmentUserStoreroomBinding

class UserStoreroom : Fragment() {
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentUserStoreroomBinding

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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backArrow.setOnClickListener { goBack() }

    }

    private fun goBack(){
        findNavController().navigate(R.id.action_userStoreroom_to_homeSession)
    }

}