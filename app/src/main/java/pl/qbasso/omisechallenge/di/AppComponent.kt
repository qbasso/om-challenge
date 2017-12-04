package pl.qbasso.omisechallenge.di

import dagger.Component
import pl.qbasso.omisechallenge.ui.activity.CharityList
import pl.qbasso.omisechallenge.ui.activity.DonateActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
interface AppComponent {
    fun inject(activity: CharityList)
    fun inject(activity: DonateActivity)
}