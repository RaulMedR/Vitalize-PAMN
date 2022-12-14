package com.example.vitalize

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentUserProfileBinding
import com.example.vitalize.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfile : Fragment() {
    private lateinit var userViewModel : UserViewModel
    private lateinit var dietViewModel : DietViewModel

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            goBack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        dietViewModel = ViewModelProvider(requireActivity())[DietViewModel::class.java]
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonLogOut.setOnClickListener { logOut() }
        binding.editUserProfileIcon.setOnClickListener{ toEditUserProfile()}
        binding.backArrow.setOnClickListener { goBack() }
        profileDataSync()
    }

    private fun toEditUserProfile() {
        findNavController().navigate(R.id.action_userProfile_to_userEditProfile)
    }

    private fun profileDataSync() {
        binding.nombreApellidosProfileUser.text = userViewModel.getNameCurrentUser()
        val photo = userViewModel.getPhotoUrl()
        if (photo != null) {
            if(!photo.equals(Uri.parse("null"))){
                Glide.with(this).asBitmap().load(photo).into(binding.imagenPerfil)
            }
            else{
                binding.imagenPerfil.setImageDrawable(getResources().getDrawable(R.drawable.logo_with_background))
            }
        }
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
        dietViewModel.cantidadObjetivo.observe(viewLifecycleOwner) { cantidadObjetivo ->
            binding.textNumCalorias.text = cantidadObjetivo
        }
    }
    private fun goBack(){
        findNavController().navigate(R.id.action_userProfile_to_homeSession)
    }
    private fun logOut(){
        userViewModel.logout()
        Toast.makeText(activity,
            "Se ha cerrado sesión correctamente",
            Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_userProfile_to_homeNoSession)

    }

}