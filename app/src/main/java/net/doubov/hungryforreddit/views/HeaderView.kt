package net.doubov.hungryforreddit.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotlinx.android.synthetic.main.view_main_header.view.*
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.R2

@ModelView(defaultLayout = R2.layout.view_header)
class HeaderView : ConstraintLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_main_header, this, true)
    }

    @TextProp
    fun setTitle(text: CharSequence) {
        titleView.text = text
    }

    @TextProp
    fun setUps(ups: CharSequence) {
        upsView.text = ups
    }

    @CallbackProp
    fun clickListener(listener: OnClickListener?) {
        setOnClickListener(listener)
    }
}