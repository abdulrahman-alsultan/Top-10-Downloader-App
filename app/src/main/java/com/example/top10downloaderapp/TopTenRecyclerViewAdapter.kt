package com.example.top10downloaderapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.text.Layout
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.top_ten_item_row.view.*

class TopTenRecyclerViewAdapter(private val topApp: List<AppData>) :
    RecyclerView.Adapter<TopTenRecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.top_ten_item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val app = topApp[position]

        holder.itemView.apply {
            Picasso.get().load(app.image).into(app_image)
            app_name.text = app.title

            app_info.setOnClickListener {
                val intent = Intent(holder.itemView.context, Describe::class.java)
                intent.putExtra("name", app.title)
                intent.putExtra("describe", app.summary)

                holder.itemView.context.startActivity(intent)
            }

            app_download.setOnClickListener {
                holder.itemView.context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(app.link)
                    )
                )
            }


        }
    }

    override fun getItemCount(): Int = topApp.size
}