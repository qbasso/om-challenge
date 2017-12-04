package pl.qbasso.omisechallenge

import android.app.Application
import pl.qbasso.omisechallenge.di.AppComponent
import pl.qbasso.omisechallenge.di.AppModule
import pl.qbasso.omisechallenge.di.DaggerAppComponent
import pl.qbasso.omisechallenge.di.NetworkModule

class ChallengeApp : Application() {

    lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).networkModule(NetworkModule()).build()
    }
}