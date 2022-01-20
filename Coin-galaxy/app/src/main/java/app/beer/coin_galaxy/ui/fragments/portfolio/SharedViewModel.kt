package app.beer.coin_galaxy.ui.fragments.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.beer.coin_galaxy.utils.Event

class SharedViewModel : ViewModel() {

    private val _addItemLiveData: MutableLiveData<Event<String>> = MutableLiveData()
    val addItemLiveData: LiveData<Event<String>> = _addItemLiveData

    fun itemAdded(itemData: String) {
        _addItemLiveData.value = Event(itemData)
    }

}