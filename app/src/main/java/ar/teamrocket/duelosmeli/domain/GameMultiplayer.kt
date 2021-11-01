package ar.teamrocket.duelosmeli.domain.model

import android.os.Parcel
import android.os.Parcelable

class GameMultiplayer() : Parcelable {
    var state: Boolean = true
    var round: Int = 0
    var currentPlayer: Int = 0

    constructor(parcel: Parcel) : this() {
        state = parcel.readByte() != 0.toByte()
        round = parcel.readInt()
        currentPlayer = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (state) 1 else 0)
        parcel.writeInt(round)
        parcel.writeInt(currentPlayer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameMultiplayer> {
        override fun createFromParcel(parcel: Parcel): GameMultiplayer {
            return GameMultiplayer(parcel)
        }

        override fun newArray(size: Int): Array<GameMultiplayer?> {
            return arrayOfNulls(size)
        }
    }



}
