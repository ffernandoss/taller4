package com.example.taller4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taller4.ui.theme.Taller4Theme
import com.google.firebase.firestore.FirebaseFirestore

class SegundaVentana : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Taller4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CarForm()
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { addCarToFirebase(context, marca, color, modelo) }) {
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

fun addCarToFirebase(context: android.content.Context, marca: String, color: String, modelo: String) {
    val db = FirebaseFirestore.getInstance()
    val car = hashMapOf(
        "marca" to marca,
        "color" to color,
        "modelo" to modelo
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