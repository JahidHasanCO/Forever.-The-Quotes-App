package dev.jahidhasanco.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.jahidhasanco.quoteapp.R
import dev.jahidhasanco.quoteapp.data.models.Result

class QuoteAdapter(private val context: Context) :
    RecyclerView.Adapter<QuoteAdapter.MyViewHolder>(){


    private var quotes: List<Result> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.single_quote, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val quote = quotes[position]
        holder.quoteAuthor_singleQuote.text = quote.author
        holder.quoteMain_singleQuote.text = quote.content

    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quoteMain_singleQuote: TextView = itemView.findViewById(R.id.quoteMain_singleQuote)
        val quoteAuthor_singleQuote: TextView = itemView.findViewById(R.id.quoteAuthor_singleQuote)
    }

//    fun clear(){
//        quotes.ra
//        notifyDataSetChanged()
//    }

    fun addQuotes(quotes: List<Result>){
        this.quotes = quotes
        notifyDataSetChanged()
    }

}