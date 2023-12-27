package com.example.quotesapp

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

class MainViewModel(val context: Context) : ViewModel() {
    private var quoteList: Array<Quote> = emptyArray()

    // when we use the ViewModel we cannot pass the Views we can only pass the Context or we can
    //  say reference of that particular file
    private var index = 0

    init {
        quoteList = loadQuoteFromAssets()
    }

    private fun loadQuoteFromAssets(): Array<Quote> {
        val inputStream = context.assets.open("quotes.json")
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        val json = String(buffer, Charsets.UTF_8)

        // Creating Gson Object
        val gson = Gson()
       return gson.fromJson(json,Array<Quote>::class.java)
     }

    fun getQuote() = quoteList[index]


    fun nextQuote() = quoteList[++index % quoteList.size]

    fun previousQuote() = quoteList[(--index + quoteList.size) % quoteList.size]

}