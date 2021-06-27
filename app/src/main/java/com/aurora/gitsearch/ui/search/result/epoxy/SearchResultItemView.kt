package com.aurora.gitsearch.ui.search.result.epoxy

import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.aurora.gitsearch.R
import com.aurora.gitsearch.data.model.GitProject
import com.aurora.gitsearch.utils.KotlinEpoxyHolder

@EpoxyModelClass
internal abstract class SearchResultItemView : EpoxyModelWithHolder<SearchResultItemView.Holder>() {

    @EpoxyAttribute
    lateinit var model: GitProject

    @EpoxyAttribute(DoNotHash)
    lateinit var onItemClicked: (GitProject) -> Unit

    override fun getDefaultLayout(): Int = R.layout.item_layout_search_result

    override fun bind(holder: Holder) {
        super.bind(holder)

        with(holder) {
            urlTextView.text = model.htmlUrl
            nameTextView.text = model.name
            descriptionTextView.text = model.description
            descriptionTextView.isVisible = model.description.isNotBlank()

            root.setOnClickListener {
                onItemClicked(model)
            }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val urlTextView by bind<TextView>(R.id.urlTextView)
        val nameTextView by bind<TextView>(R.id.nameTextView)
        val descriptionTextView by bind<TextView>(R.id.descriptionTextView)
    }
}
