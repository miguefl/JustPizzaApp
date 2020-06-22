package com.migferlab.justpizza.di

import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Logger
import com.hoopcarpool.timberlogger.TimberLogger
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object FluxModule {
    fun create() = DI.Module("Fluxy", true) {
        bind<Logger>() with singleton { TimberLogger() }
        bind<Dispatcher>() with singleton { Dispatcher(instance()) }
    }
}