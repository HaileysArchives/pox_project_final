package com.example.grocery_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.grocery_app.Domain.PopularDomain;
import com.example.grocery_app.Helper.ChangeNumberItemsListener;
import com.example.grocery_app.Helper.ManagmentCart;
import com.example.grocery_app.R;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    ArrayList<PopularDomain> listItemSelected;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;


    public CartListAdapter(ArrayList<PopularDomain> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(listItemSelected.get(position).getTitle());
        holder.feeEachItem.setText("$" + listItemSelected.get(position).getPrice());
        holder.totalEachItem.setText("$" + Math.round((listItemSelected.get(position).getPrice() * listItemSelected.get(position).getNumberinCart())));
        holder.num.setText(String.valueOf(listItemSelected.get(position).getNumberinCart()));

        int drawableResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(listItemSelected.get(position).getPicUrl(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(30, 30, 30, 30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(v -> {
            managmentCart.plusNumberItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            });

        });

        holder.minusItem.setOnClickListener(v -> {
            managmentCart.minusNumberItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            });

        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        TextView totalEachItem, num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            plusItem = itemView.findViewById(R.id.pludCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            num = itemView.findViewById(R.id.numberItemTxt);
        }
    }
}
