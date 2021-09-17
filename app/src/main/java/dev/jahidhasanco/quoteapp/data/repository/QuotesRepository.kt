package dev.jahidhasanco.quoteapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.jahidhasanco.quoteapp.data.api.QuoteService
import dev.jahidhasanco.quoteapp.data.db.QuotesDatabase
import dev.jahidhasanco.quoteapp.data.models.QuoteList
import dev.jahidhasanco.quoteapp.utils.NetworkUtils
import java.lang.Exception

class QuotesRepository(
    private val quoteService: QuoteService,
    private val quotesDatabase: QuotesDatabase,
    private val applicationContext: Context
) {
    private var databasePushTime = 0
    private val quotesLiveData = MutableLiveData<Response<QuoteList>>()

    val quotes: LiveData<Response<QuoteList>>
        get() = quotesLiveData

    suspend fun getQuotes(page: Int) {

        if (NetworkUtils.isInternetAvailable(applicationContext)) {

            try {
                val result = quoteService.getQuotes(page)
                if (result?.body() != null) {
                    if(databasePushTime >= 5){
                        quotesDatabase.quoteDao().clearAllQuotes()
                        databasePushTime = 0
                    }
                    quotesDatabase.quoteDao().addQuote(result.body()!!.results)
                    databasePushTime++
                    quotesLiveData.postValue(Response.Success(result.body()))
                }
                else{
                    quotesLiveData.postValue(Response.Error("Api Error"))
                }
            }catch (e: Exception){
                quotesLiveData.postValue(Response.Error(e.message.toString()))
            }


        } else {
            val quotes = quotesDatabase.quoteDao().getQuotes()
            val quoteList = QuoteList(1,1,1,quotes,1,1)
            quotesLiveData.postValue(Response.Success(quoteList))

        }

    }

}