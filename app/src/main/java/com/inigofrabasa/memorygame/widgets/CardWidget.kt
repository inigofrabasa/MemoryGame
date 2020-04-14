package com.inigofrabasa.memorygame.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.inigofrabasa.memorygame.MemoryGameApplication
import com.inigofrabasa.memorygame.R
import com.inigofrabasa.memorygame.animations.TurnAnimation
import com.inigofrabasa.memorygame.databinding.CardLayoutBinding

class CardWidget @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    FrameLayout(context!!, attrs) {

    private var backCardImage: ImageView? = null
    private var frontCardImage: ImageView? = null

    var isTurnedDown = true
        private set

    fun setCardImage(bitmap: Bitmap?) {
        this.frontCardImage!!.background = BitmapDrawable(MemoryGameApplication.instance.resources, bitmap)
    }

    fun turnUp() {
        isTurnedDown = false
        turnCard()
    }

    fun turnDown() {
        isTurnedDown = true
        turnCard()
    }

    companion object {

        fun inflateView(context: Context?): CardWidget {
            val inflater : LayoutInflater? = context?.run{
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            val binding = CardLayoutBinding.inflate(inflater!!)
            return binding.root as CardWidget
        }
    }

    private fun turnCard() {
        val turnAnimation = TurnAnimation(backCardImage!!, frontCardImage!!)
        if (backCardImage!!.visibility == View.GONE)
            turnAnimation.reverse()
        startAnimation(turnAnimation)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        frontCardImage = findViewById(R.id.card_front_image)
        backCardImage = findViewById(R.id.card_back_image)
    }
}