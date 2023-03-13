package com.example.basketballcoach

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.basketballcoach.model.Globals
import com.example.basketballcoach.model.UpdateFoto
import com.example.basketballcoach.retrofit.APIService
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

        actualizarEquipoNombre = findViewById<EditText>(R.id.nombreEquipoActualizar)
        imagenEquipoActualizar = findViewById<ImageView>(R.id.imagenEquipoActualizar)
        botonActualizarNombre = findViewById<ImageButton>(R.id.nombreEquipoActualizar)
        botonImagenActualizar = findViewById(R.id.editFotoEquipo)
        botonImagenGuardar = findViewById(R.id.guardarFotoEquipo)
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
            //conexionGuardarFoto(,  pathImage)


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