package com.example.vitalize.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vitalize.R
import com.example.vitalize.databinding.FragmentLogInBinding
import com.example.vitalize.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUp : Fragment() {
    private val viewModel: UserViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mAuth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.example.vitalize.R.layout.fragment_sign_up, container, false)
        mAuth = FirebaseAuth.getInstance()
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
        findNavController().navigate(com.example.vitalize.R.id.action_signUp_to_logIn)
    }


    private fun registrarse() {
        if (!binding.editTextNameRegister.text.toString().isEmpty() && !binding.editTextEmailAddressRegister.text.toString().isEmpty() &&
            !binding.editTextPasswordRegister.text.toString().isEmpty() && !binding.editTextRepeatPasswordRegister.text.toString().isEmpty()) {
            if (binding.editTextPasswordRegister.text.toString() == binding.editTextRepeatPasswordRegister.text.toString()) {
                //viewModel.createAccount(binding.editTextEmailAddressRegister.text.toString(), binding.editTextPasswordRegister.text.toString())
            }
            else{
                Log.d("EmailPassword", "signUp:contraseñasNoIguales")
            }
        } else {
            //Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            Log.d("EmailPassword", "signUp:fieldsEmpty")
        }
    }

}