package com.reflexit.tastier.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Comment;
import com.reflexit.tastier.database.entities.CommentFoods;
import com.reflexit.tastier.database.entities.Food;

import java.util.List;

public class PersonFoodCommentAdapter   extends RecyclerView.Adapter<PersonFoodCommentAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<CommentFoods> foods;

    public PersonFoodCommentAdapter(Context context, List<CommentFoods> foods) {
        this.foods = foods;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public PersonFoodCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_comment, parent, false);
        return new PersonFoodCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonFoodCommentAdapter.ViewHolder holder, int position) {
        Food food = foods.get(position).getFood().get(0);
        Comment comment = foods.get(position).getComment();
        holder.tvComment.setText(food.getName() + ": " + comment.getComment());
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvComment;
        ViewHolder(View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }


}
