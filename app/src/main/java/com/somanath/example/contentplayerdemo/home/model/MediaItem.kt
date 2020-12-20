package com.somanath.example.contentplayerdemo.home.player


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class MediaItem(
    @SerializedName("Image")
    val image: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Url")
    val url: String
)