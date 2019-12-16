package net.doubov.hungryforreddit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_detail.*
import net.doubov.api.RedditApi
import javax.inject.Inject

class MainDetailFragment : Fragment(R.layout.fragment_detail) {

    @Inject
    lateinit var redditApi: RedditApi

    object Args {
        const val ID = "id"
        const val TITLE = "title"
    }

    companion object {
        fun newInstance(
            id: String,
            title: String
        ): MainDetailFragment {
            return MainDetailFragment().also {
                it.arguments = Bundle().also {
                    it.putString(Args.ID, id)
                    it.putString(Args.TITLE, title)
                }
            }
        }
    }

    private val id: String
        get() = requireArguments().getString(Args.ID)!!

    private val title: String
        get() = requireArguments().getString(Args.TITLE)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleView.text = title
    }
}