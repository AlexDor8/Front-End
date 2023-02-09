package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.basketballcoach.model.UpdateFecha
import com.example.basketballcoach.model.UpdatePassword
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfilePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_password)

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