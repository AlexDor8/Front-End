package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.basketballcoach.model.*
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ActualizarJugador : AppCompatActivity() {

    lateinit var nombreActualizarJugador: EditText
    lateinit var apellidoActualizarJugador: EditText
    lateinit var posicionActualizarJugador: EditText
    lateinit var dorsalActualizarJugador: EditText
    lateinit var rolActualizarJugador: EditText
    lateinit var saludActualizarJugador: EditText
    lateinit var alturaActualizarJugador: EditText
    lateinit var manoDominanteActualizarJugador: EditText
    lateinit var actulizarImagen: ImageView;
    lateinit var buttonActNom: ImageButton
    lateinit var buttonActApe: ImageButton
    lateinit var buttonActPos: ImageButton
    lateinit var buttonActDor: ImageButton
    lateinit var buttonActRol: ImageButton
    lateinit var buttonActSal: ImageButton
    lateinit var buttonActAlt: ImageButton
    lateinit var buttonActManDom: ImageButton

    lateinit var jugador:Jugador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_jugador)

        nombreActualizarJugador = findViewById(R.id.nombreJugadorActualizar)
        apellidoActualizarJugador = findViewById<EditText>(R.id.apellidoJugadorActualizar)
        posicionActualizarJugador = findViewById<EditText>(R.id.posicionJugadorActualizar)
        dorsalActualizarJugador = findViewById<EditText>(R.id.dorsalActualizar)
        rolActualizarJugador = findViewById<EditText>(R.id.rolActualizar)
        saludActualizarJugador = findViewById<EditText>(R.id.saludActualizar)
        alturaActualizarJugador = findViewById<EditText>(R.id.alturaActualizar)
        manoDominanteActualizarJugador = findViewById<EditText>(R.id.manoDominanteActualizar)
        actulizarImagen = findViewById<ImageView>(R.id.imagenJugadorActualizar)

        buttonActNom = findViewById<ImageButton>(R.id.buttonActualizarJugador)
        buttonActApe = findViewById<ImageButton>(R.id.buttonActualizarJugadorApellido)
        buttonActPos = findViewById<ImageButton>(R.id.buttonActualizarJugadorPosicion)
        buttonActDor = findViewById<ImageButton>(R.id.buttonActualizarJugadorDorsal)
        buttonActRol = findViewById<ImageButton>(R.id.buttonActualizarJugadorRol)
        buttonActSal = findViewById<ImageButton>(R.id.buttonActualizarJugadorSalud)
        buttonActAlt = findViewById<ImageButton>(R.id.buttonActualizarJugadorAltura)
        buttonActManDom = findViewById<ImageButton>(R.id.buttonActualizarJugadorManoDominante)

        setData()
        cambiarNombreJugador()
        cambiarApellidoJugador()
        cambiarPosicionJugador()
        cambiarDorsalJugador()
        cambiarRolJugador()
        cambiarSaludJugador()
        cambiarAlturaJugador()
        cambiarManoDominanteJugador()
    }

    fun setData() {
        jugador = intent.getSerializableExtra("jugadorActualizar") as Jugador

        nombreActualizarJugador.setText(jugador.nombre)
        apellidoActualizarJugador.setText(jugador.apellido)
        posicionActualizarJugador.setText(jugador.posicion)
        dorsalActualizarJugador.setText(jugador.dorsal.toString())
        rolActualizarJugador.setText(jugador.rol)
        saludActualizarJugador.setText(jugador.salud)
        alturaActualizarJugador.setText(jugador.altura.toString())
        manoDominanteActualizarJugador.setText(jugador.manoDominante)
    }

    fun cambiarNombreJugador() {
        buttonActNom.setOnClickListener {
            var nuevoJugadorNombre = nombreActualizarJugador.text.toString()
            conexionCambiarNombre(jugador.id, nuevoJugadorNombre)
        }
    }

    fun conexionCambiarNombre(id: Int, nombre: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editName("baloncesto/cNombreJugador", UpdateUser(id, nombre))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "El nombre ha sido cambiado", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun cambiarApellidoJugador() {
        buttonActApe.setOnClickListener {
            var nuevoApellidoJugador = apellidoActualizarJugador.text.toString()
            conexionCambiarApellido(jugador.id, nuevoApellidoJugador)
        }
    }

    fun conexionCambiarApellido(id: Int, apellido: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editSurname("baloncesto/cApellidoJugador", UpadteApellido(id, apellido))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "El apellido ha sido cambiado", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun cambiarPosicionJugador() {
        buttonActPos.setOnClickListener {
            var nuevoPosicionJugador = posicionActualizarJugador.text.toString()
            conexionCambiarPosicion(jugador.id, nuevoPosicionJugador)
        }
    }

    fun conexionCambiarPosicion(id: Int, posicion: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editPosition("baloncesto/cPosicionJugador", UpdatePosicion(id, posicion))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "La posicion ha sido cambiada", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun cambiarDorsalJugador() {
        buttonActDor.setOnClickListener {
            var nuevoDorsalJugador = dorsalActualizarJugador.text.toString().toInt()
            conexionCambiarDorsal(jugador.id, nuevoDorsalJugador)
        }
    }

    fun conexionCambiarDorsal(id: Int, dorsal: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editDorsal("baloncesto/cDorsalJugador", UpdateDorsal(id, dorsal))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "El dorsal ha sido cambiado", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun cambiarRolJugador() {
        buttonActRol.setOnClickListener {
            var nuevoRolJugador = rolActualizarJugador.text.toString()
            conexionCambiarRol(jugador.id, nuevoRolJugador)
        }
    }

    fun conexionCambiarRol(id: Int, rol: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editRol("baloncesto/cRolJugador", UpdateRol(id, rol))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "El rol ha sido cambiado", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun cambiarSaludJugador() {
        buttonActSal.setOnClickListener {
            var nuevaSaludJugador = saludActualizarJugador.text.toString()
            conexionCambiarSalud(jugador.id, nuevaSaludJugador)
        }
    }

    fun conexionCambiarSalud(id: Int, salud: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editSalud("baloncesto/cSaludJugador", UpdateSalud(id, salud))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "La salud ha sido cambiada", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun cambiarAlturaJugador() {
        buttonActAlt.setOnClickListener {
            var nuevaAlturaJugador = alturaActualizarJugador.text.toString().toInt()
            conexionCambiarAltura(jugador.id, nuevaAlturaJugador)
        }
    }

    fun conexionCambiarAltura(id: Int, altura: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editAltura("baloncesto/cAlturaJugador", UpdateAltura(id, altura))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "La altura ha sido cambiada", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun cambiarManoDominanteJugador() {
        buttonActManDom.setOnClickListener {
            var nuevaManoDominanteJugador = manoDominanteActualizarJugador.text.toString()
            conexionCambiarManoDominante(jugador.id, nuevaManoDominanteJugador)
        }
    }

    fun conexionCambiarManoDominante(id: Int, manoDominante: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editManodDominante("baloncesto/cManoJugador", UpdateManoDominante(id, manoDominante))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "La mano dominante ha sido cambiada", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }
}