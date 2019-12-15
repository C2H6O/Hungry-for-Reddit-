package net.doubov.hungryforreddit.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.R2

@ModelView(defaultLayout = R2.layout.view_root_header)
class RootHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var title: TextView? = null
    private var ups: TextView? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.view_merge_header, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        title = findViewById(R.id.titleView)
        ups = findViewById(R.id.upsView)
    }

    @TextProp
    fun setTitle(text: CharSequence) {
        title!!.text = text
    }

    @TextProp
    fun setUps(upsText: CharSequence) {
        ups!!.text = upsText
    }

    @CallbackProp
    fun clickListener(listener: OnClickListener?) {
        setOnClickListener(listener)
    }
}