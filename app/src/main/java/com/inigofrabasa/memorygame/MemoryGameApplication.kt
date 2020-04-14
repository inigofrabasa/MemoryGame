package com.inigofrabasa.memorygame

import android.app.Application

class MemoryGameApplication : Application(){
    var appContext = this

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MemoryGameApplication
            private set
    }
}