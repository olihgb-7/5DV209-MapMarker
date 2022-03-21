package se.umu.olho0018.mapmarker.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import se.umu.olho0018.mapmarker.models.Post
import java.util.*
import java.util.concurrent.Executors

/**
 * @author Oliver Högberg, olho0018
 * Database repository to build database and handle database queries as an intermediary
 */
class PostRepository private constructor(context: Context){

    // Build Room database
    private val database: PostDatabase = Room.databaseBuilder(
        context.applicationContext,
        PostDatabase::class.java,
        "post_database"
    ).build()

    // Setup DAO and thread executor
    private val postDao = database.postDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllPosts(): LiveData<List<Post>> = postDao.getAllPosts()
    fun getPost(id: UUID): LiveData<Post> = postDao.getPost(id)

    fun addPost(post: Post) {
        // Execute on new thread
        executor.execute {
            postDao.addPost(post)
        }
    }

    fun deletePost(id: UUID) {
        // Execute on new thread
        executor.execute {
            postDao.deletePost(id)
        }
    }

    /**
     * Companion object to help initialize
     * repository and get an fetch that instance
     */
    companion object {
        private var INSTANCE: PostRepository? = null

        /**
         * Initialize repository
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PostRepository(context)
            }
        }

        /**
         * Get repository insatance
         */
        fun get(): PostRepository {
            return INSTANCE ?:
            throw IllegalStateException("PostRepository must be initialized")
        }
    }

}