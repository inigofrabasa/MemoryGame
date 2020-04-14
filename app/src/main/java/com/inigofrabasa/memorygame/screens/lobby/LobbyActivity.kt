package com.inigofrabasa.memorygame.screens.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.inigofrabasa.memorygame.R
import com.inigofrabasa.memorygame.adapters.GamesAdapter
import com.inigofrabasa.memorygame.databinding.ActivityLobbyBinding
import com.inigofrabasa.memorygame.screens.game.GameActivity
import com.inigofrabasa.memorygame.utils.COLUMNS_GAMES_NUMBER

class LobbyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLobbyBinding

    private val viewModel: LobbyViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(LobbyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lobby)
        binding.lifecycleOwner = this

        viewModel.let {
            binding.viewModel = it
        }

        viewModel.getGames()

        val adapter = GamesAdapter()
        adapter.setOnClickGameListener(object : GamesAdapter.ListenerGameSelected{
            override fun onClickGame(position: Int) {
                viewModel.gameSelected(position)
                val intent = Intent(this@LobbyActivity, GameActivity::class.java)
                startActivity(intent)
            }
        })

        binding.gamesRecycler.layoutManager = GridLayoutManager(this@LobbyActivity, COLUMNS_GAMES_NUMBER) as RecyclerView.LayoutManager
        binding.gamesRecycler.adapter = adapter
    }
}
