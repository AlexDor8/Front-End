package com.example.basketballcoach

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.basketballcoach.model.Equipo
import com.example.basketballcoach.model.Globals
import com.example.basketballcoach.model.UpdateFoto
import com.example.basketballcoach.model.UpdateUser
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

class ActualizarEquipo : AppCompatActivity() {

    lateinit var actualizarEquipoNombre:EditText
    lateinit var imagenEquipoActualizar:ImageView
    lateinit var botonActualizarNombre:ImageButton
    lateinit var botonImagenActualizar:ImageButton
    lateinit var botonImagenGuardar: ImageButton

    lateinit var bottomNav : BottomNavigationView


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri!=null) {
            imagenEquipoActualizar.setImageURI(uri)
        }else {
            Log.i("fotoPerfil", "No seleccionada")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_equipo)

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

        actualizarEquipoNombre = findViewById<EditText>(R.id.nombreEquipoActualizar)
        imagenEquipoActualizar = findViewById<ImageView>(R.id.imagenEquipoActualizar)
        botonActualizarNombre = findViewById<ImageButton>(R.id.buttonActualizarNombreEquipo)
        botonImagenActualizar = findViewById(R.id.editFotoEquipo)
        botonImagenGuardar = findViewById(R.id.guardarFotoEquipo)

        actualizarEquipoNombre.setText(Globals.equipo.nombre)
        imagenEquipoActualizar.setImageURI(Uri.parse(Globals.equipo.foto))

        botonActualizarNombre.setOnClickListener {
            var nuevoEquipoNombre = actualizarEquipoNombre.text.toString()
            conexionCambiarNombre(Globals.equipo.id, nuevoEquipoNombre)
        }

        cambiarFotoEuipo()
    }



    fun cambiarFotoEuipo() {
        botonImagenActualizar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        botonImagenGuardar.setOnClickListener {

            var imageFile: File = File("")

            var pathImage:String = "";

            val drawable = imagenEquipoActualizar.drawable

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

            conexionGuardarFoto(Globals.equipo.id,  pathImage)


        }
    }

    fun conexionGuardarFoto(id: Int, imagen: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editFoto("baloncesto/cFotoEquipo", UpdateFoto(id, imagen))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "La foto ha sido cambiada!", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun conexionCambiarNombre(id: Int, nombre: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editName("baloncesto/cNombreEquipo", UpdateUser(id, nombre))
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
}