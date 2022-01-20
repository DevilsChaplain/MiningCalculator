package app.beer.coin_galaxy.ui.fragments.news

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.data.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var news: ArrayList<Article> = ArrayList()

    fun setNews(items: List<Article>) {
        val pos = news.size
        news.addAll(items)
        notifyItemRangeInserted(pos, news.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = news[position]
        holder.bind(article)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newName: TextView = itemView.findViewById(R.id.new_name)
        val newDate: TextView = itemView.findViewById(R.id.new_date)

        fun bind(article: Article) {
            newName.text = article.title
            newDate.text = article.date
            itemView.setOnClickListener {
                val url = if (article.url.startsWith("/")) "https://ru.investing.com" + article.url else article.url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                itemView.context.startActivity(intent)
            }
        }
    }

}