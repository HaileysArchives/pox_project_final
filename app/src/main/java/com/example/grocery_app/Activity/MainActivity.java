package com.example.grocery_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.grocery_app.Adapter.PopularListAdapter;
import com.example.grocery_app.Domain.PopularDomain;
import com.example.grocery_app.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterPopular;
    private RecyclerView recyclerViewPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerview();
        bottom_navigation();
    }

    private void bottom_navigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout wishBtn = findViewById(R.id.wishBtn);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        wishBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WishActivity.class)));
    }

    private void initRecyclerview() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("빼빼로 오리지날", "Discover", "pic1", 15, 4, 1360));
        items.add(new PopularDomain("콘트라베이스 콜드브루", "Discover", "pic2", 10, 4.5, 2500));
        items.add(new PopularDomain("레몬", "Discover", "pic3", 13, 4.2, 880));
        items.add(new PopularDomain("먹태깡", "Discover", "pic4", 40, 4.7, 1700));
        items.add(new PopularDomain("포카리스웨트", "Discover", "pic5", 31, 3.9, 1980));

        recyclerViewPopular = findViewById(R.id.view1);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterPopular = new PopularListAdapter(this, items);
        recyclerViewPopular.setAdapter(adapterPopular);
    }
}
