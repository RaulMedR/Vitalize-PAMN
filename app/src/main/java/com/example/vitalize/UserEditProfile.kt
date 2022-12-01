package com.example.vitalize

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentUserEditProfileBinding
import com.example.vitalize.databinding.FragmentUserProfileBinding
import com.example.vitalize.user.UserViewModel
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.cancel

@AndroidEntryPoint
class UserEditProfile : Fragment() {
    private lateinit var userViewModel : UserViewModel
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentUserEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            cancel()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_edit_profile, container, false)

        val estaturas = resources.getStringArray(R.array.estaturas)
        val adapter_estaturas = ArrayAdapter(requireContext(), R.layout.dropdown_item, estaturas)
        binding.autoCompleteTextViewEstaturas.setAdapter(adapter_estaturas)

        val pesos = resources.getStringArray(R.array.pesos)
        val adapter_pesos = ArrayAdapter(requireContext(), R.layout.dropdown_item, pesos)
        binding.autoCompleteTextViewPesos.setAdapter(adapter_pesos)

        val pesos_decimales = resources.getStringArray(R.array.pesos_decimales)
        val adapter_pesos_decimales = ArrayAdapter(requireContext(), R.layout.dropdown_item, pesos_decimales)
        binding.autoCompleteTextViewPesosDecimales.setAdapter(adapter_pesos_decimales)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        profileDataSync()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonCancel.setOnClickListener { cancel() }
        binding.buttonSave.setOnClickListener { guardar() }
        binding.backArrow.setOnClickListener { cancel() }
    }

    private fun guardar() {
        if (binding.editNombreApellidos.text.isNotEmpty()){
            userViewModel.setNameUser(binding.editNombreApellidos.text.toString())
        }
        if(binding.dropdownMenuEstaturas.getEditText()?.getText()?.isNotEmpty() == true) {
            userViewModel.setHeight(binding.dropdownMenuEstaturas.getEditText()?.getText().toString())
        }
        val peso_entero = binding.dropdownMenuPesos.getEditText()?.getText().toString()
        val peso_decimales = binding.dropdownMenuDecimalesPesos.getEditText()?.getText().toString()
        if(peso_entero.isNotEmpty() && peso_decimales.isNotEmpty()) {
            userViewModel.setWeight((peso_entero.toDouble() + peso_decimales.toDouble()/10).toString())
        }
        else if (peso_entero.isNotEmpty() && peso_decimales.isEmpty()) {
            userViewModel.userWeight.value.let {
                when (it) {
                    is Resource.Success -> {
                        val arrayPeso = it.result!!.split(" ").toTypedArray()
                        userViewModel.setWeight((peso_entero.toDouble() + arrayPeso[0].toDouble() % 1).toString())
                    }
                    else -> {}
                }
            }

        }
        else if (peso_decimales.isNotEmpty()  && peso_entero.isEmpty() ) {
            userViewModel.userWeight.value.let {
                when (it) {
                    is Resource.Success -> {
                        val arrayPeso = it.result!!.split(" ").toTypedArray()
                        userViewModel.setWeight((arrayPeso[0].toDouble()-arrayPeso[0].toDouble() % 1 + peso_decimales.toDouble()/10).toString())
                    }
                    else -> {}
                }
            }

        }else{
            Log.d("otro", "entra4")
        }
        findNavController().navigate(R.id.action_userEditProfile_to_userProfile)

    }
    private fun cancel(){
        findNavController().navigate(R.id.action_userEditProfile_to_userProfile)
    }


    private fun profileDataSync() {
        binding.editNombreApellidos.setText(userViewModel.getNameCurrentUser())
        val photo = userViewModel.getPhotoUrl()
        if(photo != null){
            Glide.with(this).asBitmap().load(photo).into(binding.imagenPerfil)
        }
        else{
            binding.imagenPerfil.setImageDrawable(photo)
        }
        userViewModel.userWeight.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    val arrayPeso = it.result!!.split(" ").toTypedArray()
                    binding.autoCompleteTextViewPesos.setHint((arrayPeso[0].toDouble() - arrayPeso[0].toDouble()%1).toInt().toString())
                    binding.autoCompleteTextViewPesosDecimales.setHint((arrayPeso[0].toDouble()%1*10).toInt().toString())
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        userViewModel.userHeight.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    val arrayEstaturas = it.result!!.split(" ").toTypedArray()
                    binding.autoCompleteTextViewEstaturas.setHint(arrayEstaturas[0])
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