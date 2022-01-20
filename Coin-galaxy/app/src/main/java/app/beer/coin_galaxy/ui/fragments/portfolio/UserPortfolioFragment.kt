package app.beer.coin_galaxy.ui.fragments.portfolio

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.coin_galaxy.MainActivity
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.data.database.Database
import app.beer.coin_galaxy.data.model.PortfolioItem
import app.beer.coin_galaxy.ui.fragments.portfolio.add.AddPortfolioItemFragment
import app.beer.coin_galaxy.utils.*
import java.util.*
import kotlin.collections.ArrayList

class UserPortfolioFragment : Fragment() {

    private lateinit var vm: PortfolioViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private val items = ArrayList<PortfolioItem>()
    private lateinit var recyclerView: RecyclerView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(activity as MainActivity)[PortfolioViewModel::class.java]
        sharedViewModel = ViewModelProvider(activity as MainActivity)[SharedViewModel::class.java]

        setHasOptionsMenu(true)

        activity?.let {
            (it as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            sharedPreferences = it.getSharedPreferences("Coin_galaxy_sh", Context.MODE_PRIVATE)
        }

        recyclerView = view.findViewById(R.id.portfolio_recycler_view)
        adapter = PortfolioAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter.onItemLongClickListener = { portfolioItem ->
            vm.deletePortfolioItem(portfolioItem)
            vm.deletedIds.add(portfolioItem.id ?: 0)
        }

        vm.portfolioItemDeleteLiveData.observe(viewLifecycleOwner, { portfolioItemEvent ->
            portfolioItemEvent?.getContentIfNotHandled()?.let { portfolioItem ->
                items.remove(portfolioItem)
                adapter.setPortfolioItems(items)
                showToast("Вы удалили элемент из портфолио")
            }
        })

        vm.portfolioItemLiveData.observe(viewLifecycleOwner, { portfolioItems ->
            portfolioItems.filter { !vm.deletedIds.contains(it.id) }.forEach { item ->
                val price = sharedPreferences.getString(
                    "price_${item.name.uppercase()}",
                    null
                )
                if (price != null) {
                    item.price = price.replace("[$,]".toRegex(), "").toDouble() * item.quantity
                }
                if (!items.contains(item)) {
                    items.add(item)
                }
            }
            adapter.setPortfolioItems(items)
        })

        sharedViewModel.addItemLiveData.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { itemData ->
                addItem(itemData)
            }
        })
    }

    private fun addItem(newPortfolioItem: String) {
        // если был добавлне новый элемент портфолио то мы его добавляем
        val portfolioData = newPortfolioItem.split(",")
        val item = PortfolioItem(
            name = portfolioData[0],
            quantity = portfolioData[1].toDouble(),
            price = portfolioData[2].toDouble()
        )
        Log.d("LOG_D_F", "addItem: $item")
        if (!items.contains(item)) {
            vm.refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_portfolio, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_portfolio_item) {
            replaceFragment(AddPortfolioItemFragment(), true)
        }
        return true
    }

    companion object {
        lateinit var adapter: PortfolioAdapter

        fun newInstance(): Fragment {
            val args = Bundle()
            val fragment = UserPortfolioFragment()
            fragment.arguments = args
            return fragment
        }
    }

}