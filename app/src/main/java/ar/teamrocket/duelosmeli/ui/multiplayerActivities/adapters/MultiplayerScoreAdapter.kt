package ar.teamrocket.duelosmeli.ui.multiplayerActivities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.databinding.ItemHighscoreBinding

class MultiplayerScoreAdapter (private var players:List<Multiplayer>): RecyclerView.Adapter<MultiplayerScoreAdapter.HighScoreHolder>(){

    class HighScoreHolder(val binding: ItemHighscoreBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Multiplayer) {
            binding.tvNamePlayerHighscore.text = player.name
            binding.tvPointsPlayerHighscore.text = player.score.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreHolder {
        val binding = ItemHighscoreBinding
            .inflate(LayoutInflater.from(parent.context),parent, false)
        return HighScoreHolder(binding)
    }

    override fun onBindViewHolder(holder: HighScoreHolder, position: Int) {
        holder.bind(players[position])
    }

    override fun getItemCount(): Int {
        return players.size
    }

    fun setListData(data: List<Multiplayer>) {
        this.players = data
        this.notifyDataSetChanged()

    }
}