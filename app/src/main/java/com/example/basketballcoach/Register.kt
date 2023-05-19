package com.example.basketballcoach

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.basketballcoach.model.Usuario
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDate.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar


class Register : AppCompatActivity() {

    lateinit var botonFecha: ImageView
    lateinit var fechaNacimiento: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        postRegisterUsuario()
    }

    fun postRegisterUsuario() {
        val registerNombreUsuario = findViewById<EditText>(R.id.nombreUsuarioRegister)
        val password = findViewById<EditText>(R.id.passwordRegister)
        val repiteContrasena = findViewById<EditText>(R.id.repiteContraseña)
        val email = findViewById<EditText>(R.id.email)
        fechaNacimiento = findViewById<EditText>(R.id.fechaNacimiento)
        botonFecha = findViewById(R.id.elegirFecha)

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        botonFecha.setOnClickListener {
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val registrarse: Button = findViewById(R.id.botonRegister)


        registrarse.setOnClickListener {
                validacionDatos(registerNombreUsuario, password, repiteContrasena, email, fechaNacimiento)
        }


    }

    fun updateLable(myCalendar : Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        fechaNacimiento.setText(sdf.format(myCalendar.time))
    }

    fun validacionDatos(registerNombreUsuario:EditText, password:EditText, repiteContrasena:EditText, email:EditText, fechaNacimiento:EditText) {
        val nombreUsuario = registerNombreUsuario.text.toString()
        val contrasena = password.text.toString()
        val repitePassword = repiteContrasena.text.toString()
        val mail = email.text.toString()
        val nacimiento = fechaNacimiento.text.toString()
        //val formatter = SimpleDateFormat("yyyy-MM-dd")

        //val fecha = formatter.parse(nacimiento)



        //val date = parse(nacimiento, DateTimeFormatter.ISO_DATE)
        val emailRegex  = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

        if (nombreUsuario.isEmpty() || contrasena.isEmpty() || repitePassword.isEmpty() || mail.isEmpty() || nacimiento.isEmpty()) {
            Toast.makeText(applicationContext, "Los campos están vacíos", Toast.LENGTH_LONG).show();
        }else {
            var userExiste = usuarioExiste(registerNombreUsuario, nombreUsuario, contrasena, mail, nacimiento)
            println("Este usuario existe?$userExiste")
            if (contrasena != repitePassword) {
                repiteContrasena.error = "Las contraseñas no coinciden"
            }else if (contrasena.length<8) {
                password.error = "La contraseña tiene que tenir mínimo 8 caracteres"

            }else if (!emailRegex.toRegex().matches(mail)){
                email.error = "Introduce un email correcto"
            }else if(userExiste){
                registerNombreUsuario.error = "Este usuario ya existe"
            }else {
                conexionRegistro(nombreUsuario, contrasena, mail, nacimiento)
            }

        }
    }

    fun conexionRegistro(nombreUsuario:String, contrasena:String, mail:String, nacimiento:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(
                    GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java)
                .postRegister("baloncesto/CrearUsuario", Usuario(0, nombreUsuario, contrasena, mail, nacimiento,"") )
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "Te has registrado con éxito!", Toast.LENGTH_LONG).show();
                    val intent: Intent = Intent(this@Register, MainActivity::class.java);
                    startActivity(intent);
                }else {
                    respuesta.errorBody()?.string()
                }
            }

        }
    }

    fun usuarioExiste(nombre: EditText, nombreUsuario:String, contrasena:String, mail:String, nacimiento: String) :Boolean {
        var usuarioExiste = false
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(
                    GsonConverterFactory.create()
                ).client(client).build()
            var respuesta = conexion.create(APIService::class.java)
                .userExist("baloncesto/exNombre", Usuario(0, nombreUsuario, contrasena, mail, nacimiento, ""))
            withContext(Dispatchers.Main) {
                if(respuesta.isSuccessful) {
                    if (respuesta.body() == true) {
                        usuarioExiste = true;
                        nombre.error = "Este usuario ya existe"
                        println("Este usuario existe: $usuarioExiste")
                    }
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
        return usuarioExiste
    }





}