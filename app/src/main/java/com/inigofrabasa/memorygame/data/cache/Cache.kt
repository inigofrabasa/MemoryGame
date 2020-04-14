package com.inigofrabasa.memorygame.data.cache

import androidx.lifecycle.MutableLiveData

import com.inigofrabasa.memorygame.data.model.Model

class Cache {

    var gameSelected : MutableLiveData<Model.Game>? = MutableLiveData()

    companion object {
        @Volatile private var instance: Cache? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Cache().also { instance = it }
            }
    }
}