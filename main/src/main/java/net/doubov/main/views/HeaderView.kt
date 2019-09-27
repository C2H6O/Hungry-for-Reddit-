package net.doubov.main.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotlinx.android.synthetic.main.view_main_header.view.*
import net.doubov.main.R
import net.doubov.main.R2

@ModelView(defaultLayout = R2.layout.view_header)
class HeaderView : LinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.view_main_header, this, true)
    }

    @TextProp
    fun setTitle(text: CharSequence) {
        titleView.text = text
    }

    @CallbackProp
    fun clickListener(listener: OnClickListener?) {
        setOnClickListener(listener)
    }
}