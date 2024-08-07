package com.example.turafoglalas;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class TuraListActivity extends AppCompatActivity {
    private FirebaseUser user;
    private RecyclerView mRecyclerView;
    private ArrayList<TuraItem> mItemList;
    private TuraAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private int gridNumber = 1;
    private boolean viewRow = false;
    private NotificationHandler mNotificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tura_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new TuraAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");


        queryData();

        mNotificationHandler = new NotificationHandler(this);

    }

    private void queryData(){
        mItemList.clear();

        mItems.orderBy("name").limit(20).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                TuraItem item = document.toObject(TuraItem.class);
                item.setId(document.getId());
                mItemList.add(item);
            }

            if(mItemList.size() == 0){
                initializeData();
                queryData();
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    public void deleteItem(TuraItem item){
        DocumentReference ref = mItems.document(item._getId());

        ref.delete().addOnSuccessListener(success -> {
        }).addOnFailureListener(failure -> {
            Toast.makeText(this, "Item deletion failed, id: " + item._getId() + ".", Toast.LENGTH_LONG).show();
        });

        queryData();
    }

    private void initializeData() {
        String[] itemsName = getResources().getStringArray(R.array.tura_Names);
        String[] itemsLength = getResources().getStringArray(R.array.tura_Lengths);
        String[] itemsDate = getResources().getStringArray(R.array.tura_Dates);
        String[] itemsPrice = getResources().getStringArray(R.array.tura_Prices);

        for(int i=0; i<itemsName.length; i++){
            mItems.add(new TuraItem(itemsName[i], itemsLength[i], itemsDate[i], itemsPrice[i]));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.tura_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.logoutButton) {
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        } else if (itemId == R.id.viewSelector) {
            if (viewRow) {
                changeSpanCount(1);
            } else {
                changeSpanCount(2);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void changeSpanCount(int spanCount) {
        viewRow = !viewRow;
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return super.onPrepareOptionsMenu(menu);
    }

    public void sendNotification() {
        mNotificationHandler.send("You've signed up to a hike!");
    }
}