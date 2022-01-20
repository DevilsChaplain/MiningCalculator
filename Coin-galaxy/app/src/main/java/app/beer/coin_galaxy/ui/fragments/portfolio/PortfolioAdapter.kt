package app.beer.coin_galaxy.ui.fragments.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.data.model.PortfolioItem
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

class PortfolioAdapter : RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {

    private var portfolioItemItems: ArrayList<PortfolioItem> = ArrayList()
    var onItemLongClickListener: (PortfolioItem) -> Unit = { }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val totalPrice: TextView = itemView.findViewById(R.id.total_price)

        fun bind(portfolioItemItem: PortfolioItem) {
            name.text = portfolioItemItem.name.toUpperCase(Locale.getDefault())
            quantity.text = portfolioItemItem.quantity.toString()
            totalPrice.text = "\$ ${round(portfolioItemItem.price)}"

            itemView.setOnLongClickListener {
                onItemLongClickListener(portfolioItemItem)
                true
            }
        }
    }

    fun setPortfolioItems(items: List<PortfolioItem>) {
        portfolioItemItems.clear()
        notifyDataSetChanged()
        portfolioItemItems.addAll(items)
        notifyItemRangeInserted(0, portfolioItemItems.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.portfolio_item, parent, false)
    )

    override fun getItemCount() = portfolioItemItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val portfolioItem = portfolioItemItems[position]
        holder.bind(portfolioItem)
    }

}