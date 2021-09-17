package dev.jahidhasanco.quoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.jahidhasanco.quoteapp.data.api.QuoteService
import dev.jahidhasanco.quoteapp.data.api.RetrofitHelper
import dev.jahidhasanco.quoteapp.data.repository.QuotesRepository
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModel
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)

        val repository = QuotesRepository(quoteService)

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.quotes.observe(this, Observer {
            Log.d("QuotesJ",it.toString())
        })

    }
}