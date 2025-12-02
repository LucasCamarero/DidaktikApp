package com.lucascamarero.didaktikapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterViewModel: ViewModel() {

    // LiveData mutable interna que almacena el valor del contador
    private val _counter = MutableLiveData<Int>()
    // LiveData pública y de solo lectura para exponer el contador
    val count: LiveData<Int> = _counter

    // Incrementa el contador en 1
    fun upCount() {
        _counter.value = (_counter.value ?: 0) + 1
    }

    // Decrementa el contador en 1
    fun downCount() {
        _counter.value = (_counter.value ?: 0) - 1
    }

    // Inicializa el valor del contador a 0 cuando se crea el ViewModel
    init {
        _counter.value = 0
    }
}