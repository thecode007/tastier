package com.reflexit.tastier.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Person;
import com.reflexit.tastier.database.entities.PersonFaces;
import com.reflexit.tastier.utils.FileUtils;

import java.util.List;

public class CustomersAdapter  extends RecyclerView.Adapter<CustomersAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<PersonFaces> personArrayList;

    public CustomersAdapter(Context context, List<PersonFaces>  personArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.personArrayList = personArrayList;
    }

    @Override
    @NonNull
    public CustomersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_customer, parent, false);
        return new CustomersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersAdapter.ViewHolder holder, int position) {
        Person person = personArrayList.get(position).getPerson();
        holder.tvName.setText(person.getName());
        holder.tvID.setText(person.getPersonId());
        if (personArrayList.get(position).getFaces().size() > 0) {
            int lastOfFaces = personArrayList.get(position).getFaces().size() -1;
            String faceUrl = FileUtils.personDirectory + "/" + person.getPersonId() + "/" + personArrayList.get(position).
                    getFaces().get(lastOfFaces).getFaceID() + ".jpg";
            Bitmap myBitmap = BitmapFactory.decodeFile(faceUrl);
            holder.simpleDraweeView.setImageBitmap(myBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return personArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvID;
        ImageView simpleDraweeView;
        ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            simpleDraweeView = itemView.findViewById(R.id.sd_image);
            itemView.setOnClickListener(view -> {
                view.getContext().startActivity( ProfileActivity.newIntent(view.getContext(), personArrayList.get(getAdapterPosition())));
            });
        }

    }
}
