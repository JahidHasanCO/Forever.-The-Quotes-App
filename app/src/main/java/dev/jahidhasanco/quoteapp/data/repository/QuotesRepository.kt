package dev.jahidhasanco.quoteapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.jahidhasanco.quoteapp.data.api.QuoteService
import dev.jahidhasanco.quoteapp.data.models.QuoteList

class QuotesRepository(private val quoteService: QuoteService) {

    private val quotesLiveData = MutableLiveData<QuoteList>()

    val quotes: LiveData<QuoteList>
    get() = quotesLiveData

    suspend fun getQuotes(page:Int){
        val result = quoteService.getQuotes(page)
        if(result?.body() != null){
            quotesLiveData.postValue(result.body())
        }
    }

}