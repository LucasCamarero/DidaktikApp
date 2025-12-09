package com.lucascamarero.didaktikapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.lucascamarero.didaktikapp.data.db.BarakaldoDatabase
import com.lucascamarero.didaktikapp.data.db.entities.ActividadEntity
import com.lucascamarero.didaktikapp.data.db.entities.ImagenEntity
import com.lucascamarero.didaktikapp.data.db.entities.LugarEntity
import com.lucascamarero.didaktikapp.data.db.entities.PersonaEntity
import com.lucascamarero.didaktikapp.data.db.entities.ProgresoUsuarioEntity
import com.lucascamarero.didaktikapp.data.db.entities.UsuarioEntity
import com.lucascamarero.didaktikapp.ui.theme.DidaktikAppTheme
import com.lucascamarero.didaktikapp.screens.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // 1. INYECTAMOS LA BASE DE DATOS PARA PODER ESCRIBIR DATOS DE PRUEBA
    @Inject
    lateinit var database: BarakaldoDatabase

    @RequiresApi(Build.VERSION_CODES.DONUT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2. LLAMAMOS A LA FUNCIÓN DE CARGA DE DATOS (SOLO PARA PRUEBAS)
        insertarDatosDePrueba()

        setContent {
            DidaktikAppTheme(dynamicColor = false) {
                Surface {
                    // Tu lógica original de Splash se mantiene intacta
                    var showSplash by remember { mutableStateOf(true) }

                    if (showSplash) {
                        SplashScreen(onTimeout = { showSplash = false })
                    } else {
                        // Llama al gestor de pantallas y navegación
                        ScreenManager()
                    }
                }
            }
        }
    }

    // --- FUNCIÓN PARA POBLAR LA BD (Dummy Data) ---
    private fun insertarDatosDePrueba() {
        lifecycleScope.launch {
            try {
                // Paso A: Insertar Imágenes necesarias (Para Lugar y Premios)
                // IDs 1, 2 y 3.
                val imgPrincipal = ImagenEntity(1, "ruta_default", "Portada", "Principal")
                val imgAntigua = ImagenEntity(2, "act1_premio1", "Foto 1920", "Premio Antiguo")
                val imgActual = ImagenEntity(3, "act1_premio2", "Foto 2025", "Premio Actual")

                // Nota: Usamos listas porque tus DAOs esperan listas en los inserts masivos
                database.contenidoDao().insertImagenes(listOf(imgPrincipal, imgAntigua, imgActual))

                // Paso B: Insertar Persona y Usuario (El alumno)
                // Persona ID 1 -> Usuario ID 1
                val persona = PersonaEntity(1, "alumno1", "hash_dummy", "Usuario")
                // El DAO insertPersona devuelve Long, no necesitamos capturarlo aquí para el test
                try { database.personaDao().insertPersona(persona) } catch (e: Exception) {}

                val usuario = UsuarioEntity(1, "Alumno de Prueba")
                database.personaDao().insertUsuario(usuario)

                // Paso C: Insertar Lugar (Ermita)
                // Lugar ID 1, FK Imagen 1
                val lugar = LugarEntity(1, "Ermita Santa Águeda", "Lugar histórico...", "0.0,0.0", 1)
                database.contenidoDao().insertLugares(listOf(lugar))

                // Paso D: Insertar Actividad (Drag & Drop)
                // Actividad ID 1, Lugar 1, Premios 2 y 3
                val actividad = ActividadEntity(
                    actividad_id = 1,
                    tipo_actividad = "DragAndDrop",
                    archivo_actividad = "config.json",
                    lugar_fk = 1,
                    premio_antigua_fk = 2,
                    premio_actual_fk = 3
                )
                database.contenidoDao().insertActividades(listOf(actividad))

                // Paso E: Insertar el registro de Progreso VACÍO
                // Esto es vital para que el UPDATE del juego tenga algo que actualizar.
                val progresoInicial = ProgresoUsuarioEntity(
                    actividad_fk = 1,
                    persona_fk = 1,
                    completada = 0, // <--- EMPIEZA SIN COMPLETAR
                    fecha_completado = null
                )
                database.progresoDao().insertInitialProgreso(listOf(progresoInicial))

                Log.d("DB_TEST", "✅ Datos de prueba insertados correctamente.")

            } catch (e: Exception) {
                // Es normal que falle si ya existen (ConstraintViolation), por eso el Log.e
                Log.e("DB_TEST", "⚠️ Aviso: Probablemente los datos ya existen o hubo error: ${e.message}")
            }
        }
    }
}