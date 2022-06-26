package ar.teamrocket.duelosmeli.domain.impl

import android.content.Context
import android.media.MediaPlayer
import android.widget.ImageView
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.domain.Game
import ar.teamrocket.duelosmeli.domain.GameFunctions

class GameFunctionsImpl : GameFunctions {

    override fun optionsSounds(context: Context, state: Boolean) {
        when (state) {
            true -> {
                val correctSound = MediaPlayer.create(context, R.raw.correct)
                correctSound.start()
            }
            false -> {
                val incorrectSound = MediaPlayer.create(context, R.raw.incorrect)
                incorrectSound.start()
            }
        }
    }

    override fun mistakeCounterUpdater(game: Game, lifeThree: ImageView, lifeTwo: ImageView, lifeOne: ImageView) {
        when (game.errors){
            1 -> lifeThree.setImageResource(R.drawable.ic_life_not_filled)
            2 -> lifeTwo.setImageResource(R.drawable.ic_life_not_filled)
            3 -> lifeOne.setImageResource(R.drawable.ic_life_not_filled)
        }
    }

    override fun lifesCounterUpdater(game: Game, lifeThree: ImageView, lifeTwo: ImageView, lifeOne: ImageView) {
        when (game.errors){
            1 -> lifeThree.setImageResource(R.drawable.ic_life_filled)
            2 -> lifeTwo.setImageResource(R.drawable.ic_life_filled)
        }
    }
}