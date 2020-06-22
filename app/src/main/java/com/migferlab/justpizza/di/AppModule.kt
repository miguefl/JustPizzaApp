package com.migferlab.justpizza.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.hoopcarpool.fluxy.FluxyStore
import com.migferlab.justpizza.BuildConfig
import com.migferlab.justpizza.app
import com.migferlab.justpizza.core.AppLogger
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.setBinding
import org.kodein.di.singleton
import timber.log.Timber


object AppModule {
    fun create() = DI.Module("App", true) {
        bind<Application>() with singleton { app }
        bind() from setBinding<FluxyStore<*>>()
        bind<ViewModelProvider.Factory>() with singleton {
            DIViewModelFactory(
                di.direct
            )
        }
        bind<Boolean>(tag = "debug") with singleton { BuildConfig.DEBUG }
        bind<AppLogger>() with singleton {
            object : AppLogger {
                override fun v(tag: String, msg: String) = Timber.tag(tag).v(msg)
                override fun d(tag: String, msg: String) = Timber.tag(tag).d(msg)
                override fun i(tag: String, msg: String) = Timber.tag(tag).i(msg)
                override fun w(tag: String, msg: String) = Timber.tag(tag).w(msg)
                override fun e(tag: String, msg: String) = Timber.tag(tag).e(msg)
            }
        }
    }
}