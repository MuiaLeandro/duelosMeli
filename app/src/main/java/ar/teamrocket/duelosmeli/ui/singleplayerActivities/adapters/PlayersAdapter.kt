package ar.teamrocket.duelosmeli.ui.singleplayerActivities.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.teamrocket.duelosmeli.data.database.Player
import ar.teamrocket.duelosmeli.databinding.ItemPlayerBinding
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.views.GameActivity

class PlayersAdapter(private val players:List<Player>): RecyclerView.Adapter<PlayersAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(player: Player) {
            binding.tvNamePlayerItem.text = player.name
            itemView.setOnClickListener {
                playGame(player.id)
            }
        }

        private fun playGame(id:Long) {
            itemView.context
                    .startActivity(Intent(binding.root.context, GameActivity::class.java)
                    .putExtra("Id",id))
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