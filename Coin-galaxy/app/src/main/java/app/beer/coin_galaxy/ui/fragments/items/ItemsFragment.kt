package app.beer.coin_galaxy.ui.fragments.items

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.coin_galaxy.MainActivity
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.data.model.Crypto
import app.beer.coin_galaxy.ui.viewmodel.MainViewModel
import app.beer.coin_galaxy.utils.Constants
import app.beer.coin_galaxy.utils.showToast

class ItemsFragment : Fragment() {

    private lateinit var vm: CryptoViewModel

    private val cryptoItems: ArrayList<Crypto> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemsAdapter

    private lateinit var progressBar: ProgressBar

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(activity as MainActivity)[CryptoViewModel::class.java]
        sharedPreferences =
            requireActivity().getSharedPreferences("Coin_galaxy_sh", Context.MODE_PRIVATE)

        setHasOptionsMenu(true)

        progressBar = view.findViewById(R.id.progress_bar)

        recyclerView = view.findViewById(R.id.items_recycler_view)
        adapter = ItemsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        vm.cryptoItemsLiveData.observe(viewLifecycleOwner, Observer { state ->
            progressBar.isVisible = state.isLoading
            if (!state.isLoading) {
                state.data?.let {
                    if (it.isEmpty()) {
                        showToast("Что-то пошло не так")
                    } else {
                        adapter.setCryptoItems(it)
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.munu_items, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.change_language) {
            ChangeLanguageFragment.newInstance(
                sharedPreferences.getString(
                    Constants.CURRENT_LANGUAGE_KEY,
                    ""
                ) ?: "", requireActivity().supportFragmentManager
            )
        } else if (item.itemId == R.id.refresh) {
            vm.getCryptocurrencies()
        }
        return true
    }

    companion object {
        fun newInstance(): Fragment = ItemsFragment()
    }

}