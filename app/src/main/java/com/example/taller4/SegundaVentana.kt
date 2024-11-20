package com.example.taller4

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.taller4.ui.theme.Taller4Theme
import com.google.firebase.firestore.FirebaseFirestore

class SegundaVentana : FragmentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda_ventana)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace<CarListFragment>(R.id.fragment_container)
            }
        }

        findViewById<ComposeView>(R.id.compose_view).setContent {
            Taller4Theme {
                CarForm()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se necesita implementar
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]
            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            if (acceleration > 12) {
                findViewById<ComposeView>(R.id.compose_view).setContent {
                    Taller4Theme {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary))
                    }
                }
            }
        }
    }
}

@Composable
fun CarForm() {
    var marca by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var fechaCompra by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = marca,
            onValueChange = { marca = it },
            label = { Text("Marca") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = color,
            onValueChange = { color = it },
            label = { Text("Color") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = modelo,
            onValueChange = { modelo = it },
            label = { Text("Modelo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it },
            label = { Text("Matrícula") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = fechaCompra,
            onValueChange = { fechaCompra = it },
            label = { Text("Fecha de Compra") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { addCarToFirebase(context, marca, color, modelo, matricula, fechaCompra) }) {
            Text("Añadir Coche")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showDialog = true }) {
            Text("Borrar Coche")
        }
    }

    if (showDialog) {
        showDeleteCarDialog(context) { showDialog = false }
    }
}

fun addCarToFirebase(context: android.content.Context, marca: String, color: String, modelo: String, matricula: String, fechaCompra: String) {
    val db = FirebaseFirestore.getInstance()
    val car = hashMapOf(
        "marca" to marca,
        "color" to color,
        "modelo" to modelo,
        "matricula" to matricula,
        "fechaCompra" to fechaCompra
    )

    db.collection("coches")
        .add(car)
        .addOnSuccessListener {
            Toast.makeText(context, "Coche añadido", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error al añadir coche: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}

@Composable
fun showDeleteCarDialog(context: android.content.Context, onDismiss: () -> Unit) {
    var carName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Borrar Coche") },
        text = {
            Column {
                Text("Introduce el nombre del coche a borrar:")
                OutlinedTextField(
                    value = carName,
                    onValueChange = { carName = it },
                    label = { Text("Nombre del coche") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                deleteCarFromFirebase(context, carName)
                onDismiss()
            }) {
                Text("Borrar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

fun deleteCarFromFirebase(context: android.content.Context, carName: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("coches")
        .whereEqualTo("marca", carName)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                Toast.makeText(context, "Coche no encontrado", Toast.LENGTH_SHORT).show()
            } else {
                for (document in documents) {
                    db.collection("coches").document(document.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Coche borrado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error al borrar coche: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error al buscar coche: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}

@Preview(showBackground = true)
@Composable
fun CarFormPreview() {
    Taller4Theme {
        CarForm()
    }
}