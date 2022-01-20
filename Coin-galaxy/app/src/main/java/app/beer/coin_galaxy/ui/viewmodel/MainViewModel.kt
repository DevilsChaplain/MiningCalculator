package app.beer.coin_galaxy.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.beer.coin_galaxy.data.database.Database
import app.beer.coin_galaxy.data.repository.CryptoRepository
import app.beer.coin_galaxy.data.model.Crypto
import app.beer.coin_galaxy.data.model.PortfolioItem
import app.beer.coin_galaxy.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    var database: Database = Database.newInstance(app)

    fun deletePortfolioItem(portfolioItemItem: PortfolioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            database.getPortfolioDao().deletePortfolioItem(portfolioItemItem)
        }
    }

}