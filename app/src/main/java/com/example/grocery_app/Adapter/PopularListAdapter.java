// RecyclerView의 각 아이템 뷰를 관리하기 위한 어댑터
package com.example.grocery_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.grocery_app.Activity.DetailActivity;
import com.example.grocery_app.Domain.PopularDomain;
import com.example.grocery_app.R;

import java.util.ArrayList;

public class PopularListAdapter extends RecyclerView.Adapter<PopularListAdapter.Viewholder> {
    ArrayList<PopularDomain> items;  // 목록을 보여줄 데이터 리스트
    Context context;  // 현재 어댑터의 컨텍스트를 저장

    public PopularListAdapter(Context context, ArrayList<PopularDomain> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PopularListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 뷰홀더 객체를 생성하고 초기화
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop_list, parent, false);
        context = parent.getContext();
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularListAdapter.Viewholder holder, int position) {
        // 뷰홀더에 데이터 바인딩
        holder.titleTxt.setText(items.get(position).getTitle());  // 제목 설정
        holder.feeTxt.setText("₩" + items.get(position).getPrice());  // 가격 설정
        holder.ScoreTxt.setText("" + items.get(position).getScore());  // 점수 설정

        // 이미지 리소스 ID 가져오기 및 이미지 설정
        int drawableResourceId = holder.itemView.getResources().getIdentifier(items.get(position).getPicUrl(),
                "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())  // Glide를 사용해 이미지 로드 및 표시
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(holder.pic);

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("object", items.get(position));  // 인텐트에 데이터 객체 전달
            holder.itemView.getContext().startActivity(intent);  // 상세 화면으로 이동
        });
    }

    @Override
    public int getItemCount() {  // 아이템 갯수 반환
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {  // 뷰홀더 내부 클래스
        TextView titleTxt, feeTxt, ScoreTxt;
        ImageView pic;  // 텍스트뷰와 이미지뷰 참조 변수

        public Viewholder(@NonNull View itemView) {  // 뷰홀더 생성자
            super(itemView);

            // 뷰 컴포넌트 ID로 뷰 연결
            titleTxt = itemView.findViewById(R.id.titleTxt);
            feeTxt = itemView.findViewById(R.id.feeTxt);
            ScoreTxt = itemView.findViewById(R.id.scoreTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
