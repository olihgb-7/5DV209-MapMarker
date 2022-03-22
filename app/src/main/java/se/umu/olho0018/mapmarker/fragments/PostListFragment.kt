package se.umu.olho0018.mapmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import se.umu.olho0018.mapmarker.MainActivity
import se.umu.olho0018.mapmarker.R
import se.umu.olho0018.mapmarker.adapters.PostListAdapter
import se.umu.olho0018.mapmarker.viewmodels.PostListViewModel

private const val TAG = "PostListFragment"

/**
 * @author Oliver Högberg, olho0018
 * Post List fragment to handle the currently added posts
 */
class PostListFragment : Fragment() {

    private lateinit var postRecyclerView: RecyclerView
    private var adapter: PostListAdapter? = null
    private val postListViewModel: PostListViewModel by lazy {
        ViewModelProvider(this).get(PostListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view  = inflater.inflate(R.layout.fragment_post_list, container, false)

        (activity as MainActivity?)?.showTabs() // Show navigation tabs

        // Setup of recycler view for the list of posts
        postRecyclerView = view.findViewById(R.id.post_recycler_view) as RecyclerView
        postRecyclerView.layoutManager = LinearLayoutManager(context)
        postRecyclerView.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()

        // Observe changes posts from database
        postListViewModel.posts.observe(
            viewLifecycleOwner,
            Observer { posts ->
                // Setup of PostListAdapter
                adapter?.let {
                    it.posts = posts
                } ?: run {
                    adapter = context?.let { PostListAdapter(posts, layoutInflater, it) }
                }
                postRecyclerView.adapter = adapter
            }
        )
    }
}