package com.reflexit.tastier.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Food;
import com.reflexit.tastier.database.entities.Person;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<Food> foods;

    public FoodAdapter(Context context, List<Food> foods) {
        this.mInflater = LayoutInflater.from(context);
        this.foods = foods;
    }

    @Override
    @NonNull
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_food, parent, false);
        return new FoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        holder.tvPrice.setText(String.format("%s$", foods.get(position).getPrice()));
        holder.tvFoodName.setText(foods.get(position).getName());
        holder.simpleDraweeView.setImageURI(foods.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView simpleDraweeView;
        TextView tvFoodName;
        TextView tvPrice;
        MaterialButton btnComment;
        ViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = itemView.findViewById(R.id.sd_food);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnComment = itemView.findViewById(R.id.btn_comment);
            btnComment.setOnClickListener(view -> {
                PersonCommentDialog personCommentDialog = new PersonCommentDialog(view.getContext(), foods.get(getAdapterPosition()));
                personCommentDialog.show();
            });
        }
    }
}
