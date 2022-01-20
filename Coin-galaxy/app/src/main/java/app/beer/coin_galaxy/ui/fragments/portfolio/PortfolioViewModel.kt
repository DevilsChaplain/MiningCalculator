package app.beer.coin_galaxy.ui.fragments.portfolio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.beer.coin_galaxy.data.database.Database
import app.beer.coin_galaxy.data.model.Crypto
import app.beer.coin_galaxy.data.model.PortfolioItem
import app.beer.coin_galaxy.data.repository.PortfolioRepository
import app.beer.coin_galaxy.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PortfolioViewModel(app: Application) : AndroidViewModel(app) {

    private val portfolioRepository =
        PortfolioRepository(Database.newInstance(app).getPortfolioDao())

    private val _portfolioItemLiveData: MutableLiveData<List<PortfolioItem>> = MutableLiveData()
    val portfolioItemLiveData: LiveData<List<PortfolioItem>> = _portfolioItemLiveData

    private val _portfolioItemDeleteLiveData: MutableLiveData<Event<PortfolioItem>?> =
        MutableLiveData()
    val portfolioItemDeleteLiveData: LiveData<Event<PortfolioItem>?> = _portfolioItemDeleteLiveData

    val deletedIds = mutableListOf<Int>()

    init {
        getPortfolioItems()
    }

    private fun getPortfolioItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _portfolioItemLiveData.postValue(portfolioRepository.getPortfolioItems())
        }
    }

    fun addPortfolioItem(portfolioItem: PortfolioItem) {
        _portfolioItemLiveData.value =
            _portfolioItemLiveData.value?.toMutableList()?.apply { add(portfolioItem) }
    }

    fun refresh() {
        getPortfolioItems()
    }

    fun deletePortfolioItem(portfolioItem: PortfolioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            portfolioRepository.deletePortfolioItem(portfolioItem)
            _portfolioItemDeleteLiveData.postValue(Event(portfolioItem))
        }
    }

}