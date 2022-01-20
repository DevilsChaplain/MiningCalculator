package app.beer.coin_galaxy.data.repository

import android.util.Log
import app.beer.coin_galaxy.data.model.Crypto
import app.beer.coin_galaxy.utils.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import java.lang.Exception

class CryptoRepository {

    private var triedLoadAgain = false

    /**
     * некоторые имена картинок криптовалют имею другую форму чем остальные
     * */
    private val customIconsNames = hashMapOf<String, String>(
        "Dai" to "multi-collateral-dai",
        "Terra" to "terra-luna",
        "Polkadot" to "polkadot-new",
        "Elrond" to "elrond-egld"
    )

    /**
     * Парсит coinmarketcup.com для показа криптовалют
     * */
    fun getCryptocurrencies(onSuccess: (List<Crypto>) -> Unit) {
        try {
            val cryptosList = mutableListOf<Crypto>()
            // получаем страницу и начинаем парсить ее и результат записываес в список
            val document = Jsoup.connect("https://crypto.com/price").get()
            val cryptos = document.getElementsByTag("table")[0]
                .getElementsByTag("tbody")[0]
                .getElementsByTag("tr")
            cryptos.forEach { row ->
                val name = row.getElementsByClass("chakra-text")[0].text()
                val symbol = row.getElementsByClass("chakra-text")[1].text()
                val price = row.select("div[class=\"css-16q9pr7\"] > div[class=\"css-0\"]").text()
                val changes = row.select("div[class=\"css-16q9pr7\"] > p")[0].text()
                val logoName = if (customIconsNames.contains(name))
                    customIconsNames[name]
                else
                    name.replace("[. ]".toRegex(), "-").lowercase()

                val crypto = Crypto(
                    name = name,
                    symbol = symbol,
                    price = price,
                    logo = "https://static.crypto.com/token/icons/$logoName/color_icon.png?w=48&q=75",
                    rank = "1",
                    changes = changes
                )
                cryptosList.add(
                    crypto
                )
            }
            onSuccess(cryptosList)
        } catch (e: Exception) {
            e.printStackTrace()
            if (!triedLoadAgain) {
                triedLoadAgain = true
                getCryptocurrencies(onSuccess)
            } else
                onSuccess(emptyList())
        }
    }

}