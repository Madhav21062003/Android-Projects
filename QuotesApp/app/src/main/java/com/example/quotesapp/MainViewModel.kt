package com.example.quotesapp

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

class MainViewModel(val context: Context): ViewModel(){
    private var quoteList: Array<Quote> = emptyArray()
    private var index = 0 ;

    // To initialise quote list we initialize it by using init
    init {
        quoteList = loadQuotesFromAssets()
    }

    private fun loadQuotesFromAssets(): Array<Quote> {
        val inputStream = context.assets.open("quotes.json")

        // Taking size of the file
        val size:Int = inputStream.available()

        // store the file
        val buffer = ByteArray(size)

        // after input stream read it the it store it into the buffer
        inputStream.read(buffer)

        // close the input stream
        inputStream.close()

        val json = String(buffer, Charsets.UTF_8)
        val gson = Gson()
        return  gson.fromJson(json, Array<Quote>::class.java)
    }

    fun getQuote() = quoteList[index]

    fun nextQuote(): Quote {
        if (index < quoteList.size - 1) {
            index++
        } else {
            // If index is already at the last element, wrap around to the first element
            index = 0
        }
        return quoteList[index]
    }

    fun previousQuote(): Quote {
        if (index > 0) {
            index--
        } else {
            // If index is already at the first element, wrap around to the last element
            index = quoteList.size - 1
        }
        return quoteList[index]
    }




}

//class MainViewModel(val context: Context) : ViewModel() {
//    private var quoteList: Array<Quote> = emptyArray()
//
//    // when we use the ViewModel we cannot pass the Views we can only pass the Context or we can
//    //  say reference of that particular file
//    private var index = 0
//
//    init {
//        quoteList = loadQuoteFromAssets()
//    }
//
//    private fun loadQuoteFromAssets(): Array<Quote> {
//        val inputStream = context.assets.open("quotes.json")
//        val size: Int = inputStream.available()
//        val buffer = ByteArray(size)
//        inputStream.read(buffer)
//        inputStream.close()
//
//        val json = String(buffer, Charsets.UTF_8)
//
//        // Creating Gson Object
//        val gson = Gson()
//       return gson.fromJson(json,Array<Quote>::class.java)
//     }
//
//    fun getQuote() = quoteList[index]
//
//
//    fun nextQuote() = quoteList[++index % quoteList.size]
//
//    fun previousQuote() = quoteList[(--index + quoteList.size) % quoteList.size]
//
//}