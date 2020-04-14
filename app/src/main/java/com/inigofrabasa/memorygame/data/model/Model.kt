package com.inigofrabasa.memorygame.data.model

import com.google.gson.annotations.SerializedName

object Model {
    data class Games(
        @SerializedName("games") val games : List<Game>
    )

    data class Game(
        @SerializedName("id") val id : String,
        @SerializedName("name") val name : String,
        @SerializedName("columns") val columns : Int,
        @SerializedName("rows") val rows : Int
        )

    data class MatchedCards(
         var firstId : Int,
         var secondId : Int
    )
}