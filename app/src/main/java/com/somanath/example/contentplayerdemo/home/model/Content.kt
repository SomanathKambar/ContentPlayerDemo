package com.somanath.example.contentplayerdemo.home.player


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Content(
    @SerializedName("MediaItems")
    val mediaItems: List<MediaItem>
)