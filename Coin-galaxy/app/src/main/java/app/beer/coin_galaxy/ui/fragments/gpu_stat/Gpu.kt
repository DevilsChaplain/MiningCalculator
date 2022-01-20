package app.beer.coin_galaxy.ui.fragments.gpu_stat

data class Gpu(
    val name: String,
    val miningData: Map<String, String> = hashMapOf(),
    val monthIncome: String,
    val price: String,
    val payback: String
)