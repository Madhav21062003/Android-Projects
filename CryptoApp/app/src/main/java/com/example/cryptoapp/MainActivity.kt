package com.example.cryptoapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextSwitcher
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Header
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cryptoapp.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    private lateinit var data:ArrayList<Modal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        data = ArrayList<Modal>()
        apiData
        rvAdapter = RvAdapter(this,data)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = rvAdapter

        binding.search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val filterData = ArrayList<Modal>()
                for (item in data){
                    if (item.name.lowercase(Locale.getDefault()).contains(p0.toString().lowercase(Locale.getDefault())))
                    {
                        filterData.add(item)
                    }

                }
                if (filterData.isEmpty()){
                    Toast.makeText(this@MainActivity,"No data Available",Toast.LENGTH_LONG).show()
                }else{
                    rvAdapter.changeddata(filterData)
                }
            }

        })
    }

    val apiData:Unit
    get() {
        val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest:JsonObjectRequest =
            @SuppressLint("NotifyDataSetChanged")
            object:JsonObjectRequest(Method.GET,url,null,Response.Listener {
                response ->
                binding.progressBar.isVisible = false
                try {
                    val dataArray = response.getJSONArray("data")
                    for (i in 0 until dataArray.length())
                    {
                        val dataObject = dataArray.getJSONObject(i)
                        val symbol = dataObject.getString("symbol")
                        val name = dataObject.getString("name")
                        val quote = dataObject.getJSONObject("quote")
                        val USD = quote.getJSONObject("USD")
                        val price = String.format("$"+"%.2f",USD.getDouble("price"))
                        data.add(Modal(name,symbol, price ))

                    }
                    rvAdapter.notifyDataSetChanged()
                }catch (e: Exception){
                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()

                }
            }, Response.ErrorListener{
                Toast.makeText(this,"Error 1",Toast.LENGTH_LONG).show()
            })
            {
                override fun getHeaders(): Map<String, String> {

                    val headers = HashMap<String,String>();
                    headers["X-CMC_PRO_API_KEY"] = "b7e90145-3aa8-473c-87de-50cdf56e875d"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
    }

}



