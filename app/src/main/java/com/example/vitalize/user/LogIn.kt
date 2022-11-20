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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.vitalize.databinding.FragmentLogInBinding


class LogIn : Fragment() {
    private val viewModel: UserViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentLogInBinding
    private lateinit var mAuth: FirebaseAuth;
    //private var email_input: EditText? = null
    //private  var password_input:EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.example.vitalize.R.layout.fragment_log_in, container, false)
        //val view = inflater.inflate(com.example.vitalize.R.layout.fragment_log_in, container, false)
        //email_input = view.findViewById(com.example.vitalize.R.id.editTextEmailAddressLogin)
        //password_input = view.findViewById(com.example.vitalize.R.id.editTextPasswordLogin)
        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Setup a click listener for the Submit and Skip buttons.
        binding.IniciaSesionLogin.setOnClickListener { iniciaSesion() }
        binding.RegistrarseLogin.setOnClickListener { toRegister() }
    }

    fun toRegister() {
        findNavController().navigate(com.example.vitalize.R.id.action_logIn_to_signUp)
    }


    private fun iniciaSesion() {
        if (!binding.editTextEmailAddressLogin.text.toString().isEmpty() && binding.editTextPasswordLogin.text.toString().isEmpty()) {
            binding.editTextPasswordLogin.setError("Contraseña requerida")

        }
        if (binding.editTextEmailAddressLogin.text.toString().isEmpty() && !binding.editTextPasswordLogin.text.toString().isEmpty()) {
            binding.editTextEmailAddressLogin.setError("Correo requerido")
        }
        if (!binding.editTextEmailAddressLogin.text.toString().isEmpty() && !binding.editTextPasswordLogin.text.toString().isEmpty()) {
            viewModel.signIn(binding.editTextEmailAddressLogin.text.toString(), binding.editTextPasswordLogin.text.toString())
        } else {
            //Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            Log.d("EmailPassword", "signIn:fieldsEmpty")
        }
    }


}


