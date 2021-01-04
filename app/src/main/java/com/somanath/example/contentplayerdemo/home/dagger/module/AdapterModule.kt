package java.com.somanath.example.contentplayerdemo.home.dagger.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class AdapterModule(/*private val list : List<MediaItem>*/) {

//    @Provides
//    fun getItemList(): List<MediaItem> = list
}