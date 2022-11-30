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
    private val userViewModel by viewModels<UserViewModel>()
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
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonCancel.setOnClickListener { cancel() }
        binding.buttonSave.setOnClickListener { guardar() }
        binding.backArrow.setOnClickListener { cancel() }
        profileDataSync()
    }

    private fun guardar() {
        if (binding.editNombreApellidos.text.isNotEmpty()){
            userViewModel.setNameUser(binding.editNombreApellidos.text.toString())
        }
        if(binding.dropdownMenuEstaturas.getEditText()?.getText()?.isNotEmpty() == true) {
            userViewModel.setHeight(binding.dropdownMenuEstaturas.getEditText()?.getText().toString().toDouble())
        }

        val peso_entero = binding.dropdownMenuPesos.getEditText()?.getText().toString()
        val peso_decimales = binding.dropdownMenuDecimalesPesos.getEditText()?.getText().toString()

        if(peso_entero.isNotEmpty() && peso_decimales.isNotEmpty()) {
            userViewModel.setWeight(peso_entero.toDouble())
            userViewModel.setWeightDecimals(peso_decimales.toDouble())
        }
        else if (peso_decimales.isNotEmpty() && peso_decimales.isEmpty()) {
            userViewModel.setWeight(peso_entero.toDouble())
        }
        else if (peso_decimales.isNotEmpty()  && peso_decimales.isEmpty() ) {
            userViewModel.setWeightDecimals(peso_decimales.toDouble())
        }
        findNavController().navigate(R.id.action_userEditProfile_to_userProfile)

    }
    private fun cancel(){
        findNavController().navigate(R.id.action_userEditProfile_to_userProfile)
    }

    private fun convertToDouble(entero: Double, decimales: Double){

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
        /*userViewModel.getWeightCurrentUser()
        userViewModel.userWeight.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.autoCompleteTextViewEstaturas.setText(it.result)
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }*/
        userViewModel.getHeightCurrentUser()
        userViewModel.userHeight.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.autoCompleteTextViewEstaturas.setText(it.result)
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