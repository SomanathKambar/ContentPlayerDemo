package com.somanath.example.contentplayerdemo.home.utility

import android.content.Context
import android.media.MediaMetadataRetriever
import com.google.gson.Gson
import com.somanath.example.contentplayerdemo.home.model.Content
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.com.somanath.example.contentplayerdemo.home.interfaces.IDurationCallBack
import java.io.IOException


class Util {
    companion object {
        const val CONTENT_FILE_PATH = "media_item.json"
        fun getJsonFromAssets(context: Context, fileName: String): String? {

            val json: String?
            try {
                json = context.assets.open(fileName).bufferedReader().use {
                    it.readText()
                }
            } catch (e: IOException) {
                return null
            }
            return json
        }

        @JvmStatic
        fun getContentFromAssets(context: Context, fileName: String) {
            var content: Content? = null
            var disposable: Disposable? = null
            return Single.fromCallable {
                Gson().fromJson(
                    Util.getJsonFromAssets(context, fileName),
                    Content::class.java
                )
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Content?> {

                    override fun onError(e: Throwable) {
                        disposable?.dispose()
                    }

                    override fun onSuccess(t: Content?) {
                        content = t
                        disposable?.dispose()
                    }

                    override fun onSubscribe(d: Disposable?) {
                        disposable = d
                    }
                })
        }

        fun getDuration(url: String, callBack: IDurationCallBack) {
            var disposable: Disposable? = null
            Single.fromCallable(fun(): String {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(url, HashMap())
                val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val timeInMillisec = time!!.toLong()
                retriever.release()
                return convertMillieToHMmSs(timeInMillisec)
            })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<String> {

                    override fun onError(e: Throwable) {
                        disposable?.dispose()
                    }

                    override fun onSuccess(t: String) {
                        callBack.onDurationRetrieved(t)
                        disposable?.dispose()
                    }

                    override fun onSubscribe(d: Disposable?) {
                        disposable = d
                    }
                })
        }

        fun convertMillieToHMmSs(millie: Long): String {
            val seconds = millie / 1000
            val second = seconds % 60
            val minute = seconds / 60 % 60
            val hour = seconds / (60 * 60) % 24
            val result = ""
            return if (hour > 0) {
                String.format("%02d:%02d:%02d", hour, minute, second)
            } else {
                String.format("%02d:%02d", minute, second)
            }
        }
    }
}