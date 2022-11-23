package com.example.vitalize

import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentLogInBinding
import com.example.vitalize.databinding.FragmentUserProfileBinding
import com.example.vitalize.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfile : Fragment() {
    private val userViewModel by viewModels<UserViewModel>()
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.example.vitalize.R.layout.fragment_user_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        profileDataSync()
    }

    private fun profileDataSync() {
        binding.nombreApellidosProfileUser.text = userViewModel.getNameCurrentUser()
        val photo = userViewModel.getPhotoUrl()
        Log.d("profiledata", photo.toString())
        if(photo != null){
            binding.imagenPerfil.setImageURI(photo)
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

    }

}