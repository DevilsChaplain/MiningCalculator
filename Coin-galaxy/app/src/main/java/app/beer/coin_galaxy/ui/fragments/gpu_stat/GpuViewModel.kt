package app.beer.coin_galaxy.ui.fragments.gpu_stat

import android.app.Application
import androidx.lifecycle.*
import app.beer.coin_galaxy.data.repository.GpuRepository
import app.beer.coin_galaxy.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GpuViewModel(
    private val app: Application
) : AndroidViewModel(app) {

    private val gpuRepository = GpuRepository()

    private val _gpusLiveData: MutableLiveData<ResourceState<List<Gpu>>> = MutableLiveData()
    val gpusLiveData: LiveData<ResourceState<List<Gpu>>> = _gpusLiveData

    init {
        getGpus()
    }

    private fun getGpus() {
        _gpusLiveData.postValue(ResourceState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            _gpusLiveData.postValue(ResourceState(data = gpuRepository.getGpuItems(app)))
        }
    }

}