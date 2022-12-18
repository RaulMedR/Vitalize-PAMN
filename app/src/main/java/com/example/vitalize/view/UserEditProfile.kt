package com.example.vitalize.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vitalize.R
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentUserEditProfileBinding
import com.example.vitalize.viewModels.UserViewModel
import com.example.vitalize.viewModels.DietViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserEditProfile : Fragment() {
    private lateinit var userViewModel : UserViewModel
    private lateinit var dietViewModel : DietViewModel
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
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_user_edit_profile, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        dietViewModel = ViewModelProvider(requireActivity())[DietViewModel::class.java]
        profileDataSync()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonCancel.setOnClickListener { cancel() }
        binding.buttonSave.setOnClickListener { guardar() }
        binding.backArrow.setOnClickListener { cancel() }
        binding.iconBin.setOnClickListener{ alertaEliminar() }
        binding.imagenPerfilChangePhoto.setOnClickListener{ requestPermission() }
    }



    private fun alertaEliminar() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Borrar foto de perfil")
        builder.setMessage("¿Desea eliminar la foto de perfil?")
        builder.setPositiveButton("Aceptar", { dialog, id ->
            userViewModel.setPhotoUrl(Uri.parse("null"))
            binding.imagenPerfil.setImageDrawable(getResources().getDrawable(R.drawable.logo_with_background))
            Toast.makeText(context,
                "Se ha eliminado correctamente la foto de perfil", Toast.LENGTH_SHORT).show()
        })
        builder.setNegativeButton("Cancelar") { dialog, which ->
        }
        builder.show()

    }

    private fun guardar() {
        if (binding.editNombreApellidos.text.isNotEmpty()){
            userViewModel.setNameUser(binding.editNombreApellidos.text.toString())
        }
        if(binding.estaturaEditText.text.isNotEmpty()) {
            userViewModel.setHeight(binding.estaturaEditText.text.toString())
        }
        if(binding.pesoEditText.text.isNotEmpty()) {
            userViewModel.setWeight(binding.pesoEditText.text.toString())
        }
        if(binding.textNumCalorias.text.isNotEmpty()){
            dietViewModel.updateCantidadObjetivo(binding.textNumCalorias.text.toString().toInt())
        }
        findNavController().navigate(R.id.action_userEditProfile_to_userProfile)

    }
    private fun cancel(){
        findNavController().navigate(R.id.action_userEditProfile_to_userProfile)
    }


    private fun profileDataSync() {
        binding.editNombreApellidos.setText(userViewModel.getNameCurrentUser())
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
                    val arrayPeso = it.result.split(" ").toTypedArray()
                    binding.pesoEditText.setHint((arrayPeso[0].toDouble()).toString())
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
                    val arrayEstaturas = it.result.split(" ").toTypedArray()
                    binding.estaturaEditText.setHint(arrayEstaturas[0])
                }
                is Resource.Failure -> {
                    Toast.makeText(activity,
                        it.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        dietViewModel.cantidadObjetivo.observe(viewLifecycleOwner) { cantidadObjetivo ->
            binding.textNumCalorias.setHint(cantidadObjetivo)
        }

    }
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } == PackageManager.PERMISSION_GRANTED -> {
                    pickPhotoFromGallery()
                }
                else -> requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            pickPhotoFromGallery()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted ->
        if (isGranted){
            pickPhotoFromGallery()
        }
        else{
            Toast.makeText(context, "Se necesita habilitar los permisos", Toast.LENGTH_SHORT).show()
        }

    }
    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data
            if (data != null) {
                userViewModel.setPhotoUrl(data)
            }
            binding.imagenPerfil.setImageURI(data)
        }

    }
    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }


}