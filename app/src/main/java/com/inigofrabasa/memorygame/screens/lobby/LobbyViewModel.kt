package com.inigofrabasa.memorygame.screens.lobby

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.inigofrabasa.memorygame.data.model.Model
import com.inigofrabasa.memorygame.data.repository.GameRepository
import com.inigofrabasa.memorygame.manager.GameManager

class LobbyViewModel : ViewModel(){

    var gamesLiveData : MutableLiveData<List<Model.Game>> = MutableLiveData()

    fun getGames(){
        val games = GameRepository.getInstance().getGameTypes()
        games.run {
            gamesLiveData.value = this.games
        }
    }

    fun gameSelected(position : Int){
        GameRepository.getInstance().cacheGameSelected(gamesLiveData.value?.get(position)!!)
        GameManager.getInstance().gameModel = GameRepository.getInstance().fetchCacheGameSelected().value!!
    }
}