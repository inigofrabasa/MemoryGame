package com.inigofrabasa.memorygame.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

import com.inigofrabasa.memorygame.data.model.Model
import com.inigofrabasa.memorygame.databinding.GameSelectionItemBinding

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.ViewHolder>(){

    private var items : List<Model.Game>? = mutableListOf()
    private var listenerGameSelected : ListenerGameSelected? = null

    fun submitList(inItems : List<Model.Game>?){
        this.items = inItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = GameSelectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fun onViewClick(){
            items?.get(position)?.let { listenerGameSelected?.onClickGame(position) }
        }
        items?.get(position)?.let { holder.bindData(it, ::onViewClick) }
    }

    inner class ViewHolder (private var binding: ViewDataBinding? = null) : RecyclerView.ViewHolder(binding?.root!!) {
        fun bindData(item: Model.Game, clickListener: () -> Unit) {
            (binding as GameSelectionItemBinding).apply {
                name = item.name
                content.setOnClickListener { clickListener() }
            }
        }
    }

    fun setOnClickGameListener(listenerGameSelected : ListenerGameSelected){
        this.listenerGameSelected = listenerGameSelected
    }

    interface ListenerGameSelected{
        fun onClickGame(position : Int)
    }
}