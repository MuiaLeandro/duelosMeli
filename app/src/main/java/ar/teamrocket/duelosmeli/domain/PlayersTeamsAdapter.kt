package ar.teamrocket.duelosmeli.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.databinding.ItemMultiplayerBinding

class PlayersTeamsAdapter(private val players:List<Multiplayer>): RecyclerView.Adapter<PlayersTeamsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMultiplayerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(player: Multiplayer) {
            binding.tvNamePlayerItem.text = player.name
            binding.ivDelete.setOnClickListener {
                
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMultiplayerBinding
            .inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position])
    }
}