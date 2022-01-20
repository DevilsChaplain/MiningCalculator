package app.beer.coin_galaxy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.beer.coin_galaxy.data.model.PortfolioItem

/**
 *  Класс отвечающий за локальную базу данных
 */
@Database(entities = [PortfolioItem::class], version = 3, exportSchema = false)
abstract class Database : RoomDatabase() {

    companion object {
        private var instance: app.beer.coin_galaxy.data.database.Database? = null

        fun newInstance(context: Context): app.beer.coin_galaxy.data.database.Database {
            if (instance == null) {
                instance = Room.databaseBuilder(context, app.beer.coin_galaxy.data.database.Database::class.java, "portfolio")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun getPortfolioDao(): PortfolioDao

}