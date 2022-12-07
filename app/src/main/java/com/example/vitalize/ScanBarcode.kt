package com.example.vitalize

import android.content.Intent
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
import com.example.vitalize.data.Resource
import com.example.vitalize.databinding.FragmentScanBarcodeBinding
import com.google.zxing.integration.android.IntentIntegrator


class ScanBarcode : Fragment() {
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentScanBarcodeBinding

    private lateinit var foodViewModel : FoodViewModel
    private lateinit var food: Food
    private var visibleProducto : Boolean = false

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
        hideProducto()
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
                hideProducto()
                Toast.makeText(activity, "Cancelado el escaneo", Toast.LENGTH_LONG).show()
            } else {
                buscarProducto(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun buscarProducto(barcode: String){
        foodViewModel.buscarComida(barcode)
        foodViewModel.foodDataSnapshot.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Resource.Success -> {
                        if (it.result?.result?.exists() == false){
                            hideProducto()
                            Toast.makeText(activity, "El producto escaneado no se encuentra registrado", Toast.LENGTH_LONG).show()
                        }
                        else{
                            food =  it.result?.result?.toObject(Food::class.java)!!
                            showProducto()
                            food.name?.let { itName -> binding.nombreProducto.setText(itName) }
                        }
                    }
                    else -> {
                        // Nunca se comete error
                    }
                }
            }
        }
    }

    private fun showProducto(){
        binding.gridLayout.visibility =  View.VISIBLE
        binding.imagenProducto.visibility =  View.VISIBLE
        binding.nombreProducto.visibility =  View.VISIBLE
        binding.buttonAddToDiet.visibility =  View.VISIBLE
        binding.buttonAddToStoreroom.visibility =  View.VISIBLE
        binding.textoProductoEscaneado.visibility = View.VISIBLE
        visibleProducto = true
    }

    private fun hideProducto(){
        binding.gridLayout.visibility =  View.INVISIBLE
        binding.imagenProducto.visibility =  View.INVISIBLE
        binding.nombreProducto.visibility =  View.INVISIBLE
        binding.buttonAddToDiet.visibility =  View.INVISIBLE
        binding.buttonAddToStoreroom.visibility =  View.INVISIBLE
        binding.textoProductoEscaneado.visibility = View.INVISIBLE
    }


}