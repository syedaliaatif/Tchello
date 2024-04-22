package com.aatif.tchello.common.model

import android.os.Parcel
import android.os.Parcelable
import com.aatif.tchello.screens.adapters.BoardsAdapter

/**
 * Data class that holds information about board.
 */
data class Board(
    val id: String,
    val name: String,
    val coverPhoto: String,
    val tasks: List<Task>,
    val memberIds: List<String>,
    val isStarred: Boolean,
    val activityIds: List<String>,
    val ownerId: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty().ifEmpty { throw IllegalArgumentException("Empty id for board.") },
        parcel.readString().orEmpty().ifEmpty { throw IllegalArgumentException("Empty name for board.") },
        parcel.readString().orEmpty(),
        parcel.createTypedArray(Task.CREATOR).orEmpty().toList(),
        parcel.createStringArrayList().orEmpty(),
        parcel.readByte() != 0.toByte(),
        parcel.createStringArrayList().orEmpty(),
        parcel.readString().orEmpty().ifEmpty { throw IllegalArgumentException("Empty owner id for board.") }
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(coverPhoto)
        parcel.writeTypedList(tasks)
        parcel.writeStringList(memberIds)
        parcel.writeByte(if (isStarred) 1 else 0)
        parcel.writeStringList(activityIds)
        parcel.writeString(ownerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Board> {
        override fun createFromParcel(parcel: Parcel): Board {
            return Board(parcel)
        }

        override fun newArray(size: Int): Array<Board?> {
            return arrayOfNulls(size)
        }

        fun fromMap(map: Map<String, *>?): Board? {
            if(map == null) return null
            val id: String = (map.get("id") as? String).orEmpty()
            val name: String = (map.get("name") as? String).orEmpty()
            val coverPhoto: String = (map.get("coverPhoto") as? String).orEmpty()
            val tasks: List<Task> = (map.get("tasks") as? List<Task>).orEmpty()
            val memberIds: List<String> = (map.get("memberIds") as? List<String>).orEmpty()
            val isStarred: Boolean = (map.get("isStarred") as? Boolean)?:false
            val activityIds: List<String> = (map.get("activityIds") as? List<String>).orEmpty()
            val ownerId: String = (map.get("ownerId") as? String).orEmpty()
            id.ifBlank { throw IllegalArgumentException("Empty id in Board.") }
            name.ifBlank { throw IllegalArgumentException("Empty name in Board.") }
            return Board(id, name, coverPhoto, tasks, memberIds, isStarred, activityIds, ownerId)
        }
    }
}