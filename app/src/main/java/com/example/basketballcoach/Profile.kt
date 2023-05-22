package com.example.basketballcoach

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.number.NumberFormatter.with
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.example.basketballcoach.model.*
import com.example.basketballcoach.retrofit.APIService
import com.google.android.material.bottomnavigation.BottomNavigationView
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
import java.text.SimpleDateFormat
import java.util.*


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

    lateinit var bottomNav : BottomNavigationView

    lateinit var botonCambiarFecha: ImageView

    lateinit var fechaNacimiento: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.iconoJugador -> {
                    //loadFragment(PerfilFragment())
                    startActivity(intent)
                    true
                }
                R.id.iconoJugadores -> {
                    val intent: Intent = Intent(this@Profile, Equipos::class.java);
                    startActivity(intent);
                    //loadFragment(EquipoFragment())
                    true
                }
                R.id.iconoPista -> {
                    val intent: Intent = Intent(this@Profile, Pizarra::class.java);
                    startActivity(intent);
                    true
                }
                R.id.iconoCalendario -> {
                    val intent: Intent = Intent(this@Profile, Calendar::class.java);
                    startActivity(intent);
                    true
                }
                else -> {
                    println("Clica alguno de los iconos del menu")
                    true
                }
            }

        }



        val nombreA = findViewById<EditText>(R.id.nombreUsuario)
        val emailA = findViewById<EditText>(R.id.emailTexto)
        fechaNacimiento = findViewById(R.id.editTextFecha)
        val botonNombre = findViewById<ImageButton>(R.id.editName)
        val botonEmail = findViewById<ImageButton>(R.id.buttonEmail)
        val botonFecha = findViewById<ImageButton>(R.id.buttonFecha)
        val botonContra = findViewById<Button>(R.id.cambiarContrasena)

        botonCambiarFecha = findViewById(R.id.elegirFechaProfile)

        val myCalendar = java.util.Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(java.util.Calendar.YEAR, year)
            myCalendar.set(java.util.Calendar.MONTH, month)
            myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        botonCambiarFecha.setOnClickListener {
            DatePickerDialog(this, datePicker, myCalendar.get(java.util.Calendar.YEAR), myCalendar.get(
                java.util.Calendar.MONTH), myCalendar.get(java.util.Calendar.DAY_OF_MONTH)).show()
        }

        botonImagen = findViewById(R.id.editFoto)
        botonImagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        imagen = findViewById(R.id.fotoPerfil)




        conexion(Globals.loginInformation.nombre, Globals.loginInformation.contraseña, nombreA, emailA, fechaNacimiento, botonNombre, botonEmail, botonFecha, botonContra)

        saveImage = findViewById(R.id.guardarFoto)



    }

    fun updateLable(myCalendar : java.util.Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        fechaNacimiento.setText(sdf.format(myCalendar.time))
    }



    fun conexion(nombre: String, contraseña: String, nombreA:EditText, emailA:EditText, fechaNacimiento:TextView, botonNombre:ImageButton, botonEmail: ImageButton, botonFecha: ImageButton, botonContra: Button) {
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
                        Globals.usuario = usuario
                        nombreA.setText(usuario.nombre)
                        emailA.setText(usuario.email)
                        fechaNacimiento.text = (usuario.fechaNacimiento)
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

                            var imageFile:File = File("")

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
                            conexionGuardarFoto(usuario.id,  pathImage)
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
                    Toast.makeText(applicationContext, "El nombre ha sido cambiado", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(applicationContext, "El email ha sido cambiado", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(applicationContext, "La fecha ha sido cambiada", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun conexionGuardarFoto(id: Int, imagen: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editFoto("baloncesto/cFoto", UpdateFoto(id, imagen))
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
}

