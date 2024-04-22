package com.aatif.tchello.common.model

import android.os.Parcel
import android.os.Parcelable

data class Task(
    val title: String,
    val cards: List<Card>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty().ifEmpty { throw IllegalArgumentException("Empty title for a task.") },
        parcel.createTypedArrayList(Card.CREATOR).orEmpty()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(cards)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}
