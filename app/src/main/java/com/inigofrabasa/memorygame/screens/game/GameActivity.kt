package com.inigofrabasa.memorygame.screens.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.inigofrabasa.memorygame.R
import com.inigofrabasa.memorygame.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private val viewModel: GameViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(GameViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.lifecycleOwner = this
        binding.gameBackButton.setOnClickListener { onBackPressed() }

        viewModel.let {
            binding.viewModel = it
        }

        binding.visibilityWin = false
        subscribeUi()
    }

    private fun subscribeUi(){
        viewModel.turnDownAllCards.observe(this, Observer {
            binding.boardGame.turnDownAllCards()
        })
        viewModel.deleteMatchedCards.observe(this, Observer { match ->
            binding.boardGame.deletePairOfCards(match.firstId, match.secondId)
        })
        viewModel.winGame.observe(this, Observer {
            Handler().postDelayed({
                runOnUiThread{
                    binding.visibilityWin = true
                    Toast.makeText(this, "You Win!", Toast.LENGTH_SHORT).show()
                    super.onResume()
                }
            }, 300)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeObserverTurnDownAllCards()
        viewModel.removeObserverDeleteMatchedCards()
        viewModel.removeObserverWinGame()
    }
}