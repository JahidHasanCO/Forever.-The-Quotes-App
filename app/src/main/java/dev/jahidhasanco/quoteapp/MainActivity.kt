package dev.jahidhasanco.quoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    private lateinit var swipe: SwipeRefreshLayout
    lateinit var progress_Bar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView_MainActivity = findViewById(R.id.recyclerView_MainActivity)
        progress_Bar = findViewById(R.id.progress_Bar)
        swipe = findViewById(R.id.swipe)
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

        swipe.setOnRefreshListener{
            observeQuotes()
            swipe.isRefreshing = false
        }
    }

    private fun observeQuotes() {
        mainViewModel.quotes.observe(this, Observer {
            when(it){
                is Response.Loading -> {

                    progress_Bar.visibility = View.VISIBLE
                    recyclerView_MainActivity.visibility = View.INVISIBLE
                }
                is Response.Success -> {

                    it.data?.let { quote ->
                        progress_Bar.visibility = View.INVISIBLE
                        recyclerView_MainActivity.visibility = View.VISIBLE
                        quoteAdapter.addQuotes(quote.results)
                    }
                }
                is Response.Error -> {
                    progress_Bar.visibility = View.INVISIBLE
                    recyclerView_MainActivity.visibility = View.INVISIBLE
                    Toast.makeText(this,"${it.errorMessage}",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}