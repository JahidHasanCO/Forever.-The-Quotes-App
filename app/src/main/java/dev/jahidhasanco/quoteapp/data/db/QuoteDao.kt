package dev.jahidhasanco.quoteapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.jahidhasanco.quoteapp.data.models.Result

@Dao
interface QuoteDao{

    @Insert
    suspend fun addQuote(quotes: List<Result>)

    @Query("SELECT * FROM quote")
    suspend fun getQuotes(): List<Result>

    @Query("DELETE FROM quote")
    suspend fun clearAllQuotes()


}