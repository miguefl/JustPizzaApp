package com.migferlab.justpizza.features.base

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.migferlab.justpizza.R
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import timber.log.Timber


open class BaseFragment : Fragment(), DIAware {

    override val di: DI by di()

    fun setToolbarTitle(title: String) {
        activity?.findViewById<Toolbar>(R.id.toolBar)?.title = title
    }

    inline fun <T> LiveData<T>.observe(isLogEnabled: Boolean = true, crossinline cb: (T) -> Unit) {
        removeObservers(viewLifecycleOwner)
        observe(viewLifecycleOwner,
            Observer {
                if (isLogEnabled) {
                    Timber.d(
                        " \n" +
                                """
                    ┌────────────────────────────────────────────
                    |─> ${it.toString()}
                    └────────────────────────────────────────────
                    """.trimIndent()
                    )
                }
                cb(it)
            })
    }
}