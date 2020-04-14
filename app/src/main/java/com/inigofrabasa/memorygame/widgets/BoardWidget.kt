package com.inigofrabasa.memorygame.widgets

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.inigofrabasa.memorygame.R
import com.inigofrabasa.memorygame.manager.GameManager
import com.inigofrabasa.memorygame.utils.FACTOR
import com.inigofrabasa.memorygame.utils.Utils
import com.inigofrabasa.memorygame.utils.UtilsJava

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class BoardWidget @JvmOverloads constructor(context: Context?, attributeSet: AttributeSet? = null)
    : LinearLayout(context, attributeSet) {

    private var gameManager : GameManager? = null

    private var widgetJob = Job()
    private var cScope = CoroutineScope(widgetJob + Dispatchers.IO)

    private val rowLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    private var cardLayoutParams: LayoutParams? = null

    private val screenWidth: Int
    private val screenHeight: Int

    private val mViewReference: MutableMap<Int, CardWidget>
    private val turnedUp: MutableList<Int> = mutableListOf()

    private var avoidMove = false
    private var widthSize = 0
    private var heightSize = 0

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER

        val margin = resources.getDimensionPixelSize(R.dimen.margin_top)
        val padding = resources.getDimensionPixelSize(R.dimen.board_padding)

        screenHeight = resources.displayMetrics.heightPixels - margin - padding * 2
        screenWidth = resources.displayMetrics.widthPixels - padding * 2 - Utils.px(20)

        mViewReference = HashMap()
        clipToPadding = false
    }

    fun renderBoard(inGameManager : GameManager) {
        this.gameManager = inGameManager

        var simpleMargin = resources.getDimensionPixelSize(R.dimen.card_margin)
        val density = resources.displayMetrics.density
        simpleMargin = (1 * density).toInt().coerceAtLeast( (simpleMargin - 2 * density).toInt())

        var totalMarginHorizontal = 0
        for (row in 0 until this.gameManager?.gameModel!!.columns) {
            totalMarginHorizontal += simpleMargin * 2
        }

        var totalMarginVertical = 0
        for (row in 0 until this.gameManager?.gameModel!!.rows) {
            totalMarginVertical += simpleMargin * 2
        }

        val cardWidthS1: Int = (screenWidth - totalMarginHorizontal) / this.gameManager?.gameModel!!.columns
        val cardHeightS1 = (cardWidthS1 * FACTOR).toInt()

        widthSize = cardWidthS1
        heightSize = cardHeightS1

        if(totalMarginVertical + cardHeightS1 * this.gameManager?.gameModel!!.rows > screenHeight){

            val cardHeightS2: Int = (screenHeight - totalMarginVertical) / this.gameManager?.gameModel!!.rows
            val cardWidthS2 = (cardHeightS2 / FACTOR).toInt()

            widthSize = cardWidthS2
            heightSize = cardHeightS2
        }

        cardLayoutParams = LayoutParams(widthSize ,heightSize)
        cardLayoutParams!!.setMargins(simpleMargin, simpleMargin, simpleMargin, simpleMargin)

        for (row in 0 until this.gameManager?.gameModel!!.rows) {

            val linearLayout = LinearLayout(context)

            linearLayout.orientation = HORIZONTAL
            linearLayout.gravity = Gravity.CENTER

            for (card in 0 until this.gameManager?.gameModel!!.columns) {
                addCard(row * this.gameManager?.gameModel!!.columns + card, linearLayout)
            }

            addView(linearLayout, rowLayoutParams)
            linearLayout.clipChildren = false
        }

        clipChildren = false
    }

    private fun addCard(id: Int, parent: ViewGroup) {
        val cardWidget: CardWidget = CardWidget.inflateView(context)
        cardWidget.layoutParams = cardLayoutParams

        parent.addView(cardWidget)
        parent.clipChildren = false
        mViewReference[id] = cardWidget

        cScope.launch {
            try {
                val bitmapResult = this@BoardWidget.gameManager?.run {
                    UtilsJava.loadCardBitmap(id, widthSize, heightSize)
                }
                cardWidget.setCardImage(bitmapResult)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        cardWidget.setOnClickListener {

            if (!avoidMove && cardWidget.isTurnedDown) {
                cardWidget.turnUp()
                turnedUp.add(id)

                if (turnedUp.size == 2)
                    avoidMove = true

                gameManager!!.onTurnedCard(id)
            }
        }
    }

    fun turnDownAllCards() {
        for (id in turnedUp) {
            mViewReference[id]!!.turnDown()
        }
        turnedUp.clear()
        avoidMove = false
    }

    fun deletePairOfCards(id1: Int, id2: Int) {

        animateDisappear(mViewReference[id1])
        animateDisappear(mViewReference[id2])

        turnedUp.clear()
        avoidMove = false
    }

    private fun animateDisappear(v: CardWidget?) {
        val animator = ObjectAnimator.ofFloat(v, "alpha", 0f)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                v?.setLayerType(View.LAYER_TYPE_NONE, null)
                v?.visibility = View.INVISIBLE
            }
        })
        animator.duration = 100
        v?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        animator.start()
    }
}