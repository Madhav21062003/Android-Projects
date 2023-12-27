package com.example.imagegenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText inputText ;
    MaterialButton generateBtn ;
    ProgressBar progressBar ;
    ImageView imageView ;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        generateBtn = findViewById(R.id.generateButton);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.image_view);

        generateBtn.setOnClickListener((v)->{
            String text = inputText.getText().toString().trim();
            if(text.isEmpty()){
                inputText.setError("Text can't be empty");
                return;
            }
            callAPI(text);
        });

    }

    void callAPI(String text) {
        //API CALL
        setInProgress(true);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("prompt", text);
            jsonBody.put("size", "256x256");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .header("Authorization", "Bearer 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")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast toast = Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//                });
                Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String imageUrl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                    loadImage(imageUrl);
                    setInProgress(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void setInProgress(boolean inProgress){
        runOnUiThread(()->{
            if(inProgress){
                progressBar.setVisibility(View.VISIBLE);
                generateBtn.setVisibility(View.GONE);
            }else{
                progressBar.setVisibility(View.GONE);
                generateBtn.setVisibility(View.VISIBLE);
            }
        });

    }

    void loadImage(String url){
        //load image

        runOnUiThread(()->{
            Picasso.get().load(url).into(imageView);
        });

    }
}
