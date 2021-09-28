package ar.teamrocket.duelosmeli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.teamrocket.duelosmeli.database.Player
import ar.teamrocket.duelosmeli.databinding.ItemPlayerBinding

class PlayersAdapter(private val players:List<Player>): RecyclerView.Adapter<PlayersAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(player: Player) {
            binding.tvNamePlayerItem.text = player.name
            binding.btnPlayItem.setOnClickListener {
                playGame(player.id)
            }
        }

        private fun playGame(id:Long) {
            TODO("Not yet implemented")
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlayerBinding
            .inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return players.size
    }



}