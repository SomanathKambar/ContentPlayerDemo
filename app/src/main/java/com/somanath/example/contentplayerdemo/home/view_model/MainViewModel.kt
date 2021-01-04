package com.somanath.example.contentplayerdemo.home.view_model

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.somanath.example.contentplayerdemo.home.model.Content
import com.somanath.example.contentplayerdemo.home.utility.Util
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.com.somanath.example.contentplayerdemo.home.interfaces.IMainViewModel

class MainViewModel @ViewModelInject constructor(app : Application) : AndroidViewModel(app) , IMainViewModel{

    private var mContent : MutableLiveData<Content>? = null

    override fun getContents(): MutableLiveData<Content> {
        if(mContent == null){
            mContent = MutableLiveData<Content>()
            updateContent()
        }
        return mContent!!
    }

    override fun updateContent() {
      Single.fromCallable {
            Gson().fromJson(
                Util.getJsonFromAssets(getApplication(), Util.CONTENT_FILE_PATH),
                Content::class.java
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Content?> {

                override fun onError(e: Throwable) {}
                override fun onSuccess(t: Content?) {
                    mContent?.value = t
                }

                override fun onSubscribe(d: Disposable?) {
                }
            })

    }
}
