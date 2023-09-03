package ar.teamrocket.duelosmeli.domain.impl

import android.content.Context
import android.media.MediaPlayer
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.domain.Game
import ar.teamrocket.duelosmeli.domain.GameFunctions
import ar.teamrocket.duelosmeli.domain.GifDrawableImageViewTarget
import com.bumptech.glide.Glide

class GameFunctionsImpl : GameFunctions {

    override fun showImage(context: Context, image: Int, imageView: ImageView) {
        Glide.with(context)
            .load(image)
            .into(GifDrawableImageViewTarget(imageView, 1));
    }

    override fun audioPlayer(context: Context, sound: Int) {
        MediaPlayer.create(context, sound)
            .setOnPreparedListener { it.start() }
    }

    override fun optionsSounds(context: Context, state: Boolean) {
        when (state) {
            true -> audioPlayer(context, R.raw.correct)
            false -> audioPlayer(context, R.raw.incorrect)
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

    override fun getColorFromAttr(context: Context, @AttrRes attrColor: Int, typedValue: TypedValue, ): Int {
        context.theme.resolveAttribute(attrColor, typedValue, true)
        return typedValue.data
    }

    override fun colorFormatter(context: Context, list: List<View>, color: Int) {
        val colorFromAttr = getColorFromAttr(context, color, TypedValue())
        for (button in list) {
            button.setBackgroundColor(colorFromAttr)
        }
    }
}
