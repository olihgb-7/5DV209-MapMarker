package se.umu.olho0018.mapmarker.viewmodels

import androidx.lifecycle.ViewModel
import se.umu.olho0018.mapmarker.database.PostRepository

/**
 * @author Oliver Högberg, olho0018
 * Post List ViewModel to handle PostListFragment logic
 */
class PostListViewModel : ViewModel() {

    private val postRepository = PostRepository.get()   // Get instance of database repository
    var posts = postRepository.getAllPosts()            // Get all posts
}