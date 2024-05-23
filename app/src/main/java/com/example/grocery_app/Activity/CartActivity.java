package com.example.grocery_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grocery_app.Adapter.CartListAdapter;
import com.example.grocery_app.Helper.ChangeNumberItemsListener;
import com.example.grocery_app.Helper.ManagmentCart;
import com.example.grocery_app.R;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ManagmentCart managmentCart;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
    private Button orderNowButton, connectButton;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managmentCart = new ManagmentCart(this);
        initView();
        setVariable();
        initList();
        calculateCart();
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateCart();
            }
        });
        recyclerView.setAdapter(adapter);
        if (managmentCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
        } else {
            emptyTxt.setVisibility(View.GONE);
        }
    }

    private void calculateCart() {
        double percentTax = 0.02;  // Modify this value as needed for the tax price
        double delivery = 10;  // Assume delivery fee
        double tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        totalFeeTxt.setText("₩" + itemTotal);
        taxTxt.setText("₩" + tax);
        deliveryTxt.setText("₩" + delivery);
        totalTxt.setText("₩" + total);
    }

    private void setVariable() {
        backBtn.setOnClickListener(v -> finish());
        orderNowButton = findViewById(R.id.buttonOrder);
        connectButton = findViewById(R.id.buttonConnect);

        orderNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, ScanQRActivity.class);
            startActivity(intent);
        });

        // Set the listener for the 'Connect' button to navigate to CreateQRActivity
        connectButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CreateQRActivity.class);
            startActivity(intent);
        });
    }

    private void initView() {
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        recyclerView = findViewById(R.id.view3);
        backBtn = findViewById(R.id.backBtn);
        emptyTxt = findViewById(R.id.emptyTxt);
    }
}
