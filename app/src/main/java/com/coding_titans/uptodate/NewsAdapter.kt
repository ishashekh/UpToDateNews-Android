package com.coding_titans.uptodate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class NewsAdapter(private val newsList: List<NewsArticle>, private val onArticleClickListener: (String) -> Unit) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.newsTitleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.newsDescriptionTextView)
        val imageView: ImageView = itemView.findViewById(R.id.newsImageView)
        val itemLayout: LinearLayout = itemView.findViewById(R.id.newsItemLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsArticle = newsList[position]
        holder.titleTextView.text = newsArticle.title
        holder.descriptionTextView.text = newsArticle.description
        Glide.with(holder.itemView.context)
            .load(newsArticle.image)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.imageView)

        holder.itemLayout.setOnClickListener {
            onArticleClickListener(newsArticle.url)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}


