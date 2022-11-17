package com.example.vitalize.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vitalize.R
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUp : Fragment() {
    private val viewModel: UserViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentSignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Setup a click listener for the Submit and Skip buttons.
        binding.buttonRegister.setOnClickListener { registrarse() }
        binding.IniciaSesionRegister.setOnClickListener { toLogin() }
    }

    fun toLogin() {
        findNavController().navigate(R.id.action_signUp_to_logIn)
    }


    private fun registrarse() {
        val emailInput = binding.editTextEmailAddressRegister.text.toString()
        val passwordInput = binding.editTextPasswordRegister.text.toString()
        val repeatPasswordInput = binding.editTextRepeatPasswordRegister.text.toString()
        val nameInput = binding.editTextNameRegister.text.toString()

        if (emailInput.isNotEmpty() && passwordInput.isNotEmpty() && repeatPasswordInput.isNotEmpty() && nameInput.isNotEmpty()) {
            if (passwordInput == repeatPasswordInput) {
                Log.d("viewmodelregister", "he")
                viewModel.singup(nameInput, emailInput, passwordInput)
                viewModel.signupFlow.value?.let {
                    Log.d("viewmodelregister", it.toString())
                    when(it){
                        is Resource.Failure -> {
                            if (it.exception.toString() == "ERROR_EMAIL_ALREADY_IN_USE"){

                                Toast.makeText(activity, "El email ya se encuentra registrado", Toast.LENGTH_SHORT).show()
                            }
                            else if (it.exception.toString() == "ERROR_INVALID_EMAIL"){
                                Toast.makeText(activity, "El formato del email es inválido", Toast.LENGTH_SHORT).show()
                            }
                            else if (it.exception.toString() == "ERROR_WEAK_PASSWORD"){
                                Toast.makeText(activity, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(activity, it.exception, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> {}
                    }
                }
            }
            else{
                Toast.makeText(activity, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                Log.d("EmailPassword", "signUp:contraseñasNoIguales")
            }
        } else {
            Toast.makeText(activity, "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show()
            Log.d("EmailPassword", "signUp:fieldsEmpty")
        }


    }

}