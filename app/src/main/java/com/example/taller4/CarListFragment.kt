// File: app/src/main/java/com/example/taller4/CarListFragment.kt
package com.example.taller4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class CarListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private val carList = mutableListOf<Car>()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        carAdapter = CarAdapter(carList) { car ->
            val fragment = CarDetailsFragment.newInstance(car)
            parentFragmentManager.commit {
                replace(R.id.details_fragment_container, fragment)
                addToBackStack(null)
            }
        }
        recyclerView.adapter = carAdapter
        listenToCarUpdates()
        return view
    }

    private fun listenToCarUpdates() {
        val db = FirebaseFirestore.getInstance()
        listenerRegistration = db.collection("coches")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(context, "Error al cargar coches: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    carList.clear()
                    for (document in snapshots) {
                        val car = document.toObject(Car::class.java)
                        carList.add(car)
                    }
                    carAdapter.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration?.remove()
    }
}