package dev.jahidhasanco.quoteapp.app

import android.app.Application
import dev.jahidhasanco.quoteapp.data.api.QuoteService
import dev.jahidhasanco.quoteapp.data.api.RetrofitHelper
import dev.jahidhasanco.quoteapp.data.db.QuotesDatabase
import dev.jahidhasanco.quoteapp.data.repository.QuotesRepository

class QuoteApplication: Application() {

    lateinit var quotesRepository: QuotesRepository

    override fun onCreate() {
        super.onCreate()

        initialize()
    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val database = QuotesDatabase.getDatabase(applicationContext)
        quotesRepository = QuotesRepository(quoteService,database,applicationContext)
    }
}