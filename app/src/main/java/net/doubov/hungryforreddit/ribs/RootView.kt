package net.doubov.hungryforreddit.ribs

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.airbnb.epoxy.EpoxyRecyclerView
import net.doubov.api.models.NewsResponse
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.views.rootHeaderView

/**
 * Top level view for {@link RootRibBuilder.RootRibScope}.
 */
class RootView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle), RootInteractor.RootRibPresenter {

    interface Listener {
        fun onItemSelected(position: Int)
    }

    private var listener: Listener? = null
    private var recyclerView: EpoxyRecyclerView? = null

    override fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        println("LX___ inflating rootView!")
        recyclerView = findViewById(R.id.recyclerView)
    }

    override fun setData(data: NewsResponse) {
        recyclerView!!.withModels {
            data.data.children.map { it.data }.forEachIndexed { index, childResponse ->
                rootHeaderView {
                    id(childResponse.id)
                    title(childResponse.title)
                    ups(childResponse.ups.toString())
                    clickListener(OnClickListener {
                        listener?.onItemSelected(index)
                    })
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        recyclerView?.adapter = null
        super.onDetachedFromWindow()
    }
}
