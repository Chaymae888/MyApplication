package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FavorisActivity extends AppCompatActivity implements OnHeartClickListener {
    private RecyclerView recyclerView;
    private SearchView searchView;
    List<DataClass> dataList;
    MyAdapter adapter;
    DataClass androidData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoris);


        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false); // Expand the SearchView when clicked
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager=new GridLayoutManager(FavorisActivity.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList=new ArrayList<>();

        androidData=new DataClass("Product1","this Product is Hallal...",R.drawable.product_icon,true);
        dataList.add(androidData);
        androidData=new DataClass("Product2","this Product is Hallal...",R.drawable.product_icon,true);
        dataList.add(androidData);
        androidData=new DataClass("Product3","this Product is Haram...",R.drawable.product_icon,true);
        dataList.add(androidData);
        androidData=new DataClass("Product4","this Product is Hallal...",R.drawable.product_icon,true);
        dataList.add(androidData);
        androidData=new DataClass("Product5","this Product is Haram...",R.drawable.product_icon,true);
        dataList.add(androidData);
        androidData=new DataClass("Product6","this Product is Hallal...",R.drawable.product_icon,true);
        dataList.add(androidData);

        adapter=new MyAdapter(FavorisActivity.this,dataList);
        adapter.setOnHeartClickListener(FavorisActivity.this);
        recyclerView.setAdapter(adapter);



    }


    public void onHeartClick(int position) {
        if (position >= 0 && position < dataList.size()) {
            DataClass item = dataList.get(position);
            item.setFavorite(!item.getIsFavorite());
            if (!item.getIsFavorite()) {
                dataList.remove(position);
                Log.e("FavorisActivity", "position: " + position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, dataList.size() - position);// Notify adapter of removal
                if(dataList.isEmpty()){

                    ImageView imageView =findViewById(R.id.imageView);;
                    TextView textView=findViewById(R.id.textView8);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);

                }
            }
        }


    }
    private void searchList(String text){
        List<DataClass> dataSearchList=new ArrayList<>();
        for(DataClass data:dataList){
            if(data.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }
        if(dataSearchList.isEmpty()){
            Toast.makeText(this, "NOT FOUND", Toast.LENGTH_SHORT).show();
        }
        else adapter.setSearchList(dataSearchList);
    }




}