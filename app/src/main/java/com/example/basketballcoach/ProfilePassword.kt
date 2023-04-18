package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.basketballcoach.model.UpdateFecha
import com.example.basketballcoach.model.UpdatePassword
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

class ProfilePassword : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_password)

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

        validacionContra()
    }

    fun validacionContra() {
        val contraAntigua = findViewById<EditText>(R.id.antiguaContraseña)
        val nuevaContrasena = findViewById<EditText>(R.id.nuevaContrasena)
        val repetirContrasena = findViewById<EditText>(R.id.rrepiteNuevaContrasena)
        val boton = findViewById<Button>(R.id.cambiarContra)
        boton.setOnClickListener {
            var profileId:Int = intent.getIntExtra("id", 0)
            var contrasena = contraAntigua.text.toString()
            var nuevaContraseña = nuevaContrasena.text.toString()
            var repContra = repetirContrasena.text.toString()

            compContrasena(contrasena, profileId, nuevaContraseña, repContra, nuevaContrasena, contraAntigua)
        }

    }

    fun conexionCambiarContraseña(id: Int, nuevaContraseña: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).editContrasena("baloncesto/cContrasena", UpdatePassword(id, nuevaContraseña))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "La contraseña ha sido cambiada", Toast.LENGTH_LONG).show();
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }

    fun compContrasena(contrasena: String, profileId:Int, nuevaContraseña: String, repContra: String, nuevaContrasena:EditText, contraAntigua:EditText) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).esContrasena("baloncesto/compContrasena", UpdatePassword(profileId, contrasena))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    if(respuesta.body() == true) {
                            if (nuevaContraseña == repContra) {
                                if (nuevaContraseña.length<8) {
                                    nuevaContrasena.error = "La contraseña tiene que tener mínimo 8 caracteres"
                                }else {
                                    conexionCambiarContraseña(profileId, nuevaContraseña)
                                }
                            }else {
                                nuevaContrasena.error = "Las contraseñas no coinciden"
                            }
                    }else {
                        contraAntigua.error = "La contraseña no es correcta"
                    }
                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }
}