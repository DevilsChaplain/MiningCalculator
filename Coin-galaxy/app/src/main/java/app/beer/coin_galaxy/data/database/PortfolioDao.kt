package app.beer.coin_galaxy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import app.beer.coin_galaxy.data.model.PortfolioItem

@Dao
interface PortfolioDao {

    @Insert
    fun insertPortfolioItem(item: PortfolioItem)

    @Delete(entity = PortfolioItem::class)
    fun deletePortfolioItem(item: PortfolioItem)

    @Query("SELECT * FROM portfolio")
    fun getAllPortfolioItems(): List<PortfolioItem>

}