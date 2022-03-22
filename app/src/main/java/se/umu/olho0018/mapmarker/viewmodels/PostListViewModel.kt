package se.umu.olho0018.mapmarker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import se.umu.olho0018.mapmarker.database.PostRepository

/**
 * @author Oliver Högberg, olho0018
 * Post List ViewModel to handle PostListFragment logic
 */
class PostListViewModel : ViewModel() {

    private val postRepository = PostRepository.get()   // Get instance of database repository
    private var categoryLiveData = MutableLiveData<String>()

    val posts = Transformations.switchMap(categoryLiveData) { category ->

        if (category == "none") {
            postRepository.getAllPosts()
        }
        else {
            postRepository.getCategoryPosts(category)
        }
    }

    fun filterPosts(category: String = "none") {
        categoryLiveData.value = category
    }
}