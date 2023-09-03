package ar.teamrocket.duelosmeli.domain

import android.content.Context
import android.widget.ImageView

interface GameFunctions {

    fun showImage(context: Context, image: Int, imageView: ImageView)

    fun audioPlayer(context: Context, sound: Int)

    fun optionsSounds(context: Context, state: Boolean)

    fun mistakeCounterUpdater(game: Game, lifeThree: ImageView, lifeTwo: ImageView, lifeOne: ImageView)

    fun lifesCounterUpdater(game: Game, lifeThree: ImageView, lifeTwo: ImageView, lifeOne: ImageView)
}