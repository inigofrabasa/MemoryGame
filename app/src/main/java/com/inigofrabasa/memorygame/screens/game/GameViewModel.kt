package com.inigofrabasa.memorygame.screens.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.inigofrabasa.memorygame.data.model.Model
import com.inigofrabasa.memorygame.manager.GameManager

class GameViewModel : ViewModel() {

    val gameManager : GameManager
        get() = GameManager.getInstance()

    val turnDownAllCards : MutableLiveData<Boolean>
        get() = GameManager.getInstance().turnDownAllCardsLiveData

    val deleteMatchedCards : MutableLiveData<Model.MatchedCards>
        get() = GameManager.getInstance().deleteMatchedCardsLiveData

    val winGame : MutableLiveData<Boolean>
        get() = GameManager.getInstance().winGameLiveData

    fun removeObserverTurnDownAllCards(){
        GameManager.getInstance().removeObserverTurnDownAllCards()
    }

    fun removeObserverDeleteMatchedCards(){
        GameManager.getInstance().removeObserverDeleteMatchedCards()
    }

    fun removeObserverWinGame(){
        GameManager.getInstance().removeObserverWinGame()
    }
}