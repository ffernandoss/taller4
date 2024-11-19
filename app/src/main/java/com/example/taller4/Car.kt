// File: app/src/main/java/com/example/taller4/Car.kt
package com.example.taller4

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    val marca: String = "",
    val color: String = "",
    val modelo: String = "",
    val matricula: String = "",
    val fechaCompra: String = ""
) : Parcelable