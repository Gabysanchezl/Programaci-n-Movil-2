package com.example.android.dessertpusher

    import android.app.Application
    import android.view.View
    import timber.log.Timber

class PusherAplication: Application(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree()

        )
    }
}