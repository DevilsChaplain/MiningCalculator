package app.beer.coin_galaxy.data.repository

import app.beer.coin_galaxy.data.database.PortfolioDao
import app.beer.coin_galaxy.data.model.PortfolioItem

class PortfolioRepository(private val portfolioDao: PortfolioDao) {

    fun getPortfolioItems(): List<PortfolioItem> {
        return portfolioDao.getAllPortfolioItems()
    }

    fun deletePortfolioItem(portfolioItem: PortfolioItem) {
        portfolioDao.deletePortfolioItem(portfolioItem)
    }

}