package java.com.somanath.example.contentplayerdemo.home

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import java.com.somanath.example.contentplayerdemo.home.dagger.MainComponent
import java.com.somanath.example.contentplayerdemo.home.interfaces.IHasInjector
import javax.inject.Inject


class ContentPlayerApp: Application(), IHasInjector {

    @Inject
    var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null

    override fun onCreate() {
        super.onCreate()
//        DaggerAppComponent
//            .builder()
//            .application(this)
//            .build()
//            .inject(this)
    }
    override fun activityInjector(): AndroidInjector<Activity> {
        TODO("Not yet implemented")
    }
}