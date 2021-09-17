package dev.jahidhasanco.quoteapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.jahidhasanco.quoteapp.data.models.Result

@Database(entities = [Result::class],version = 2)
abstract class QuotesDatabase: RoomDatabase() {

    abstract fun quoteDao() : QuoteDao
    companion object{
        @Volatile
        private var INSTANCE: QuotesDatabase? = null

        fun getDatabase(context: Context): QuotesDatabase {
            if (INSTANCE == null) {
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,
                        QuotesDatabase::class.java,
                        "quoteDB")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}