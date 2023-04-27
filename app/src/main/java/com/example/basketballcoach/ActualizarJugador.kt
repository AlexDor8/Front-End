package com.example.basketballcoach

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.basketballcoach.model.*
import com.example.basketballcoach.retrofit.APIService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream


class ActualizarJugador : AppCompatActivity() {

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri!=null) {
            imagen.setImageURI(uri)
        }else {
            Log.i("fotoPerfil", "No seleccionada")
        }
    }

    lateinit var botonImagen :ImageButton
    lateinit var saveImage: ImageButton
    lateinit var imagen: ImageView

    lateinit var nombreActualizarJugador: EditText
    lateinit var apellidoActualizarJugador: EditText
    lateinit var posicionActualizarJugador: EditText
    lateinit var dorsalActualizarJugador: EditText
    lateinit var rolActualizarJugador: EditText
    lateinit var saludActualizarJugador: EditText
    lateinit var alturaActualizarJugador: EditText
    lateinit var manoDominanteActualizarJugador: EditText
    lateinit var buttonActNom: ImageButton
    lateinit var buttonActApe: ImageButton
    lateinit var buttonActPos: ImageButton
    lateinit var buttonActDor: ImageButton
    lateinit var buttonActRol: ImageButton
    lateinit var buttonActSal: ImageButton
    lateinit var buttonActAlt: ImageButton
    lateinit var buttonActManDom: ImageButton

    lateinit var jugador:Jugador

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_jugador)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.iconoJugador -> {
                    //loadFragment(PerfilFragment())

                    val intent: Intent = Intent(this, Profile::class.java);
                    startActivity(intent);
                    true
                }
                R.id.iconoJugadores -> {
                    val intent: Intent = Intent(this, Equipos::class.java);
                    startActivity(intent);
                    //loadFragment(EquipoFragment())
                    true
                }
                R.id.iconoPista -> {
                    //loadFragment(PistaFragment())
                    true
                }
                R.id.iconoCalendario -> {
                    val intent: Intent = Intent(this, Calendar::class.java);
                    startActivity(intent);
                    true
                }
                else -> {
                    println("Clica alguno de los iconos del menu")
                    true
                }
            }
        }

        botonImagen = findViewById(R.id.editFoto)
        botonImagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        imagen = findViewById(R.id.imagenJugadorActualizar)

        nombreActualizarJugador = findViewById(R.id.nombreJugadorActualizar)
        apellidoActualizarJugador = findViewById<EditText>(R.id.apellidoJugadorActualizar)
        posicionActualizarJugador = findViewById<EditText>(R.id.posicionJugadorActualizar)
        dorsalActualizarJugador = findViewById<EditText>(R.id.dorsalActualizar)
        rolActualizarJugador = findViewById<EditText>(R.id.rolActualizar)
        saludActualizarJugador = findViewById<EditText>(R.id.saludActualizar)
        alturaActualizarJugador = findViewById<EditText>(R.id.alturaActualizar)
        manoDominanteActualizarJugador = findViewById<EditText>(R.id.manoDominanteActualizar)

        buttonActNom = findViewById<ImageButton>(R.id.buttonActualizarJugador)
        buttonActApe = findViewById<ImageButton>(R.id.buttonActualizarJugadorApellido)
        buttonActPos = findViewById<ImageButton>(R.id.buttonActualizarJugadorPosicion)
        buttonActDor = findViewById<ImageButton>(R.id.buttonActualizarJugadorDorsal)
        buttonActRol = findViewById<ImageButton>(R.id.buttonActualizarJugadorRol)
        buttonActSal = findViewById<ImageButton>(R.id.buttonActualizarJugadorSalud)
        buttonActAlt = findViewById<ImageButton>(R.id.buttonActualizarJugadorAltura)
        buttonActManDom = findViewById<ImageButton>(R.id.buttonActualizarJugadorManoDominante)

        saveImage = findViewById(R.id.guardarFoto)

        setData()
        cambiarFotoJugador()
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

        Glide.with(imagen.context).load(jugador.foto).into(imagen)
        nombreActualizarJugador.setText(jugador.nombre)
        apellidoActualizarJugador.setText(jugador.apellido)
        posicionActualizarJugador.setText(jugador.posicion)
        dorsalActualizarJugador.setText(jugador.dorsal.toString())
        rolActualizarJugador.setText(jugador.rol)
        saludActualizarJugador.setText(jugador.salud)
        alturaActualizarJugador.setText(jugador.altura.toString())
        manoDominanteActualizarJugador.setText(jugador.manoDominante)
    }

    fun cambiarFotoJugador() {
        saveImage.setOnClickListener {

            var imageFile: File = File("")

            var pathImage:String = "";

            val drawable = imagen.drawable

            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                val filesDir = applicationContext.filesDir
                imageFile = File("$filesDir/$drawable")
                pathImage= imageFile.toString()
                println(pathImage)
                val outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()

            }
            conexionGuardarFoto(jugador.id,  pathImage)
        }
    }

    fun conexionGuardarFoto(id: Int, foto: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editFoto("baloncesto/cFotoJugador", UpdateFoto(id, foto))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "La foto ha sido cambiada", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
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