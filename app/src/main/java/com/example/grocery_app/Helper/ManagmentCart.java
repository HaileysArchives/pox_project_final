package com.example.grocery_app.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.grocery_app.Domain.PopularDomain;

import java.util.ArrayList;
import java.util.RandomAccess;

public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(PopularDomain item) {
        ArrayList<PopularDomain> listPop = (ArrayList<PopularDomain>) getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listPop.size(); i++) {
            if (listPop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }

        if (existAlready) {
            listPop.get(n).setNumberinCart(item.getNumberinCart());
        } else {
            listPop.add(item);
        }
        tinyDB.putListObject("CartList", listPop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    // 이 메서드를 ArrayList<PopularDomain>을 반환하도록 수정
    public ArrayList<PopularDomain> getListCart() {
        // 실제로 ArrayList<PopularDomain>을 반환하도록 tinyDB 클래스의 메서드를 적절히 호출해야 함
        return tinyDB.getListObject("CartList", PopularDomain.class);
    }

    public void minusNumberItem(ArrayList<PopularDomain> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem.get(position).getNumberinCart() == 1) {
            listItem.remove(position);

        } else {
            listItem.get(position).setNumberinCart(listItem.get(position).getNumberinCart() - 1);
        }
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public void plusNumberItem(ArrayList<PopularDomain> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setNumberinCart(listItem.get(position).getNumberinCart() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public Double getTotalFee() {
        ArrayList<PopularDomain> listItem = (ArrayList<PopularDomain>) getListCart();
        double fee = 0;
        for (int i = 0; i < listItem.size(); i++) {
            fee = fee + (listItem.get(i).getPrice() * listItem.get(i).getNumberinCart());
        }
        return fee;
    }
}
