package com.aurora.gitsearch.ui.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> Fragment.viewBindingLifecycle(
    noinline onCleanup: ((T) -> Unit)? = null
): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {

        // A backing property to hold our value
        private var binding: T? = null

        init {
            var viewLifecycleOwner: LifecycleOwner? = null
            this@viewBindingLifecycle
                .viewLifecycleOwnerLiveData
                .observe(this@viewBindingLifecycle) { newLifecycleOwner ->
                    viewLifecycleOwner?.lifecycle?.removeObserver(this)

                    viewLifecycleOwner = newLifecycleOwner.also {
                        it.lifecycle.addObserver(this)
                    }
                }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            onCleanup?.invoke(binding!!)
            binding = null
        }

        override fun getValue(
            thisRef: Fragment,
            property: KProperty<*>
        ): T {
            return this.binding!!
        }

        override fun setValue(
            thisRef: Fragment,
            property: KProperty<*>,
            value: T
        ) {
            this.binding = value
        }
    }
