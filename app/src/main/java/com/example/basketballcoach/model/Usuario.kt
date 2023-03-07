package com.example.basketballcoach.model

import java.io.Serializable
import java.util.*

data class Usuario(
    var id:Int,
    var nombre: String,
    var contraseña: String,
    var email: String,
    var fechaNacimiento: String,
    var foto: String
) :Serializable{


}