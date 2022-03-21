package se.umu.olho0018.mapmarker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import se.umu.olho0018.mapmarker.models.Post
import java.util.*

/**
 * @author Oliver Högberg, olho0018
 * Interface DAO to setup database queries
 */
@Dao
interface PostDao {

    @Query("SELECT * FROM post")
    fun getAllPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM post WHERE id=(:id)")
    fun getPost(id: UUID): LiveData<Post>

    @Update
    fun updatePost(post: Post)

    @Insert
    fun addPost(post: Post)

    @Query("DELETE FROM post")
    fun deleteAllPosts()

    @Query("DELETE FROM post WHERE id=(:id)")
    fun deletePost(id: UUID)
}