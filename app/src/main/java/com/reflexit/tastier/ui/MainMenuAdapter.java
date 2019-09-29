package com.reflexit.tastier.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reflexit.tastier.R;
import com.reflexit.tastier.model.MainMenuSelector;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<MainMenuSelector> mainMenuSelectors;

    public MainMenuAdapter(Context context, ArrayList<MainMenuSelector> mainMenuSelectors) {
        this.mInflater = LayoutInflater.from(context);
        this.mainMenuSelectors = mainMenuSelectors;
    }

    @Override
    @NonNull
    public MainMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_main_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuAdapter.ViewHolder holder, int position) {
        holder.ivSection.setImageResource(mainMenuSelectors.get(position).getImageResource());
        holder.tvSection.setText(mainMenuSelectors.get(position).getSectionName());
        holder.itemView.setOnClickListener(mainMenuSelectors
                .get(position)
                .getOnClickListener());
    }

    @Override
    public int getItemCount() {
        return mainMenuSelectors.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSection;
        ImageView ivSection;

        ViewHolder(View itemView) {
            super(itemView);
            tvSection = itemView.findViewById(R.id.tv_section);
            ivSection = itemView.findViewById(R.id.iv_section);
        }

    }
}
