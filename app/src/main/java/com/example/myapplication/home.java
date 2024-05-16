package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class home extends AppCompatActivity {

    ImageView imageview;
    TextView name;

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


        imageview=findViewById(R.id.imageView12);
        name=findViewById(R.id.textView12);


        showUserData();
    }

    private void showUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        if (currentUser != null) {
            databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        name.setText(userName);

                        if (dataSnapshot.hasChild("image")) {
                            String image = dataSnapshot.child("image").getValue().toString();
                            Picasso.get().load(image).transform(new CircleTransform()).into(imageview);
                        }
                    }
                    else {
                        // Handle the case where user data is not found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled
                }
            });
        } else {
            // Handle the case where user is not logged in
        }


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