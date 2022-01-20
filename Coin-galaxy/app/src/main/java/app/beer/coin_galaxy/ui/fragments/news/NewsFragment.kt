package app.beer.coin_galaxy.ui.fragments.news

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.data.model.Article
import app.beer.coin_galaxy.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class NewsFragment : Fragment() {

    private var page = 1
    private var baseUrl = "https://ru.investing.com/news/cryptocurrency-news/$page"

    private val news: ArrayList<Article> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: NewsAdapter

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.news_recycler_view)
        adapter = NewsAdapter()
        layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        getNews()
    }

    private fun getNews() {
        progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            getNewsFromHTML()
        }
    }

    private fun getNewsFromHTML() {
        try {
            val document = Jsoup.connect(baseUrl).get()
            val element = document.select("article[class=\"js-article-item articleItem\"]")

            for (i in 0 until element.size) {
                val id = element.eq(i).attr("data-id")

                val title = element.select("div[class=textDiv]")
                    .select("a[class=title]")
                    .eq(i)
                    .text()

                val date = element.select("div[class=textDiv]")
                    .select("span[class=articleDetails]")
                    .select("span[class=date]")
                    .eq(i)
                    .text()

                val url = element.select("div[class=textDiv]")
                    .select("a[class=title]")
                    .eq(i)
                    .attr("href")

                news.add(Article(id, title, "", date, url))
            }
            GlobalScope.launch(Dispatchers.Main) {
                adapter.setNews(news)
                progressBar.visibility = View.GONE
            }
        } catch (e: Exception) {
            activity?.runOnUiThread { showToast("Не удалось получить документ! Попробуйте ещё раз") }
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}