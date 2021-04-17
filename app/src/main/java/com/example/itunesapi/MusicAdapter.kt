package com.example.itunesapi

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_music.view.*

class MusicAdapter(private val clickAction: (Int) -> Unit): RecyclerView.Adapter<MusicViewHolder>() {
    private var dataList: List<MusicUIData> = listOf()

    fun setData(data: List<MusicUIData>) {
        dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = dataList[position].music
        holder.itemView.run {
            collectionName.text = music.collectionName
            trackName.text = music.trackName
            Picasso.get().load(music.imageUrl).into(image)
            icon.setImageDrawable(getDrawable(
                if (dataList[position].isPlaying) R.drawable.ic_baseline_stop_24 else R.drawable.ic_baseline_play_arrow_24,
                context
            ))

            iconLayout.setOnClickListener {
                clickAction.invoke(music.id)
            }
        }
    }

    private fun getDrawable(@DrawableRes resource: Int, context: Context): Drawable? {
        return ContextCompat.getDrawable(context, resource)
    }

}

class MusicViewHolder(view: View): RecyclerView.ViewHolder(view)