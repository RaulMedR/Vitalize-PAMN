package com.example.vitalize

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vitalize.camera.CameraPreview
import com.example.vitalize.data.FirestoreRepository
import com.example.vitalize.data.FirestoreRepositoryImpl
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentScanBarcodeBinding
import com.example.vitalize.user.UserViewModel
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import java.io.IOException


class ScanBarcode : Fragment() {
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentScanBarcodeBinding

    private lateinit var foodViewModel : FoodViewModel
    private lateinit var food: Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            goBack()
        }
        escanear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.example.vitalize.R.layout.fragment_scan_barcode, container, false)
        binding.backArrow.setOnClickListener { goBack() }
        binding.buttonCapture.setOnClickListener{ escanear() }
        foodViewModel = ViewModelProvider(requireActivity())[FoodViewModel::class.java]
        return binding.root
    }

    private fun goBack() {
        findNavController().navigate(com.example.vitalize.R.id.action_scanBarcode_to_homeSession)
    }

    private fun escanear() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("\nESCANEAR CÓDIGO DE BARRAS\n")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(activity, "Cancelado el escaneo", Toast.LENGTH_LONG).show()
            } else {
                Log.d("prueba", result.contents)
                buscarProducto(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /*private fun buscarProducto(barcode: String) {
        foodViewModel.buscarComida(barcode)
        foodViewModel.findFoodFlow.value.let {
            when (it) {
                is Resource.Success -> {
                    Log.d("prueba", it.toString())
                    Log.d("prueba", it.result)
                    //binding.barcodeText.setText("Producto escaneado " + it.result)
                }
                else -> {
                    Log.d("prueba", it.toString())
                    Toast.makeText(activity, "El producto escaneado no se encuentra registrado", Toast.LENGTH_LONG).show()
                }
            }
        }

    }*/

    private fun buscarProducto(barcode: String){
        foodViewModel.buscarComida(barcode)
        foodViewModel.foodDataSnapshot.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Resource.Success -> {

                        food =  it.result?.result?.toObject(Food::class.java)!!
                        binding.gridLayout.visibility =  View.VISIBLE
                        binding.imagenProducto.visibility =  View.VISIBLE
                        binding.nombreProducto.visibility =  View.VISIBLE
                        binding.buttonAddToDiet.visibility =  View.VISIBLE
                        binding.buttonAddToStoreroom.visibility =  View.VISIBLE
                        binding.textoProductoEscaneado.visibility = View.VISIBLE
                        food.name?.let { itName -> binding.nombreProducto.setText(itName) }

                        //binding.productName
                            //.setText(product.name)

                        //var imagen = product.image

                        //Glide.with(this).asBitmap().load(imagen)
                            //.into(binding.productImage)

                        //binding.product.visibility = View.VISIBLE
                    }

                    else -> {
                        Log.e("prueba", "errorrrrr")
                    }
                }
            }
        }
    }


}