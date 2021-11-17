package ar.teamrocket.duelosmeli.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.databinding.ItemMultiplayerBinding

class PlayersTeamsAdapter(private var players:List<Multiplayer>, private val listener: IPlayersTeamsAdapter): RecyclerView.Adapter<PlayersTeamsAdapter.ViewHolder>() {
    //private var multiplayers= emptyList<Multiplayer>()

    fun setListData(data: List<Multiplayer>) {
        this.players = data
        this.notifyDataSetChanged()
    }


    class ViewHolder(val binding: ItemMultiplayerBinding,val listener: IPlayersTeamsAdapter) : RecyclerView.ViewHolder(binding.root){

        fun bind(player: Multiplayer) {
            binding.tvNamePlayerItem.text = player.name
            binding.ivDelete.setOnClickListener {
                listener.onItemClicked(player)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMultiplayerBinding
            .inflate(LayoutInflater.from(parent.context),parent, false)

        return ViewHolder(binding,listener)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position])
    }

}

interface IPlayersTeamsAdapter {
    fun onItemClicked(player: Multiplayer)
}