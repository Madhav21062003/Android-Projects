package com.example.memapur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.memapur.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.text.CollationElementIterator;

public class MainActivity extends AppCompatActivity {
        ActivityMainBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getMemes();
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMemes();
            }


        });

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Attempt 1
                // shareMeme();


                // Attempt 3
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                String shareBody = "uri";
//                String shareSub = "Your Subject Here";
//                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
//                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
//                startActivity(Intent.createChooser(intent,"Share Via"));


                // Attempt 3
                BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.memeImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Title",null);
                Uri uri = Uri.parse(bitmapPath);
                Intent intent  = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(intent,"Share Via: "));


            }


        });
    }

    private void getMemes() {
        String url = "https://meme-api.com/gimme";


            binding.loadingPb.setVisibility(View.VISIBLE);
        binding.memeImage.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String imgUrl = response.getString("url");
                            Glide.with(getApplicationContext()).load(imgUrl).into(binding.memeImage);
                            binding.loadingPb.setVisibility(View.GONE);
                            binding.memeImage.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);

    }

    private void shareMeme() {
        Bitmap image = getBitmapFromView(binding.memeImage);
        shareImageAndText(image);
    }

    private void shareImageAndText(Bitmap image) {
        Uri uri = getImageToShare(image);
        Intent intent = new Intent(Intent.ACTION_SEND);

        // putting the uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM,uri);

        // setting type of image
        intent.setType("image/png");

        // Calling start activity to share
        startActivity(Intent.createChooser(intent,"Share Image Via:"));
    }

    private Uri getImageToShare(Bitmap image) {
        File imageFolder = new File(getCacheDir(), "images");
        Uri uri = null ;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "meme.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();
             uri = FileProvider.getUriForFile(this,"com.example.memapur",file);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    private Bitmap getBitmapFromView(ImageView memeImage) {

        ImageFilterButton view = null;

        Bitmap returnBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnBitmap);

        Drawable background = view.getBackground();
        if (background != null){
            background.draw(canvas);
        }else {
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return returnBitmap;
    }

}