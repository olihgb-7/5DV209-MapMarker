package se.umu.olho0018.mapmarker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import se.umu.olho0018.mapmarker.models.Post
import java.util.*

/**
 * @author Oliver Högberg, olho0018
 * Database class to setup a Room database for posts
 */
@Database(entities = [ Post::class ], version=1)
@TypeConverters(PostTypeConverters::class)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}

/**
 * @author Oliver Högberg, olho0018
 * Type converter class for non primitive types in database
 */
class PostTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}