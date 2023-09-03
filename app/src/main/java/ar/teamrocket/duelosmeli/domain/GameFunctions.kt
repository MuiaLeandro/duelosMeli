package ar.teamrocket.duelosmeli.domain

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

interface GameFunctions {

    fun showImage(context: Context, image: Int, imageView: ImageView)

    fun audioPlayer(context: Context, sound: Int)

    fun optionsSounds(context: Context, state: Boolean)

    fun mistakeCounterUpdater(game: Game, lifeThree: ImageView, lifeTwo: ImageView, lifeOne: ImageView)

    fun lifesCounterUpdater(game: Game, lifeThree: ImageView, lifeTwo: ImageView, lifeOne: ImageView)

    @ColorInt
    fun getColorFromAttr(context: Context, @AttrRes attrColor: Int, typedValue: TypedValue): Int

    fun colorFormatter(context: Context, list: List<View>, color: Int)
}