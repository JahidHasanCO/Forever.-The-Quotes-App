package dev.jahidhasanco.quoteapp.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jahidhasanco.quoteapp.data.models.QuoteList
import dev.jahidhasanco.quoteapp.data.repository.QuotesRepository
import dev.jahidhasanco.quoteapp.data.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: QuotesRepository) : ViewModel(){

    fun getQuote(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes(page)
        }
    }
        val quotes: LiveData<Response<QuoteList>>
        get() = repository.quotes

}