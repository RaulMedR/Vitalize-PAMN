package com.example.vitalize.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.vitalize.R
import com.example.vitalize.databinding.FragmentHomeNoSessionBinding

class HomeNoSession : Fragment() {
    private lateinit var binding: FragmentHomeNoSessionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home_no_session, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.lifecycleOwner = viewLifecycleOwner
        // Setup a click listener for the Submit and go to Login Page buttons.
        binding.RegisterHomeNoSession.setOnClickListener { toRegister() }
        binding.LoginHomeNoSession.setOnClickListener { toLogin() }
    }

    fun toLogin() {
        findNavController().navigate(R.id.action_homeNoSession_to_logIn)
    }

    fun toRegister() {
        findNavController().navigate(R.id.action_homeNoSession_to_signUp)
    }
}