package com.example.vitalize.user

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentLogInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogIn : Fragment() {
    private val viewModel by viewModels<UserViewModel>()
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.example.vitalize.R.layout.fragment_log_in, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Setup a click listener for the Submit and go to Register Page buttons.
        binding.IniciaSesionLogin.setOnClickListener { iniciaSesion() }
        binding.RegistrarseLogin.setOnClickListener { toRegister() }
    }

    fun toRegister() {
        findNavController().navigate(com.example.vitalize.R.id.action_logIn_to_signUp)
    }


    private fun iniciaSesion() {
        val emailInput = binding.editTextEmailAddressLogin.text.toString()
        val passwordInput = binding.editTextPasswordLogin.text.toString()
        if (emailInput.isNotEmpty() && passwordInput.isEmpty()) {
            Toast.makeText(activity, "Contraseña requerida", Toast.LENGTH_SHORT).show()
        }
        else if (emailInput.isEmpty() && passwordInput.isNotEmpty()) {
            Toast.makeText(activity, "Correo requerido", Toast.LENGTH_SHORT).show()
        }
        else if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
            viewModel.logout()
            viewModel.login(emailInput, passwordInput)
            viewModel.loginFlow.observe(viewLifecycleOwner) {
                it?.let {
                    Log.d("viewmodellogin", it.toString())
                    when (it) {
                        is Resource.Failure -> {
                            viewModel.resetFlow()
                            Toast.makeText(activity,
                                "Inicio de sesión fallido",
                                Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Success -> {
                            Toast.makeText(activity,
                                "Inicio de sesión efectuado con éxito",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(activity, "Los campos deben estar rellenos", Toast.LENGTH_SHORT).show()
            Log.d("EmailPassword", "signIn:fieldsEmpty")
        }
    }


}


