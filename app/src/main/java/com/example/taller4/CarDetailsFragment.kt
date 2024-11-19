package com.example.taller4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class CarDetailsFragment : Fragment() {

    private lateinit var marcaTextView: TextView
    private lateinit var colorTextView: TextView
    private lateinit var modeloTextView: TextView
    private lateinit var matriculaTextView: TextView
    private lateinit var fechaCompraTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car_details, container, false)
        marcaTextView = view.findViewById(R.id.marcaTextView)
        colorTextView = view.findViewById(R.id.colorTextView)
        modeloTextView = view.findViewById(R.id.modeloTextView)
        matriculaTextView = view.findViewById(R.id.matriculaTextView)
        fechaCompraTextView = view.findViewById(R.id.fechaCompraTextView)

        val car = arguments?.getParcelable<Car>("car")
        car?.let {
            marcaTextView.text = it.marca
            colorTextView.text = it.color
            modeloTextView.text = it.modelo
            matriculaTextView.text = it.matricula
            fechaCompraTextView.text = it.fechaCompra
        }

        return view
    }

    companion object {
        fun newInstance(car: Car): CarDetailsFragment {
            val fragment = CarDetailsFragment()
            val args = Bundle()
            args.putParcelable("car", car)
            fragment.arguments = args
            return fragment
        }
    }
}