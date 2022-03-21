package se.umu.olho0018.mapmarker.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import se.umu.olho0018.mapmarker.MainActivity
import se.umu.olho0018.mapmarker.R
import se.umu.olho0018.mapmarker.viewmodels.PostDetailViewModel
import java.text.SimpleDateFormat

private const val TAG = "PostDetailFragment"

/**
 * @author Oliver Högberg, olho0018
 * Post Detail fragment to handle a detailed view of posts
 */
class PostDetailFragment : Fragment() {

    private val args: PostDetailFragmentArgs by navArgs()

    private val postDetailViewModel: PostDetailViewModel by lazy {
        ViewModelProvider(this).get(PostDetailViewModel::class.java)
    }

    private lateinit var titleText: TextView
    private lateinit var postDate: TextView
    private lateinit var postLatitude: TextView
    private lateinit var postLongitude: TextView
    private lateinit var postDescription: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_detail, container, false)

        (activity as MainActivity?)?.hideTabs() // Hide navigation tabs
        setupViews(view)                        // Setup views

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.delete_post_action_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar to delete post
        return when (item.itemId) {
            R.id.delete_all_item -> {
                deleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Display a dialog to warn user of the action of deleting a post
     */
    private fun deleteDialog() {

        AlertDialog.Builder(context)
            .setTitle(R.string.delete_post_string)
            .setMessage(R.string.delete_post_explanation)
            .setPositiveButton(R.string.confirm_label) { _, _ ->
                postDetailViewModel.deletePost(args.post.id)

                /* Make sure to navigate the user to correct fragment
                 * depending on where the initially came from
                 */
                when (args.originFragment) {
                    "MapsFragment" ->
                        findNavController().navigate(R.id.action_postDetailFragment_to_mapsFragment)
                    "PostListFragment" ->
                        findNavController().navigate(R.id.action_postDetailFragment_to_postListFragment)
                }
            }
            .setNegativeButton(R.string.cancel_label, null)
            .show()
    }

    /**
     * Setup views to be displayed in the fragment
     * @param view The parent view to fetch specific view from
     */
    private fun setupViews(view: View) {

        setHasOptionsMenu(true)

        titleText = view.findViewById(R.id.detail_post_title)
        postDate = view.findViewById(R.id.detail_post_date)
        postLatitude = view.findViewById(R.id.post_latitude)
        postLongitude = view.findViewById(R.id.post_longitude)
        postDescription = view.findViewById(R.id.detail_post_description)

        titleText.text = args.post.title
        postDate.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(args.post.postDate)
        postLatitude.text = "Lat: ${args.post.latitude.toString()}"
        postLongitude.text = "Lng: ${args.post.longitude.toString()}"
        postDescription.text = args.post.description
    }
}