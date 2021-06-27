package com.aurora.gitsearch.ui.search.result.epoxy

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.aurora.gitsearch.R
import com.aurora.gitsearch.utils.KotlinEpoxyHolder

@EpoxyModelClass
internal abstract class ErrorView : EpoxyModelWithHolder<ErrorView.Holder>() {

    @EpoxyAttribute(DoNotHash)
    lateinit var onRetryClicked: () -> Unit

    override fun getDefaultLayout(): Int = R.layout.item_layout_error

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.retryButton.setOnClickListener {
            onRetryClicked()
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val retryButton by bind<TextView>(R.id.retryButton)
    }
}
