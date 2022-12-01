package com.example.vitalize

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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentUserEditProfileBinding
import com.example.vitalize.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

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