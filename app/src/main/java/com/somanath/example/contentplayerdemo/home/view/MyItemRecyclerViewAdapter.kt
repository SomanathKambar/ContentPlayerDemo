package com.somanath.example.contentplayerdemo.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.text.input.INVALID_SESSION
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.somanath.example.contentplayerdemo.R
import com.somanath.example.contentplayerdemo.home.model.MediaItem
import com.somanath.example.contentplayerdemo.home.utility.Util
import com.somanath.example.contentplayerdemo.home.view.dummy.DummyContent.DummyItem
import java.com.somanath.example.contentplayerdemo.home.interfaces.IDurationCallBack
import java.com.somanath.example.contentplayerdemo.home.interfaces.IOnItemClickHandler
import javax.inject.Inject

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter @Inject constructor(
    private val values: List<MediaItem>,  private val iOnItemClickHandler: IOnItemClickHandler)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        holder.title.text = item.title
        holder.duration.let {
            Util.getDuration(item.url, object : IDurationCallBack {
                override fun onDurationRetrieved(duration: String) {
                    it.text = duration
                }
            })
        }
        Glide.with(holder.itemView.context).load(item.image).into(holder.image)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        val title: TextView = view.findViewById(R.id.title)
        val duration: TextView = view.findViewById(R.id.duration)
        val image: ImageView = view.findViewById(R.id.image)
        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            val url = values.get(adapterPosition).url
            iOnItemClickHandler.onItemClick(url)
        }
    }
}