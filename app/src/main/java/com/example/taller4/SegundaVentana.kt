package com.example.taller4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.taller4.ui.theme.Taller4Theme

class SegundaVentana : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Taller4Theme {
                Text(text = "Bienvenido a la Segunda Ventana")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegundaVentanaPreview() {
    Taller4Theme {
        Text(text = "Bienvenido a la Segunda Ventana")
    }
}