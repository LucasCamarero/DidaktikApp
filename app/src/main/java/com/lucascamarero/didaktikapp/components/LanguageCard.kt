package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lucascamarero.didaktikapp.R

/**
 * Composable que muestra un selector de idioma dentro de una tarjeta.
 *
 * El layout del selector se adapta dinámicamente a la orientación
 * de la pantalla:
 * - En orientación vertical, los idiomas se muestran en columna.
 * - En orientación horizontal, los idiomas se muestran en fila.
 *
 * Cada idioma se representa mediante un texto descriptivo y
 * una imagen clicable (bandera).
 *
 * @param isLandscape Indica si el dispositivo se encuentra en
 * orientación horizontal.
 * @param onLanguageSelected Callback que se invoca al seleccionar
 * un idioma. Recibe el código del idioma seleccionado.
 */
@Composable
fun LanguageCard(
    isLandscape: Boolean,
    onLanguageSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageItem(
                    title = "Hizkuntza",
                    flagRes = R.drawable.basque_flag,
                    contentDescription = "Euskara"
                ) { onLanguageSelected("eu") }

                LanguageItem(
                    title = "Idioma",
                    flagRes = R.drawable.spain_flag,
                    contentDescription = "Castellano"
                ) { onLanguageSelected("es") }

                LanguageItem(
                    title = "Language",
                    flagRes = R.drawable.england_flag,
                    contentDescription = "English"
                ) { onLanguageSelected("en") }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    LanguageItem(
                        title = "Hizkuntza",
                        flagRes = R.drawable.basque_flag,
                        contentDescription = "Euskara"
                    ) { onLanguageSelected("eu") }

                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    LanguageItem(
                        title = "Idioma",
                        flagRes = R.drawable.spain_flag,
                        contentDescription = "Castellano"
                    ) { onLanguageSelected("es") }

                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    LanguageItem(
                        title = "Language",
                        flagRes = R.drawable.england_flag,
                        contentDescription = "English"
                    ) { onLanguageSelected("en") }
                }
            }
        }
    }
}

/**
 * Composable que representa un único idioma dentro del selector.
 *
 * Muestra un título descriptivo y una imagen asociada (bandera),
 * que actúa como elemento interactivo.
 *
 * @param title Texto que identifica el idioma.
 * @param flagRes Recurso drawable de la bandera asociada al idioma.
 * @param contentDescription Descripción accesible de la imagen.
 * @param onClick Acción que se ejecuta al pulsar sobre el idioma.
 */
@Composable
private fun LanguageItem(
    title: String,
    flagRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )

        Image(
            painter = painterResource(id = flagRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(80.dp)
                .clickable(onClick = onClick)
        )
    }
}