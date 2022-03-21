package se.umu.olho0018.mapmarker.viewmodels

import androidx.lifecycle.ViewModel
import se.umu.olho0018.mapmarker.database.PostRepository
import se.umu.olho0018.mapmarker.models.Post

/**
 * @author Oliver Högberg, olho0018
 * Post Create ViewModel to handle PostCreateFragment logic
 */
class PostCreateViewModel : ViewModel() {

    private val postRepository = PostRepository.get() // Get instance of database repository

    /**
     * Add post to database
     */
    fun addPost(post: Post) {
        postRepository.addPost(post)
    }
}