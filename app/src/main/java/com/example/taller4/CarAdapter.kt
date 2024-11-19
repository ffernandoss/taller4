package com.example.taller4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class Car(val marca: String = "", val color: String = "", val modelo: String = "")

class CarAdapter(private val carList: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val marcaTextView: TextView = itemView.findViewById(R.id.marcaTextView)
        val colorTextView: TextView = itemView.findViewById(R.id.colorTextView)
        val modeloTextView: TextView = itemView.findViewById(R.id.modeloTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.marcaTextView.text = car.marca
        holder.colorTextView.text = car.color
        holder.modeloTextView.text = car.modelo
    }

    override fun getItemCount() = carList.size
}