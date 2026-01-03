package com.lucascamarero.didaktikapp.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.data.db.models.PhotoItem

class FinalGameViewModel : ViewModel() {

    val photos: List<PhotoItem> = listOf(
        PhotoItem(1, 1, R.drawable.premio11),
        PhotoItem(2, 1, R.drawable.premio12),
        PhotoItem(3, 2, R.drawable.premio21),
        PhotoItem(4, 2, R.drawable.premio22),
        PhotoItem(5, 3, R.drawable.premio31),
        PhotoItem(6, 3, R.drawable.premio32),
        PhotoItem(7, 4, R.drawable.premio41),
        PhotoItem(8, 4, R.drawable.premio42),
        PhotoItem(9, 5, R.drawable.premio51),
        PhotoItem(10, 5, R.drawable.premio52),
        PhotoItem(11, 6, R.drawable.premio61),
        PhotoItem(12, 6, R.drawable.premio62),
        PhotoItem(13, 7, R.drawable.premio71),
        PhotoItem(14, 7, R.drawable.premio72),
    ).shuffled()

    var firstSelected by mutableStateOf<PhotoItem?>(null)
        private set

    var matched by mutableStateOf(setOf<Int>())
        private set

    var message by mutableStateOf("")
        private set

    fun onPhotoClicked(photo: PhotoItem): Boolean {
        if (matched.contains(photo.id)) return false

        if (firstSelected == null) {
            firstSelected = photo
            message = ""
            return false
        }

        if (firstSelected!!.id != photo.id) {
            if (firstSelected!!.pairId == photo.pairId) {
                matched = matched + firstSelected!!.id + photo.id
                message = "✅ Asociación correcta"
            } else {
                message = "❌ Asociación incorrecta"
            }
            firstSelected = null
        }

        return matched.size == 14
    }

    fun isSelected(photo: PhotoItem): Boolean =
        firstSelected?.id == photo.id

    fun isMatched(photo: PhotoItem): Boolean =
        matched.contains(photo.id)
}