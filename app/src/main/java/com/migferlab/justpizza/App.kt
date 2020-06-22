package com.migferlab.justpizza

import android.app.Application
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.FluxyStore
import com.migferlab.justpizza.di.AppModule
import com.migferlab.justpizza.di.FirebaseModule
import com.migferlab.justpizza.di.FirestoreModule
import com.migferlab.justpizza.di.FluxModule
import com.migferlab.justpizza.di.RepositoryModule
import com.migferlab.justpizza.di.StoreModule
import com.migferlab.justpizza.di.ViewModelModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import timber.log.Timber

import kotlin.properties.Delegates

private var appInstance: App by Delegates.notNull()
val app: App get() = appInstance

class App : Application(), DIAware {

    override val di = DI.lazy {
        import(AppModule.create())
        import(ViewModelModule.create())
        import(FluxModule.create())
        import(StoreModule.create())
        import(RepositoryModule.create())
        import(FirestoreModule.create())
        import(FirebaseModule.create())
    }

    override fun onCreate() {
        super.onCreate()

        appInstance = this
        Timber.plant(Timber.DebugTree())

        val dispatcher: Dispatcher by instance<Dispatcher>()
        val stores: Set<FluxyStore<*>> by instance<Set<FluxyStore<*>>>()
        dispatcher.stores = stores.toList()
    }

}