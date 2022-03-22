package se.umu.olho0018.mapmarker.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.olho0018.mapmarker.MainActivity
import se.umu.olho0018.mapmarker.R
import se.umu.olho0018.mapmarker.adapters.PostListAdapter
import se.umu.olho0018.mapmarker.viewmodels.PostListViewModel

private const val TAG = "PostListFragment"
private const val POST_LIST_FRAGMENT_KEY = "postListFragment"

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

    private var itemId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view  = inflater.inflate(R.layout.fragment_post_list, container, false)

        // Handles restoration and instance state retrieval
        if (savedInstanceState != null) {
            itemId = savedInstanceState.getInt(POST_LIST_FRAGMENT_KEY)
        }

        (activity as MainActivity?)?.showTabs() // Show navigation tabs

        // Setup of recycler view for the list of posts
        postRecyclerView = view.findViewById(R.id.post_recycler_view) as RecyclerView
        postRecyclerView.layoutManager = LinearLayoutManager(context)
        postRecyclerView.adapter = adapter

        setHasOptionsMenu(true)
        postListViewModel.filterPosts()

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

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.filter_overflow_menu, menu)
        menu.performIdentifierAction(itemId, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        itemId = item.itemId
        // Handle overflow menu to filter posts
        return when (itemId) {
            R.id.none_cat_item-> {
                postListViewModel.filterPosts()
                true
            }
            R.id.important_cat_item-> {
                postListViewModel.filterPosts(
                    resources.getString(R.string.important_cat)
                )
                true
            }
            R.id.work_cat_item-> {
                postListViewModel.filterPosts(
                    resources.getString(R.string.work_cat)
                )
                true
            }
            R.id.personal_cat_item-> {
                postListViewModel.filterPosts(
                    resources.getString(R.string.personal_cat)
                )
                true
            }
            R.id.travel_cat_item-> {
                postListViewModel.filterPosts(
                    resources.getString(R.string.travel_cat)
                )
                true
            }
            R.id.shopping_cat_item-> {
                postListViewModel.filterPosts(
                    resources.getString(R.string.shopping_cat)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(POST_LIST_FRAGMENT_KEY, itemId)

    }
}