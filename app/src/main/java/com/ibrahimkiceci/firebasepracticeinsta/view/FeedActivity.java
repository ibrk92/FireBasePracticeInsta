package com.ibrahimkiceci.firebasepracticeinsta.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ibrahimkiceci.firebasepracticeinsta.R;
import com.ibrahimkiceci.firebasepracticeinsta.adapter.PostAdapter;
import com.ibrahimkiceci.firebasepracticeinsta.databinding.ActivityFeedBinding;
import com.ibrahimkiceci.firebasepracticeinsta.databinding.ActivityMainBinding;
import com.ibrahimkiceci.firebasepracticeinsta.model.Post;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post> postArrayList;
    private ActivityFeedBinding binding;
    PostAdapter postAdapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        postArrayList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        getData();
        // it shows one under the other in recyclerview
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList);
        binding.recyclerView.setAdapter(postAdapter);


    }

    private void getData(){

        firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if (value != null){
                    // if value is not empty;

                    //value.getDocuments() // this type is list so i need to reach with for loop;

                    for (DocumentSnapshot snapshot: value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData(); // map or hashmap are almost same;

                        String userEmail = (String) data.get("useremail"); // you need to write (String), it is called casting
                        String comment = (String) data.get("comment");
                        String downloadUrl = (String) data.get("downloadUrl");

                            Post post = new Post(userEmail, comment, downloadUrl);

                            postArrayList.add(post);


                    }

                    postAdapter.notifyDataSetChanged(); // when new data is entered, it is called for postAdapter


                }

            }
        });




    }

    //Adding option menu;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_post){
            //Intent to Upload Activity
            Intent intent = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.sign_out){
            // Signout
            // you must let to know firebase about user is sign out
            firebaseAuth.signOut();
            Intent intenttoMain =  new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intenttoMain);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}