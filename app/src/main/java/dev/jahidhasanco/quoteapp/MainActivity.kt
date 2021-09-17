package dev.jahidhasanco.quoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.jahidhasanco.quoteapp.app.QuoteApplication
import dev.jahidhasanco.quoteapp.data.repository.Response
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModel
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModelFactory
import dev.jahidhasanco.ui.Adapter.QuoteAdapter

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    lateinit var recyclerView_MainActivity: RecyclerView
    lateinit var layoutManagerV: LinearLayoutManager
    lateinit var quoteAdapter: QuoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView_MainActivity = findViewById(R.id.recyclerView_MainActivity)
        val repository = (application as QuoteApplication).quotesRepository
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)
        quoteAdapter = QuoteAdapter(this)
        observeQuotes()

        layoutManagerV = LinearLayoutManager(this)
        recyclerView_MainActivity.apply {
            adapter = quoteAdapter
            layoutManager = layoutManagerV
            setHasFixedSize(true)
        }
    }

    private fun observeQuotes() {
        mainViewModel.quotes.observe(this, Observer {
            when(it){
                is Response.Loading -> {}
                is Response.Success -> {
                    it.data?.let { quote ->
                        quoteAdapter.addQuotes(quote.results)
//
//                        quoteAdapter.addQuotes(quote.results)

                    }
                }
                is Response.Error -> {

                    Toast.makeText(this,"${it.errorMessage}",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}