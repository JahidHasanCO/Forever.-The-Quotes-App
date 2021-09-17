package dev.jahidhasanco.quoteapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.jahidhasanco.quoteapp.app.QuoteApplication
import dev.jahidhasanco.quoteapp.data.repository.Response
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModel
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModelFactory
import dev.jahidhasanco.quoteapp.utils.NetworkUtils
import dev.jahidhasanco.ui.Adapter.QuoteAdapter

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    private var maxPage = 2
    private var page = 1


    lateinit var recyclerView_MainActivity: RecyclerView
    lateinit var layoutManagerV: LinearLayoutManager
    lateinit var quoteAdapter: QuoteAdapter
    private lateinit var swipe: SwipeRefreshLayout
    lateinit var progress_Bar: ProgressBar
    lateinit var todaysQuoteMain: TextView
    lateinit var todaysQuoteAuthor: TextView

    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView_MainActivity = findViewById(R.id.recyclerView_MainActivity)
        progress_Bar = findViewById(R.id.progress_Bar)
        todaysQuoteMain = findViewById(R.id.todaysQuoteMain)
        todaysQuoteAuthor = findViewById(R.id.todaysQuoteAuthor)
        swipe = findViewById(R.id.swipe)

        swipe.visibility = View.INVISIBLE
        progress_Bar.visibility = View.VISIBLE

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        sharedEditor = sharedPreferences.edit()

        val repository = (application as QuoteApplication).quotesRepository
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)
        mainViewModel.getQuote(page)
        quoteAdapter = QuoteAdapter(this)


        layoutManagerV = LinearLayoutManager(this)
        recyclerView_MainActivity.apply {
            adapter = quoteAdapter
            layoutManager = layoutManagerV
            setHasFixedSize(true)
        }

        if (isItFirstTime()) {
            if(NetworkUtils.isInternetAvailable(this)){
                observeQuotes()
            }else{
                sharedEditor.putBoolean("firstTime", true)
                sharedEditor.commit()
                sharedEditor.apply()
                Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show()
            }

        } else {
            observeQuotes()
        }



        swipe.setOnRefreshListener{
            val pageRan = (page until maxPage).random()
            if(NetworkUtils.isInternetAvailable(this)){
                mainViewModel.getQuote(pageRan)
                observeQuotes()
            }else{
                Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show()
            }

            swipe.isRefreshing = false
        }
    }

    fun isItFirstTime(): Boolean {
        return if (sharedPreferences!!.getBoolean("firstTime", true)) {
            sharedEditor.putBoolean("firstTime", false)
            sharedEditor.commit()
            sharedEditor.apply()
            true
        } else {
            false
        }
    }


    private fun observeQuotes() {
        mainViewModel.quotes.observe(this, Observer {
            when(it){
                is Response.Loading -> {
                    swipe.visibility = View.INVISIBLE
                    progress_Bar.visibility = View.VISIBLE
                }
                is Response.Success -> {

                    it.data?.let { quote ->
                        quoteAdapter.addQuotes(quote.results)
                        todaysQuoteMain.text = quote.results[0].content
                        todaysQuoteAuthor.text = quote.results[0].author
                        if(quote.totalPages >= maxPage){
                            maxPage = quote.totalPages
                        }
                        swipe.visibility = View.VISIBLE
                        progress_Bar.visibility = View.INVISIBLE
                    }

                }
                is Response.Error -> {
                    swipe.visibility = View.INVISIBLE
                    progress_Bar.visibility = View.VISIBLE
                    Toast.makeText(this,"${it.errorMessage}",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}