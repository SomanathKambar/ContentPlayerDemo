package java.com.somanath.example.contentplayerdemo.home.dagger

import android.app.Application
import com.somanath.example.contentplayerdemo.home.view.MyItemRecyclerViewAdapter
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import java.com.somanath.example.contentplayerdemo.home.dagger.module.AdapterModule
import java.com.somanath.example.contentplayerdemo.home.dagger.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AdapterModule::class, AppModule::class])
interface MainComponent {
     fun injectAdapter(adapter: MyItemRecyclerViewAdapter)
     fun injectApp(app: Application?)

    @Component.Builder
    interface Builder {
        fun application(app: Application?): Builder?
        fun adapter(adapter: MyItemRecyclerViewAdapter): Builder?
        fun build(): MainComponent?
    }
}