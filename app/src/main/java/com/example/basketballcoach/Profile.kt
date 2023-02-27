package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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


class Profile : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var profileName = intent.getStringExtra("nombre").toString()
        var profilePassword = intent.getStringExtra("contrasena").toString()

        val nombreA = findViewById<EditText>(R.id.nombreUsuario)
        val emailA = findViewById<EditText>(R.id.emailTexto)
        val fechaNacimiento = findViewById<EditText>(R.id.editTextFecha)
        val botonNombre = findViewById<ImageButton>(R.id.editName)
        val botonEmail = findViewById<ImageButton>(R.id.buttonEmail)
        val botonFecha = findViewById<ImageButton>(R.id.buttonFecha)
        val botonContra = findViewById<Button>(R.id.cambiarContrasena)
        botonImagen = findViewById(R.id.editFoto)
        botonImagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        imagen = findViewById(R.id.fotoPerfil)

        conexion(profileName, profilePassword, nombreA, emailA, fechaNacimiento, botonNombre, botonEmail, botonFecha, botonContra)


    }

    fun conexion(nombre: String, contraseña: String, nombreA:EditText, emailA:EditText, fechaNacimiento:EditText, botonNombre:ImageButton, botonEmail: ImageButton, botonFecha: ImageButton, botonContra: Button) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getUserData("baloncesto/Login", LoginInformation(nombre, contraseña))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    var usuario = respuesta.body()
                    if (usuario != null) {
                        nombreA.setText(usuario.nombre)
                        emailA.setText(usuario.correo)
                        fechaNacimiento.setText((usuario.fechaNacimiento.toString()))
                        botonNombre.setOnClickListener {
                            var nuevoUsuarioNombre = nombreA.text.toString()
                            conexionCambiarNombre(usuario.id, nuevoUsuarioNombre)
                        }
                        botonEmail.setOnClickListener {
                            val emailRegex  = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
                            var nuevoUsuarioEmail = emailA.text.toString()
                            if (!emailRegex.toRegex().matches(nuevoUsuarioEmail)) {
                                emailA.error = "Introduce un email correcto"
                            }else {
                                conexionCambiarEmail(usuario.id, nuevoUsuarioEmail)
                            }
                        }
                        botonFecha.setOnClickListener {
                            var nuevoUsuarioFecha = fechaNacimiento.text.toString()
                            conexionCambiarFecha(usuario.id, nuevoUsuarioFecha)
                        }
                        botonContra.setOnClickListener {
                            val intent: Intent = Intent(this@Profile, ProfilePassword::class.java);
                            intent.putExtra("contrasena", usuario.contraseña)
                            intent.putExtra("id", usuario.id)
                            startActivity(intent);
                        }
                    }

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
            var respuesta = conexion.create(APIService::class.java).editName("baloncesto/cNombre", UpdateUser(id, nombre))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun conexionCambiarEmail(id: Int, correo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editEmail("baloncesto/cEmail", UpdateEmail(id, correo))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun conexionCambiarFecha(id: Int, fechaNacimiento: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editFecha("baloncesto/cFechaNacimiento", UpdateFecha(id, fechaNacimiento))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }
}