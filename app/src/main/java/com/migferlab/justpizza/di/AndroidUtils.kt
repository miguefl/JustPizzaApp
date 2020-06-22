package com.migferlab.justpizza.di

import androidx.annotation.MainThread
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hoopcarpool.fluxy.FluxyStore
import com.migferlab.justpizza.features.base.BaseFragment
import com.migferlab.justpizza.features.main.MainActivity
import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.bind
import org.kodein.di.bindings.NoArgBindingDI
import org.kodein.di.bindings.NoArgSimpleBindingDI
import org.kodein.di.direct
import org.kodein.di.inSet
import org.kodein.di.instance
import org.kodein.di.instanceOrNull
import org.kodein.di.provider
import org.kodein.di.singleton

inline fun <reified T : ViewModel> DI.Builder.bindViewModel(
    overrides: Boolean? = null,
    noinline creator: NoArgBindingDI<*>.() -> T
) {
    bind<T>(T::class.java.simpleName, overrides) with provider(creator)
}

inline fun <reified T : FluxyStore<*>> DI.Builder.bindStore(noinline creator: NoArgSimpleBindingDI<*>.() -> T) {
    bind<T>() with singleton(creator = creator)
    bind<FluxyStore<*>>().inSet() with singleton { instance<T>() }
}

/**
 * [ViewModelProvider.Factory] implementation that relies in Kodein injector to retrieve ViewModel
 * instances.
 *
 * Optionally you can decide if you want all instances to be force-provided by module bindings or
 * if you allow creating new instances of them via [Class.newInstance] with [allowNewInstance].
 * The default is true to mimic the default behaviour of [ViewModelProvider].
 */
class DIViewModelFactory(
    private val injector: DirectDI,
    private val allowNewInstance: Boolean = true
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return injector.instanceOrNull<ViewModel>(tag = modelClass.simpleName) as T?
            ?: if (allowNewInstance) {
                modelClass.newInstance()
            } else {
                throw RuntimeException("The class ${modelClass.name} cannot be provided as no Kodein bindings could be found")
            }
    }
}

@MainThread
inline fun <reified VM : ViewModel, T : BaseFragment> T.viewModel(): Lazy<VM> {
    return lazy {
        ViewModelProvider(viewModelStore, direct.instance()).get(VM::class.java)
    }
}

@MainThread
inline fun <reified VM : ViewModel, T : MainActivity> T.viewModel(): Lazy<VM> {
    return lazy {
        ViewModelProvider(viewModelStore, direct.instance()).get(VM::class.java)
    }
}

