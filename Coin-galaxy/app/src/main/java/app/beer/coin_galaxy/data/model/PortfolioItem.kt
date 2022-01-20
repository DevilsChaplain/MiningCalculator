package app.beer.coin_galaxy.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class PortfolioItem(
    var name: String = "",
    var quantity: Double = 0.0,
    var price: Double = 0.0,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)