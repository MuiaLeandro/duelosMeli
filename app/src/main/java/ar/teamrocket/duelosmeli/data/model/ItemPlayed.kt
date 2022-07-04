package ar.teamrocket.duelosmeli.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemPlayed (
    var title: String,
    var picture: String,
    var permalink: String
): Parcelable
