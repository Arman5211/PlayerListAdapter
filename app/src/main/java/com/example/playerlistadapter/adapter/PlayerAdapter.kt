package com.example.playerlistadapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playerlistadapter.databinding.ListItemBinding
import com.example.playerlistadapter.model.Player


class PlayerAdapter(private val playerlist:MutableList<Player>,private val clicklisten:PlayerClickListen):
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>()
{
    interface PlayerClickListen{
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    class PlayerViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.name.text = player.PlayerName.toString()
            binding.age.text = player.PlayerAge.toString()
            binding.position.text = player.PlayerPosition.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding=ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
    return playerlist.size
}

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val Player = playerlist[position]
        holder.bind(Player)
        holder.binding.editBtn.setOnClickListener {
            clicklisten.onEditClick(position)
        }
        holder.binding.deleteBtn.setOnClickListener {
            clicklisten.onDeleteClick(position)

        }
    }
}