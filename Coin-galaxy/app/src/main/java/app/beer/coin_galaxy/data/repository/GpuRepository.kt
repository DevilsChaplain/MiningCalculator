package app.beer.coin_galaxy.data.repository

import android.content.Context
import android.util.Log
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.ui.fragments.gpu_stat.Gpu
import org.jsoup.Jsoup

class GpuRepository {

    /**
     * Получает данные с сайта о видеокартах (стоимость карты, сколько приносит, доход в месяц)
     * */
    fun getGpuItems(context: Context): List<Gpu> {
        Log.d("LOG_D_DATA", "getGpuItems: ")
        val gpusList = mutableListOf<Gpu>()
        val document = Jsoup.connect("https://www.kryptex.org/ru/best-gpus-for-mining").get()
        val gpus = document.getElementsByClass("tr-link cursor-pointer")
        gpus.forEach { gpuRow ->
            gpuRow.children().let { children ->
                val name = children[0].text()
                val price = children[1].text()
                val ethMiningData = children[2].text()
                val etcMiningData = children[3].text()
                val expMiningData = children[4].text()
                val ubqMiningData = children[5].text()
                val rvnMiningData = children[6].text()
                val beanMiningData = children[7].text()
                val montlyIncome = children[8].select("span[class=\"monthly\"]").text()
                val payback = children[9].text().split(" ")[0]
                val paybackText = context.resources.getQuantityText(R.plurals.days, payback.toInt())
                gpusList.add(
                    Gpu(
                        name = name,
                        price = price,
                        monthIncome = montlyIncome,
                        miningData = hashMapOf(
                            "eth" to ethMiningData,
                            "etc" to etcMiningData,
                            "exp" to expMiningData,
                            "ubq" to ubqMiningData,
                            "rvn" to rvnMiningData,
                            "bean" to beanMiningData
                        ),
                        payback = "$payback $paybackText"
                    )
                )
            }
        }
        return gpusList
    }

}