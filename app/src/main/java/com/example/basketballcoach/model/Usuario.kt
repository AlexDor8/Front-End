package com.example.basketballcoach.model

import java.util.*

data class Usuario(
    var id:Int,
    var nombre: String,
    var contraseña: String,
    var correo: String,
    var fechaNacimiento: Date,
    var foto: String
) {


}