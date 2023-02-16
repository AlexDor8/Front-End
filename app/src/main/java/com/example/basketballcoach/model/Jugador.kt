package com.example.basketballcoach.model

data class Jugador(
    val id: Int,
    val nombre: String,
    val primerApellido: String,
    val posicion: String,
    val dorsal: Int,
    val rol: String,
    val salud: String,
    val altura: String,
    val manoDominante: String,
    val foto: String
) {
}