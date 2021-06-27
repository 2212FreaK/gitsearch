package com.aurora.gitsearch.ui.search.result.epoxy

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.aurora.gitsearch.R
import com.aurora.gitsearch.utils.KotlinEpoxyHolder

@EpoxyModelClass
internal abstract class LoadingView : EpoxyModel<View>() {
    override fun getDefaultLayout(): Int = R.layout.item_layout_loading
}
