package app.beer.coin_galaxy.ui.fragments.items

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import app.beer.coin_galaxy.data.database.Database
import app.beer.coin_galaxy.data.model.Crypto
import app.beer.coin_galaxy.data.model.PortfolioItem
import app.beer.coin_galaxy.data.repository.CryptoRepository
import app.beer.coin_galaxy.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CryptoViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = CryptoRepository()
    var database: Database = Database.newInstance(app)

    private val _cryptoItemsLiveData: MutableLiveData<ResourceState<List<Crypto>>> =
        MutableLiveData()
    val cryptoItemsLiveData: LiveData<ResourceState<List<Crypto>>> = _cryptoItemsLiveData

    init {
        getCryptocurrencies()
    }

    fun getCryptocurrencies() {
        _cryptoItemsLiveData.value = ResourceState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCryptocurrencies { cryptos ->
                _cryptoItemsLiveData.postValue(ResourceState(data = cryptos))
            }
        }
    }

    fun getPrice(name: String): Double? {
        if (_cryptoItemsLiveData.value == null)
            getCryptocurrencies()
        return _cryptoItemsLiveData.value?.data?.find { it.symbol == name }?.price?.replace("""[$,]""".toRegex(), "")?.toDouble()
    }

    fun addPortfolioItem(portfolioItemItem: PortfolioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            database.getPortfolioDao().insertPortfolioItem(portfolioItemItem)
        }
    }

}