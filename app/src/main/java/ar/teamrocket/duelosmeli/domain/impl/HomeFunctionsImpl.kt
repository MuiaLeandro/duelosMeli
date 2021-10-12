package ar.teamrocket.duelosmeli.domain.impl

import android.content.Context
import android.widget.ImageView
import ar.teamrocket.duelosmeli.domain.GifDrawableImageViewTarget
import ar.teamrocket.duelosmeli.domain.HomeFunctions
import com.bumptech.glide.Glide

class HomeFunctionsImpl : HomeFunctions {
    override fun showImage(context: Context, image: Int, imageView: ImageView) {
        Glide.with(context)
            .load(image)
            .into(GifDrawableImageViewTarget(imageView, 1));
    }
}