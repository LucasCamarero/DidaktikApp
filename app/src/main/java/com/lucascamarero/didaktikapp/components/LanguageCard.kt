package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lucascamarero.didaktikapp.R

@Composable
fun LanguageCard(
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                // Euskera

                Text("Hizkuntza", style = MaterialTheme.typography.labelMedium)

                Image(
                    painter = painterResource(id = R.drawable.basque_flag),
                    contentDescription = "Euskara",
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { onLanguageSelected("eu") }
                )

                Spacer(modifier = Modifier.padding(vertical = 20.dp))

                // Castellano
                Text("Idioma", style = MaterialTheme.typography.labelMedium)

                Image(
                    painter = painterResource(id = R.drawable.spain_flag),
                    contentDescription = "Castellano",
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { onLanguageSelected("es") }
                )

                Spacer(modifier = Modifier.padding(vertical = 20.dp))

                // Inglés
                Text("Language", style = MaterialTheme.typography.labelMedium)

                Image(
                    painter = painterResource(id = R.drawable.england_flag),
                    contentDescription = "English",
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { onLanguageSelected("en") }
                )
            }
        }
    }
}