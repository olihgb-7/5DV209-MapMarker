package se.umu.olho0018.mapmarker.viewmodels

import android.graphics.Color
import androidx.lifecycle.ViewModel
import se.umu.olho0018.mapmarker.database.PostRepository
import java.util.*

/**
 * @author Oliver Högberg, olho0018
 * Maps ViewModel to handle MapsFragment logic
 */
class MapsViewModel: ViewModel() {

    private val postRepository = PostRepository.get()   // Get instance of database repository
    var posts = postRepository.getAllPosts()            // Get all posts
    fun post(id: UUID) = postRepository.getPost(id)     // Get single post

    /**
     * Convert a Hex string to a corresponding float array representation
     * @param colorString Color Hex string to be converted
     * @return FloatArray to be returned as a hue
     */
    fun convertHexToHue(colorString: String): FloatArray? {
        val hue = FloatArray(3)
        val color: Int = Color.parseColor(colorString)
        Color.colorToHSV(color, hue)
        return hue
    }
}