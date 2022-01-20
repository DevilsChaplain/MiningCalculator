package app.beer.coin_galaxy.utils

data class ResourceState<T>(
    val isLoading: Boolean = false,
    val data: T? = null
)