package ar.teamrocket.duelosmeli.domain

import com.bumptech.glide.load.resource.gif.GifDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.Nullable
import com.bumptech.glide.request.target.ImageViewTarget


/**
 * A target for display [GifDrawable] or [Drawable] objects in [ImageView]s.
 */
class GifDrawableImageViewTarget : ImageViewTarget<Drawable?> {
    private var mLoopCount = GifDrawable.LOOP_FOREVER

    constructor(view: ImageView?, loopCount: Int) : super(view) {
        mLoopCount = loopCount
    }

    constructor(view: ImageView?, loopCount: Int, waitForLayout: Boolean) : super(
        view,
        waitForLayout
    ) {
        mLoopCount = loopCount
    }

    override fun setResource(@Nullable resource: Drawable?) {
        if (resource is GifDrawable) {
            resource.setLoopCount(mLoopCount)
        }
        view.setImageDrawable(resource)
    }
}