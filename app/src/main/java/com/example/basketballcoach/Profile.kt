package com.example.basketballcoach

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.number.NumberFormatter.with
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.example.basketballcoach.model.*
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream


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

        saveImage = findViewById(R.id.guardarFoto)



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
                        emailA.setText(usuario.email)
                        fechaNacimiento.setText((usuario.fechaNacimiento))
                        //MyAppGlideModule.with(imagen.context).load(usuario.foto).placeholder(R.drawable.usuario).fitCenter().into(imagen)
                        with(imagen.context).load(usuario.foto).into(imagen)
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
                        saveImage.setOnClickListener {
                            usuario.foto = imagen.toString()

                            var imageFile:File = File("")

                            val drawable = imagen.drawable

                            if (drawable is BitmapDrawable) {
                                val bitmap = drawable.bitmap
                                val filesDir = applicationContext.filesDir
                                imageFile = File(filesDir, "image.png")
                                val outputStream = FileOutputStream(imageFile)
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                outputStream.close()

                            }
                            val requestFile: RequestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val fotoUpdate: MultipartBody.Part = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                            val idUsuario: RequestBody =usuario.id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                            conexionGuardarFoto(idUsuario,  fotoUpdate)
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

    fun conexionGuardarFoto(id: RequestBody, imagen: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editFoto("baloncesto/cFoto", id, imagen)
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

