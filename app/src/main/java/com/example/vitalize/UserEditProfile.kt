package com.example.vitalize

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentUserEditProfileBinding
import com.example.vitalize.databinding.FragmentUserProfileBinding
import com.example.vitalize.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.cancel

@AndroidEntryPoint
class UserEditProfile : Fragment() {
    private val userViewModel by viewModels<UserViewModel>()
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentUserEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_edit_profile, container, false)
        val estaturas = resources.getStringArray(R.array.estaturas)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, estaturas)
        Log.d("prueba", binding.autoCompleteTextViewEstaturas.dropDownHeight.toString())
        binding.autoCompleteTextViewEstaturas.setAdapter(adapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonCancel.setOnClickListener { cancel() }
        binding.buttonSave.setOnClickListener { guardar() }
        //profileDataSync()
    }

    private fun guardar() {
        TODO("Not yet implemented")
    }
    private fun cancel(){
        findNavController().navigate(R.id.action_userEditProfile_to_userProfile)
    }

    /*private fun profileDataSync() {
        binding.nombreApellidosProfileUser.text = userViewModel.getNameCurrentUser()
        val photo = userViewModel.getPhotoUrl()
        Log.d("profiledata", photo.toString())
        if(photo != null){
            Glide.with(this).asBitmap().load(photo).into(binding.imagenPerfil)
        }
        userViewModel.getWeightCurrentUser()
        userViewModel.userWeight.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.textPeso.text = it.result
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        userViewModel.getHeightCurrentUser()
        userViewModel.userHeight.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.textAltura.text = it.result
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    }*/



}