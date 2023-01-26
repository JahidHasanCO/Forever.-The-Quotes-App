package dev.jahidhasanco.ui.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.jahidhasanco.databinding.ActivityQuoteBinding

class QuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { onBackPressed() }

        val quote = intent.getStringExtra("quote")
        val author = intent.getStringExtra("author")

        binding.todaysQuoteMain.text = quote
        binding.todaysQuoteAuthor.text = author

        binding.btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("text", "$quote  -  $author")
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Quote copied!", Toast.LENGTH_SHORT).show()
        }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "$quote  -  $author")
            startActivity(Intent.createChooser(shareIntent, "Share quote"))
        }
    }

}