package se.umu.olho0018.mapmarker.viewmodels

import androidx.lifecycle.ViewModel
import se.umu.olho0018.mapmarker.database.PostRepository
import java.util.*

/**
 * @author Oliver Högberg, olho0018
 * Post Detail ViewModel to handle PostDetailFragment logic
 */
class PostDetailViewModel: ViewModel() {

    private val postRepository = PostRepository.get() // Get instance of database repository

    /**
     * Add post from database
     */
    fun deletePost(id: UUID) {
        postRepository.deletePost(id)
    }
}