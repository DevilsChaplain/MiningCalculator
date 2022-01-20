package app.beer.coin_galaxy.ui.fragments.items

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.data.model.Crypto
import app.beer.coin_galaxy.utils.loadAndSetImage

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private var cryptoItems = ArrayList<Crypto>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pairImage: ImageView = itemView.findViewById(R.id.pair_image)
        val pairName: TextView = itemView.findViewById(R.id.pair_name)
        val price: TextView = itemView.findViewById(R.id.price)
        val changes: TextView = itemView.findViewById(R.id.changes)

        fun bind(crypto: Crypto) {
            pairImage.loadAndSetImage(crypto.logo)
            pairName.text = crypto.name
            price.text = crypto.price
            if (crypto.changes.startsWith("-")) {
                changes.setBackgroundColor(itemView.context.resources.getColor(R.color.red))
            } else {
                changes.setBackgroundColor(itemView.context.resources.getColor(R.color.green))
            }
            changes.text = crypto.changes
        }
    }

    fun setCryptoItems(items: List<Crypto>) {
        cryptoItems.clear()
        cryptoItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
    )

    override fun getItemCount() = cryptoItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crypto = cryptoItems[position]
        holder.bind(crypto)
    }

}