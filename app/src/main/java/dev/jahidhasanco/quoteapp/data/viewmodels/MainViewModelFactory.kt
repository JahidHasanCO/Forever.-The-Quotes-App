package dev.jahidhasanco.quoteapp.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.jahidhasanco.quoteapp.data.repository.QuotesRepository

class MainViewModelFactory(private val repository: QuotesRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}