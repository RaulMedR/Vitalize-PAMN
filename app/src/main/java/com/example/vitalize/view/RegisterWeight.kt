package com.example.vitalize.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.vitalize.R
import com.example.vitalize.databinding.FragmentRegisterWeightBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class RegisterWeight : Fragment(){
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: FragmentRegisterWeightBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_register_weight, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.calendarIcon.setOnClickListener { selectDate() }
        binding.buttonRegistrarPeso.setOnClickListener({registrarPeso()})

        val pesos = resources.getStringArray(R.array.pesos)
        val adapter_pesos = ArrayAdapter(requireContext(), R.layout.dropdown_item, pesos)
        binding.autoCompleteTextViewPesos.setAdapter(adapter_pesos)

        val pesos_decimales = resources.getStringArray(R.array.pesos_decimales)
        val adapter_pesos_decimales = ArrayAdapter(requireContext(),
            R.layout.dropdown_item, pesos_decimales)
        binding.autoCompleteTextViewPesosDecimales.setAdapter(adapter_pesos_decimales)
        //profileDataSync()
    }

    private fun selectDate(){
        val calendario = Calendar.getInstance()
        val yy = calendario[Calendar.YEAR]
        val mm = calendario[Calendar.MONTH]
        val dd = calendario[Calendar.DAY_OF_MONTH]


        val datePicker = DatePickerDialog(
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1
                val date = "$dayOfMonth/$month/$year"
                binding.fecha.setText(date)
            }, yy, mm, dd
        )

        datePicker.show()
    }


    private fun registrarPeso(){
        //findNavController().navigate(R.id.action_userEditProfile_to_userProfile)
    }



}