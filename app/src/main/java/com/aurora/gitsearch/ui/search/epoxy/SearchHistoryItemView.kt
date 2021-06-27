package com.aurora.gitsearch.ui.search.epoxy

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.aurora.gitsearch.R
import com.aurora.gitsearch.utils.KotlinEpoxyHolder

@EpoxyModelClass
internal abstract class SearchHistoryItemView : EpoxyModelWithHolder<SearchHistoryItemView.Holder>() {

    @EpoxyAttribute
    lateinit var name: String

    @EpoxyAttribute(DoNotHash)
    lateinit var onItemClicked: (String) -> Unit

    override fun getDefaultLayout(): Int = R.layout.item_layout_search_history

    override fun bind(holder: Holder) {
        super.bind(holder)

        with(holder) {
            nameTextView.text = name

            root.setOnClickListener {
                onItemClicked(name)
            }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val nameTextView by bind<TextView>(R.id.nameTextView)
    }
}
