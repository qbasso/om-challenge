package pl.qbasso.omisechallenge.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.qbasso.omisechallenge.ui.adapter.CharityAdapter

@Module
class AppModule(private val application: Application) {

    @Provides
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun provideCharityAdapter(): CharityAdapter = CharityAdapter(listOf())

}
