package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToFavourits(View v){
        Intent i = new Intent(this, FavorisActivity.class);
        startActivity(i);
    }
    //    public void goToScan(View v){
//        Intent i = new Intent(this, scan.class);
//        startActivity(i);
//    }
public void goToSearch(View v){
   Intent i = new Intent(this, search.class);
   startActivity(i);
}
public void goToProfil(View v){
   Intent i = new Intent(this, ProfilActivity.class);
   startActivity(i);
}
    public void goToNotifications(View v){
        Intent i = new Intent(this, notification.class);
        startActivity(i);
    }
    public void goToProduct(View v){
        Intent i = new Intent(this, product.class);
        startActivity(i);
    }

}