package java.com.somanath.example.contentplayerdemo.home.dagger.module

import com.somanath.example.contentplayerdemo.home.model.MediaItem
import dagger.Module
import dagger.Provides

@Module
class AdapterModule(private val list : List<MediaItem>) {

    @Provides
    fun getItemList(): List<MediaItem> = list
}