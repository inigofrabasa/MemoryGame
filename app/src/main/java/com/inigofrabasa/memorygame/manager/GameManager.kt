package com.inigofrabasa.memorygame.manager

import android.os.Handler
import androidx.lifecycle.MutableLiveData

import com.inigofrabasa.memorygame.data.model.Model
import com.inigofrabasa.memorygame.utils.DELAY_TIME
import com.inigofrabasa.memorygame.utils.RESET_NUMBER
import com.inigofrabasa.memorygame.utils.URI_DRAWABLE
import java.util.Collections.shuffle

class GameManager {

    lateinit var gameModel : Model.Game

    var turnDownAllCardsLiveData : MutableLiveData<Boolean>                 = MutableLiveData()
    var deleteMatchedCardsLiveData : MutableLiveData<Model.MatchedCards>    = MutableLiveData()
    var winGameLiveData : MutableLiveData<Boolean>                          = MutableLiveData()

    var match = mutableMapOf<Int, Int>()
    var cardsPath = mutableMapOf<Int, String>()

    var cardsImagePaths: MutableList<String>? = null

    private var cardsInGame = RESET_NUMBER
    private var cardId = RESET_NUMBER

    init {
        loadImagesNames()
    }

    private fun areCardsMatch(firstId: Int, secondId: Int): Boolean {
        val integer = match[firstId] ?: return false
        return integer == secondId
    }

    private fun loadImagesNames(){
        cardsImagePaths = mutableListOf()
        for (i in 1..10)
            cardsImagePaths!!.add(URI_DRAWABLE + String.format("card_%d", i))
    }

    fun buildLogicBoard() {

        cardsInGame = gameModel.rows * gameModel.columns
        val ids: MutableList<Int?> = ArrayList()

        for (i in 0 until gameModel.rows * gameModel.columns)
            ids.add(i)

        ids.shuffle()

        val tileImageUrls: List<String>? = cardsImagePaths
        shuffle(tileImageUrls!!)

        match = mutableMapOf()
        cardsPath = HashMap()

        var j = 0
        var i = 0

        while (i < ids.size) {
            if (i + 1 < ids.size) {
                match[ids[i]!!] = ids[i + 1]!!
                match[ids[i + 1]!!] = ids[i]!!

                cardsPath[ids[i]!!] = tileImageUrls[j]
                cardsPath[ids[i + 1]!!] = tileImageUrls[j]

                i++
                j++
            }
            i++
        }
    }

    fun onTurnedCard(cardId : Int){
        val id: Int = cardId
        if (this.cardId == RESET_NUMBER) {
            this.cardId = id
        } else {
            if (areCardsMatch(this.cardId, id)) {
                Handler().postDelayed({
                    deleteMatchedCardsLiveData.value = Model.MatchedCards(this.cardId, id)
                    this.cardId = RESET_NUMBER
                }, DELAY_TIME)
                cardsInGame -= 2
                if (cardsInGame == 0) {
                    Handler().postDelayed({
                        cardsInGame = RESET_NUMBER
                        winGameLiveData.value = true
                    }, DELAY_TIME)
                }
            } else {
                Handler().postDelayed({
                    turnDownAllCardsLiveData.value = true
                    this.cardId = RESET_NUMBER
                }, DELAY_TIME)
            }
        }
    }

    fun removeObserverTurnDownAllCards(){
        turnDownAllCardsLiveData = MutableLiveData()
    }

    fun removeObserverDeleteMatchedCards(){
        deleteMatchedCardsLiveData = MutableLiveData()
    }

    fun removeObserverWinGame(){
        winGameLiveData = MutableLiveData()
    }

    companion object {
        @Volatile private var instance: GameManager? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: GameManager().also { instance = it }
            }
    }
}