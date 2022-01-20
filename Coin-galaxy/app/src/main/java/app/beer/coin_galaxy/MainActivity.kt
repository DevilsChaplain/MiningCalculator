package app.beer.coin_galaxy

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import app.beer.coin_galaxy.ui.fragments.gpu_stat.GpuStatFragment
import app.beer.coin_galaxy.ui.fragments.items.ItemsFragment
import app.beer.coin_galaxy.ui.fragments.news.NewsFragment
import app.beer.coin_galaxy.ui.fragments.portfolio.UserPortfolioFragment
import app.beer.coin_galaxy.utils.Constants
import app.beer.coin_galaxy.utils.replaceFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    lateinit var bottomNav: BottomNavigationView
    var dLocale: Locale? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("Coin_galaxy_sh", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString(Constants.CURRENT_LANGUAGE_KEY, "")
        val change = if (language == "English") "en" else "ru"
        dLocale = Locale(change)
        updateConfig()

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        replaceFragment(ItemsFragment.newInstance())

        // находим нижнию навигацию + ставим слушатель кликов на нем
        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_usd -> replaceFragment(ItemsFragment.newInstance())
                R.id.nav_gpus -> replaceFragment(GpuStatFragment())
                R.id.nav_portfolio -> replaceFragment(
                    UserPortfolioFragment.newInstance(),
                    id = "portfolio_fragment"
                )
                R.id.nav_news -> replaceFragment(NewsFragment.newInstance())
            }
            true
        }
    }

    /**
     * Обновляем язык
     * */
    private fun updateConfig() {
        if (dLocale == Locale(""))
            return
        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        resources.updateConfiguration(configuration, null)
    }

}