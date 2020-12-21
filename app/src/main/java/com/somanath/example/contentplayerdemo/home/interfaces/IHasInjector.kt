package java.com.somanath.example.contentplayerdemo.home.interfaces

import android.app.Activity
import dagger.android.AndroidInjector

interface IHasInjector {
    fun activityInjector() : AndroidInjector<Activity>
}