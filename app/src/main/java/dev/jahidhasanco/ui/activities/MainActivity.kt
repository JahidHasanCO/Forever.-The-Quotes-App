package dev.jahidhasanco.ui.activities

import android.content.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.jahidhasanco.databinding.ActivityMainBinding
import dev.jahidhasanco.quoteapp.app.QuoteApplication
import dev.jahidhasanco.quoteapp.data.repository.Response
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModel
import dev.jahidhasanco.quoteapp.data.viewmodels.MainViewModelFactory
import dev.jahidhasanco.quoteapp.utils.NetworkUtils
import dev.jahidhasanco.ui.adapter.QuoteAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private var maxPage = 2
    private var page = 1

    private val quoteAdapter by lazy { QuoteAdapter(this) }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedEditor: SharedPreferences.Editor
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swipe.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        sharedEditor = sharedPreferences.edit()

        val repository = (application as QuoteApplication).quotesRepository
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)
        mainViewModel.getQuote(page)

        binding.recyclerViewMainActivity.apply {
            adapter = quoteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        if (isItFirstTime()) {
            if (NetworkUtils.isInternetAvailable(this)) {
                observeQuotes()
            } else {
                sharedEditor.putBoolean("firstTime", true)
                sharedEditor.commit()
                sharedEditor.apply()
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        } else {
            observeQuotes()
        }

        binding.swipe.setOnRefreshListener {
            val pageRan = (page until maxPage).random()
            if (NetworkUtils.isInternetAvailable(this)) {
                mainViewModel.getQuote(pageRan)
                observeQuotes()
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
            binding.swipe.isRefreshing = false
        }

    }

    private fun isItFirstTime(): Boolean {
        return if (sharedPreferences.getBoolean("firstTime", true)) {
            sharedEditor.putBoolean("firstTime", false)
            sharedEditor.commit()
            sharedEditor.apply()
            true
        } else {
            false
        }
    }

    private fun observeQuotes() {
        mainViewModel.quotes.observe(this) {
            when (it) {
                is Response.Loading -> {
                    binding.swipe.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Response.Success -> {
                    it.data?.let { quote ->
                        quoteAdapter.addQuotes(quote.results)
                        binding.todaysQuoteMain.text = quote.results[0].content
                        binding.todaysQuoteAuthor.text = quote.results[0].author
                        if (quote.totalPages >= maxPage) {
                            maxPage = quote.totalPages
                        }
                        binding.swipe.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.INVISIBLE

                        binding.btnCopy.setOnClickListener {
                            val clipboard =
                                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(
                                "text",
                                "${quote.results[0].content}  -  ${quote.results[0].author}"
                            )
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(this, "Quote copied!", Toast.LENGTH_SHORT).show()
                        }

                        binding.btnShare.setOnClickListener {
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.type = "text/plain"
                            shareIntent.putExtra(
                                Intent.EXTRA_TEXT,
                                "${quote.results[0].content}  -  ${quote.results[0].author}"
                            )
                            startActivity(Intent.createChooser(shareIntent, "Share quote"))
                        }
                    }
                }
                is Response.Error -> {
                    binding.swipe.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(this, "${it.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}