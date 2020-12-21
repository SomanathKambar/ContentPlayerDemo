package java.com.somanath.example.contentplayerdemo.home.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app
}