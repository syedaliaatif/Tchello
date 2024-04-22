package com.aatif.tchello.common.model

import android.os.Parcel
import android.os.Parcelable


data class Card(
    val title: String,
    val description: String,
    val coverPhoto: String,
    val labels: List<String>,
    val memberIds: List<String>,
    val startDate: String,
    val endDate: String,
    val activityIds: List<String>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty().ifEmpty { throw IllegalArgumentException("Empty title for Card.") },
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.createStringArrayList().orEmpty(),
        parcel.createStringArrayList().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.createStringArrayList().orEmpty()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(coverPhoto)
        parcel.writeStringList(labels)
        parcel.writeStringList(memberIds)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeStringList(activityIds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}
