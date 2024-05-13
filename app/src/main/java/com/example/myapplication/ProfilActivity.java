package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.DialogInterface;

import com.google.firebase.FirebaseApp;
import com.squareup.picasso.Picasso;

import android.text.InputType;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Objects;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class ProfilActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    ImageView imageview;
    TextView textViewUsername;
    TextView textViewUsername2;
    TextView textViewEmail;
    TextView textViewPassword;
    FloatingActionButton SaveImageButton;
    private Uri imageUri;
    private String myUri="";
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profil);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.green)));
        }

        FirebaseApp.initializeApp(this);

        textViewUsername=findViewById(R.id.textView7);
        textViewUsername2=findViewById(R.id.textView2);
        textViewEmail=findViewById(R.id.textView5);
        textViewPassword=findViewById(R.id.textView4);

        showUserData();


        ImageButton UpdateUsernameButton=findViewById(R.id.floatingActionButton6);
        ImageButton UpdateUseremailButton=findViewById(R.id.floatingActionButton3);
        ImageButton UpdateUserpasswordButton=findViewById(R.id.floatingActionButton2);


        UpdateUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1=LayoutInflater.from(ProfilActivity.this).inflate(R.layout.dialog_layout,null);
                TextInputEditText editText=view1.findViewById(R.id.editText);
                AlertDialog alertDialog=new MaterialAlertDialogBuilder(ProfilActivity.this)
                        .setTitle("Username:")
                        .setView(view1)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String UpdatedUsername=Objects.requireNonNull(editText.getText()).toString().trim();
                                if (!UpdatedUsername.isEmpty()) {
                                    updateUsername(UpdatedUsername);
                                } else {
                                    Toast.makeText(ProfilActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }
        });
        UpdateUseremailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1=LayoutInflater.from(ProfilActivity.this).inflate(R.layout.email_modification_layout,null);
                TextInputEditText editText=view1.findViewById(R.id.editText);
                AlertDialog alertDialog=new MaterialAlertDialogBuilder(ProfilActivity.this)
                        .setTitle("Email:")
                        .setView(view1)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String UpdatedEmail=Objects.requireNonNull(editText.getText()).toString().trim();
                                if (!UpdatedEmail.isEmpty()) {
                                    updateEmail(UpdatedEmail);
                                } else {
                                    Toast.makeText(ProfilActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                                }

                                //here we should modify the email of the user in the firebase
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }
        });
        UpdateUserpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1=LayoutInflater.from(ProfilActivity.this).inflate(R.layout.password_modification_layout,null);
                TextInputEditText editTextPassword=view1.findViewById(R.id.editTextPassword);
                TextInputEditText editTextNewPassword=view1.findViewById(R.id.editTextNewPassword);
                TextInputEditText editTextConfirmPassword=view1.findViewById(R.id.editTextConfirmPassword);
                AlertDialog alertDialog=new MaterialAlertDialogBuilder(ProfilActivity.this)
                        .setTitle("Change password:")
                        .setView(view1)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String currentPassword = Objects.requireNonNull(editTextPassword.getText()).toString().trim();
                                String newPassword = Objects.requireNonNull(editTextNewPassword.getText()).toString().trim();
                                String confirmPassword = Objects.requireNonNull(editTextConfirmPassword.getText()).toString().trim();
                                if(!currentPassword.isEmpty()  && !newPassword.isEmpty() && newPassword.equals(confirmPassword)){
                                    checkPassword(currentPassword,newPassword);
                                }
                                else{
                                    Toast.makeText(ProfilActivity.this, "INCORRECT PASSWORD", Toast.LENGTH_SHORT).show();
                                }

                                dialog.dismiss();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }
        });




        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images");

        imageview = findViewById(R.id.imageView4);


        SaveImageButton =findViewById(R.id.floatingActionButton);

        SaveImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfilActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        getUserinfo();

    }

    private void getUserinfo() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            databaseReference.child(currentUser.getEmail()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                        if (snapshot.hasChild("image")) {
                            String image = snapshot.child("image").getValue().toString();
                            Picasso.get().load(image).into(imageview);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        } else {
            // Handle the case where the user is not signed in
            // For example, redirect to the sign-in screen or show a message
        }
    }


    private void uploadProfileImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait while we are setting your data");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageReference.child(auth.getCurrentUser().getEmail() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Return the download URL task
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    progressDialog.dismiss(); // Dismiss progress dialog in any case

                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        String imageUrl = downloadUrl.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", imageUrl);

                        // Update user's image in database
                        databaseReference.child(auth.getCurrentUser().getEmail()).updateChildren(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> updateTask) {
                                        if (updateTask.isSuccessful()) {
                                            // Image upload and database update successful
                                            Toast.makeText(ProfilActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Handle database update failure
                                            Toast.makeText(ProfilActivity.this, "Error updating profile image", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        // Handle download URL retrieval failure
                        Toast.makeText(ProfilActivity.this, "Error getting download URL", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri=data.getData();
        imageview.setImageURI(imageUri);
        uploadProfileImage();
    }
    private void updateUsername(String newUsername){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.child("name").setValue(newUsername)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showUserData();
                        Toast.makeText(ProfilActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void updateEmail(String newEmail) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.updateEmail(newEmail)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showUserData();
                            Toast.makeText(ProfilActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfilActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void updatePassword(String currentPassword, String newPassword) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);
            currentUser.reauthenticate(credential)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            currentUser.updatePassword(newPassword)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            showUserData();
                                            Toast.makeText(ProfilActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProfilActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfilActivity.this, "Authentication failed. Incorrect current password.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void checkPassword(String currentPassword, final String newPassword) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String storedPassword = dataSnapshot.child("password").getValue(String.class);
                        if (storedPassword != null && storedPassword.equals(currentPassword)) {
                            updatePassword(currentPassword, newPassword);
                        } else {
                            Toast.makeText(ProfilActivity.this, "Incorrect current password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfilActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfilActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ProfilActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public void showUserData(){
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String email = currentUser.getEmail();
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(email);
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String userName = dataSnapshot.child("username").getValue(String.class);
                            String userEmail = dataSnapshot.child("email").getValue(String.class);
                            // Mettez à jour les champs TextView avec les données récupérées
                            textViewUsername.setText(userName);
                            textViewUsername2.setText(userName);
                            textViewEmail.setText(userEmail);
                            // Vous pouvez également afficher d'autres données de l'utilisateur si nécessaire
                        } else {
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

    public void goToHome(View v){
        Intent i = new Intent(this, home.class);
        startActivity(i);
    }




}