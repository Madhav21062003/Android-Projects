package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newsapp.Models.NewsApiResponse;
import com.example.newsapp.Models.NewsHeadlines;

import java.util.List;


public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener{

        RecyclerView recyclerView ;
        CustomAdaptor adaptor ;
        ProgressDialog dialog ;

        Button b1,b2,b3,b4,b5,b6, b7 ;
        SearchView searchView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching News Articles.............");
        dialog.show();

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news Articles......."+query);
                dialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener, "general", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        b1 = findViewById(R.id.btn_1);
        b1.setOnClickListener(this);

        b2 = findViewById(R.id.btn_2);
        b2.setOnClickListener(this);

        b3 = findViewById(R.id.btn_3);
        b3.setOnClickListener(this);

        b4 = findViewById(R.id.btn_4);
        b4.setOnClickListener(this);

        b5 = findViewById(R.id.btn_5);
        b5.setOnClickListener(this);

        b6 = findViewById(R.id.btn_6);
        b6.setOnClickListener(this);

        b7 = findViewById(R.id.btn_7);
        b7.setOnClickListener(this);

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, "general", null);
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if (list.isEmpty()){
                Toast.makeText(MainActivity.this, "No Data Found!!!!!!!", Toast.LENGTH_SHORT).show();
            }else {
                showNews(list);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, "An Error Occurred!!!!!!!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adaptor = new CustomAdaptor(this, list,this);
        recyclerView.setAdapter(adaptor);
    }

    @Override
    public void onNewsClicked(NewsHeadlines headlines) {
        startActivity(new Intent(MainActivity.this, DetailsActivity.class)
                .putExtra("data",headlines));
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v ;
        String category = button.getText().toString();

        dialog.setTitle("Fetching news articles of "+category);
        dialog.show();
        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, category, null);
    }
}

//  From where we fetch the news articles in this application we use Rest API to fetch News Articles
// In this application we use newsapi.org to get free api key
// In this application we stores our api key in "strings.xml" file stored as  name as  (<string name="api_key">bd830df1a4b94d1487886bf754c704cf</string>)

/* Step-1 - Go to newsApi.org
    step-2 Go to documentation section
    step-3 copy the url that is given in the get section
    step-4 open postman in your browser and signin or signup
    step-5 paste the link that u get from the newsapi website and the click on send button
    step-6  After click send button we saw some text in body section like

            "status": "ok",
    "totalResults": 35,
    "articles": [                (here arcticles is a JSON Array having multiple objects like source , author, title etc)
        {
            "source": {
                "id": "cnn",
                "name": "CNN"
            },...................................}

     step - 7 create a pacake named as models next part explained in Source.java class


 */
