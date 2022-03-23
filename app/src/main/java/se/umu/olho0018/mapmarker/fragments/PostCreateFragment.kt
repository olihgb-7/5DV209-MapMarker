package se.umu.olho0018.mapmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import se.umu.olho0018.mapmarker.MainActivity
import se.umu.olho0018.mapmarker.R
import se.umu.olho0018.mapmarker.models.Post
import se.umu.olho0018.mapmarker.viewmodels.PostCreateViewModel
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "PostCreateFragment"

/**
 * @author Oliver Högberg, olho0018
 * Post Create fragment to handle a creation of new posts
 */
class PostCreateFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val args: PostCreateFragmentArgs by navArgs()

    private lateinit var titleField: EditText
    private lateinit var dateTextView: TextView
    private lateinit var editCategorySpinner: Spinner
    private lateinit var descriptionField: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private lateinit var category: String
    private lateinit var categoryColor: String
    private lateinit var categoryColorCard: String

    private val postEditViewModel: PostCreateViewModel by lazy {
        ViewModelProvider(this).get(PostCreateViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_post_create, container, false)

        (activity as MainActivity?)?.hideTabs() // Hide navigation tabs
        setupViews(view)                        // Setup views
        spinnerListenerSetup()                  // Setup spinner listener
        buttonListenersSetup()                  // Setup button listener

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

        // Sets the post category to selected item name
        category = parent?.selectedItem.toString()

        /* Sets the post category color to selected item color.
         * Uses helper function to convert color ints to hex strings
         * and to set spinner text color.
         */
        when (category) {
            requireContext().resources.getStringArray(R.array.spinner_array)[0] -> {
                categoryColor = convertColorIntToHexString(R.color.color_important)
                categoryColorCard = convertColorIntToHexString(R.color.color_important_card)
                setSpinnerTextColor(view, R.color.color_important)
            }
            requireContext().resources.getStringArray(R.array.spinner_array)[1] -> {
                categoryColor = convertColorIntToHexString(R.color.color_work)
                categoryColorCard = convertColorIntToHexString(R.color.color_work_card)
                setSpinnerTextColor(view, R.color.color_work)
            }
            requireContext().resources.getStringArray(R.array.spinner_array)[2] -> {
                categoryColor = convertColorIntToHexString(R.color.color_personal)
                categoryColorCard = convertColorIntToHexString(R.color.color_personal_card)
                setSpinnerTextColor(view, R.color.color_personal)
            }
            requireContext().resources.getStringArray(R.array.spinner_array)[3] -> {
                categoryColor = convertColorIntToHexString(R.color.color_travel)
                categoryColorCard = convertColorIntToHexString(R.color.color_travel_card)
                setSpinnerTextColor(view, R.color.color_travel)
            }
            requireContext().resources.getStringArray(R.array.spinner_array)[4] -> {
                categoryColor = convertColorIntToHexString(R.color.color_shopping)
                categoryColorCard = convertColorIntToHexString(R.color.color_shopping_card)
                setSpinnerTextColor(view, R.color.color_shopping)
            }
            else -> {
                categoryColor = convertColorIntToHexString(R.color.color_default)
                categoryColorCard = convertColorIntToHexString(R.color.color_default_card)
                setSpinnerTextColor(view, R.color.color_default)
            }
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        category = parent?.selectedItem.toString()
    }

    /**
     * Setup of spinner
     */
    private fun spinnerListenerSetup() {
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            editCategorySpinner.adapter = adapter
        }
        editCategorySpinner.onItemSelectedListener = this
    }

    /**
     * Setup of buttons
     */
    private fun buttonListenersSetup() {

        // Setup of click listener for save button
        saveButton.setOnClickListener {

            // Create new post
            val post = Post(
                title = titleField.text.toString(),
                description = descriptionField.text.toString(),
                latitude = args.latLng.latitude,
                longitude = args.latLng.longitude,
                category = this.category,
                categoryColor = this.categoryColor,
                categoryColorCard = this.categoryColorCard
            )
            postEditViewModel.addPost(post)

            findNavController().navigate(R.id.action_postCreateFragment_to_mapsFragment)
        }

        // Setup of click listener for cancel button
        cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_postCreateFragment_to_mapsFragment)
        }
    }

    /**
     * Setup views to be displayed in the fragment
     * @param view The parent view to fetch specific view from
     */
    private fun setupViews(view: View) {

        titleField = view.findViewById(R.id.edit_post_title_text)
        descriptionField = view.findViewById(R.id.edit_post_description_text)
        dateTextView = view.findViewById(R.id.edit_post_date)
        editCategorySpinner = view.findViewById(R.id.edit_post_category_spinner)
        saveButton = view.findViewById(R.id.edit_save_post_button)
        cancelButton = view.findViewById(R.id.edit_cancel_post_button)

        dateTextView.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
    }

    /**
     * Helper function to convert color Ints to Hex strings
     */
    private fun convertColorIntToHexString(colorInt: Int): String {
        return requireContext().getColor(colorInt).toUInt().toString(16)
    }

    /**
     * Helper function to set spinner text color
     */
    private fun setSpinnerTextColor(view: View?, colorInt: Int) {
        if (view != null && view is TextView) {
            (view).setTextColor(requireContext().getColor(colorInt))
        }
    }
}