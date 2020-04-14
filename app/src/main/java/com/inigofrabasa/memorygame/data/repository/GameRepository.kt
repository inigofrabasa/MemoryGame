package com.inigofrabasa.memorygame.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

import com.inigofrabasa.memorygame.data.cache.Cache
import com.inigofrabasa.memorygame.data.model.Model
import com.inigofrabasa.memorygame.utils.Utils.readFileFromAssets

class GameRepository {

    fun getGameTypes() : Model.Games{
        val data: String = readFileFromAssets("games.json")

        val objectJson = Gson()

        return objectJson.fromJson(
            data,
            Model.Games::class.java
        )
    }

    fun cacheGameSelected(game : Model.Game){
        Cache.getInstance().gameSelected?.run{
            value = game
        }
    }

    fun fetchCacheGameSelected() : MutableLiveData<Model.Game>{
        return Cache.getInstance().gameSelected!!
    }

    companion object {
        @Volatile private var instance: GameRepository? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: GameRepository().also { instance = it }
            }
    }
}