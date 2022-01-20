package app.beer.coin_galaxy.ui.fragments.portfolio.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.beer.coin_galaxy.MainActivity
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.data.model.Crypto
import app.beer.coin_galaxy.data.model.PortfolioItem
import app.beer.coin_galaxy.ui.fragments.items.CryptoViewModel
import app.beer.coin_galaxy.ui.fragments.portfolio.SharedViewModel
import app.beer.coin_galaxy.utils.*
import com.google.android.material.button.MaterialButton
import java.util.*

class AddPortfolioItemFragment : Fragment() {

    private lateinit var vm: CryptoViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var nameCryptoSpinner: AppCompatSpinner
    private lateinit var quantityEditText: EditText
    private lateinit var addPortfolioItemBtn: MaterialButton

    private lateinit var progressBar: ProgressBar

    var name: String = ""
    var quantity: Double = 0.0
    var totalPrice: Double = 0.0

    private val cryptos = mutableListOf<Crypto>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_portfolio_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(activity as MainActivity)[CryptoViewModel::class.java]
        sharedViewModel = ViewModelProvider(activity as MainActivity)[SharedViewModel::class.java]

        cryptos.clear()
        cryptos.addAll(vm.cryptoItemsLiveData.value?.data ?: emptyList())

        activity?.let {
            it as MainActivity
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.toolbar.setNavigationOnClickListener { _ -> it.supportFragmentManager.popBackStack() }
        }

        nameCryptoSpinner = view.findViewById(R.id.name_crypto)
        quantityEditText = view.findViewById(R.id.quantity)
        addPortfolioItemBtn = view.findViewById(R.id.add_portfolio_item_btn)
        progressBar = view.findViewById(R.id.progress_bar)

        nameCryptoSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            cryptos.map { it.symbol }
        )

        nameCryptoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                name = cryptos[position].symbol
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // сохраняем элемент портфолио и отправляем на фрагмент с монетами что был добавлен элементами
        addPortfolioItemBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            if (name.isNotEmpty()) {
                if (quantityEditText.text.isNotEmpty()) {
                    vm.getPrice(name.uppercase(Locale.getDefault()))?.also { price ->
                        quantity = quantityEditText.text.toString().toDouble()
                        totalPrice = price * quantity
                        vm.addPortfolioItem(
                            PortfolioItem(
                                name,
                                quantity,
                                totalPrice
                            )
                        )
                        progressBar.visibility = View.GONE
                        (activity as MainActivity?)?.supportFragmentManager?.popBackStack()
                        sharedViewModel.itemAdded("$name,$quantity,$totalPrice")
                        showToast("Вы удачно добавили криптовалюту в портфолио")
                    } ?: showToast("Не удалось найти криптовалюту")
                } else {
                    showToast("Введите количество вашей криптовалюты")
                    progressBar.visibility = View.GONE
                }
            } else {
                showToast("Введите название вашей криптовалюты")
                progressBar.visibility = View.GONE
            }
        }
    }

}