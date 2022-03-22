package se.umu.olho0018.mapmarker.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Model class to handle Post objects
 */
@Parcelize
@Entity
class Post (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var postDate: Date = Date(),
    var title: String = "",
    var description: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var category: String = "",
    var categoryColor: String = "",
    var categoryColorCard: String = ""
) : Parcelable