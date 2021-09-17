package dev.jahidhasanco.quoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.jahidhasanco.quoteapp.app.QuoteApplication
import dev.jahidhasanco.quoteapp.data.repository.Response
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModel
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = (application as QuoteApplication).quotesRepository

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        observeQuotes()



    }

    private fun observeQuotes() {
        mainViewModel.quotes.observe(this, Observer {
            when(it){
                is Response.Loading -> {}
                is Response.Success -> {
                    it.data?.let {

                    }
                }
                is Response.Error -> {

                    Toast.makeText(this,"${it.errorMessage}",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}