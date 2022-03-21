package se.umu.olho0018.mapmarker.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import se.umu.olho0018.mapmarker.R
import se.umu.olho0018.mapmarker.fragments.PostListFragment
import se.umu.olho0018.mapmarker.fragments.PostListFragmentDirections
import se.umu.olho0018.mapmarker.models.Post

/**
 * @author Oliver Högberg, olho0018
 * Holder class to handle items in recycler view
 */
class PostListHolder(view: View, var context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private lateinit var post: Post

    private val postTitle: TextView = itemView.findViewById(R.id.post_title)
    private val postDate: TextView = itemView.findViewById(R.id.post_date)
    private val postDescription: TextView = itemView.findViewById(R.id.post_description)
    private val postCardView: CardView = itemView.findViewById(R.id.post_card_view)

    init {
        itemView.setOnClickListener(this)
    }

    /**
     * Updates the recycler view item with corresponding post attributes
     * @param post The post to be displayed
     */
    fun bind(post: Post) {
        this.post = post
        postTitle.text = this.post.title
        postDate.text = this.post.postDate.toString()
        postDescription.text = this.post.description
        setCardColor(post)
    }

    override fun onClick(view: View?) {
        /* Handles recycler view item navigation
         * from a post to the corresponding PostDetailFragment
         */
        if (view != null) {
            Navigation.findNavController(view).navigate(
                PostListFragmentDirections.actionPostListFragmentToPostDetailFragment(
                    post,
                    PostListFragment::class.simpleName as String
                )
            )
        }
    }

    /**
     * Sets the CardView background color of specific post
     * @param post The post of which the CardView background color should change
     */
    private fun setCardColor(post: Post) {
        postCardView.setCardBackgroundColor(Color.parseColor("#${post.categoryColor}"))
    }
}

/**
 * @author Oliver Högberg, olho0018
 * Adapter class to be used by PostListFragment to handle the recycler view
 */
class PostListAdapter(
    var posts: List<Post>,
    var layoutInflater: LayoutInflater,
    var context: Context
) : RecyclerView.Adapter<PostListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListHolder {
        val view = layoutInflater.inflate(R.layout.list_item_post, parent, false)
        return PostListHolder(view, context)
    }

    override fun onBindViewHolder(holder: PostListHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount() = posts.size
}