package java.com.somanath.example.contentplayerdemo.home.dagger.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.com.somanath.example.contentplayerdemo.home.view_model.MainRepository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule{
//    @Provides
//    @Singleton
//    fun provideContext(): Context = Application

    @Provides
    @Singleton
    fun provideMainRepository() = MainRepository()
}