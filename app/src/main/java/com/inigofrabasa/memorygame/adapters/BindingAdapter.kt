package com.inigofrabasa.memorygame.adapters

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.inigofrabasa.memorygame.MemoryGameApplication
import com.inigofrabasa.memorygame.R

import com.inigofrabasa.memorygame.data.model.Model
import com.inigofrabasa.memorygame.manager.GameManager
import com.inigofrabasa.memorygame.utils.COLUMNS_GAMES_NUMBER
import com.inigofrabasa.memorygame.utils.Utils
import com.inigofrabasa.memorygame.widgets.BoardWidget

@BindingAdapter("gameItemName")
fun bindGameItemName(textView: TextView, text: String) {
    textView.text = text
}

@BindingAdapter("listGames")
fun bindListGames(recyclerView: RecyclerView?, data: List<Model.Game>?) {
    if (recyclerView != null && recyclerView.adapter != null) {
        val adapter = recyclerView.adapter as GamesAdapter
        adapter.let {
            adapter.submitList(data)
        }
    }
}

@BindingAdapter("gameManager")
fun bindGameManager(boardWidget: BoardWidget?, gameManager: GameManager?) {
    if (boardWidget != null && gameManager != null) {
        gameManager.buildLogicBoard()
        boardWidget.renderBoard(gameManager)
    }
}

@BindingAdapter("resizeLayout")
fun bindResizeLayout(linearLayout: LinearLayout, resize: Boolean) {

    if(resize){
        val params = linearLayout.layoutParams
        val mScreenWidth = MemoryGameApplication.instance.appContext.resources.displayMetrics.widthPixels - Utils.px(20)

        val simpleMargin = MemoryGameApplication.instance.appContext.resources.getDimensionPixelSize(R.dimen.card_margin)

        var totalMarginHorizontal = 0
        for (row in 0 until COLUMNS_GAMES_NUMBER)
            totalMarginHorizontal += simpleMargin

        val gameCardWidth: Int = (mScreenWidth - totalMarginHorizontal) / COLUMNS_GAMES_NUMBER

        params.height = gameCardWidth
        params.width = gameCardWidth

        linearLayout.layoutParams = params
    }
}

@BindingAdapter("viewVisibility")
fun bindViewVisibility(view: LinearLayout, result: Boolean) {

    if(result)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}