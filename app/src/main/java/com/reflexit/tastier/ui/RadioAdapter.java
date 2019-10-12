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
import com.reflexit.tastier.database.entities.PersonFaces;
import com.reflexit.tastier.utils.FileUtils;

import java.util.List;

public class RadioAdapter  extends RecyclerView.Adapter<RadioAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<PersonFacesSelector> personFacesSelectors;
    private OnPictureSelectedListener onPictureSelectedListener;

    public RadioAdapter(Context context, List<PersonFacesSelector> personFaces,
                        OnPictureSelectedListener onPictureSelectedListener) {
        this.mInflater = LayoutInflater.from(context);
        this.personFacesSelectors = personFaces;
        this.onPictureSelectedListener = onPictureSelectedListener;
    }

    @Override
    @NonNull
    public RadioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.radiobutton, parent, false);
        return new RadioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioAdapter.ViewHolder holder, int position) {

        PersonFaces personFaces = personFacesSelectors.get(position).getPersonFaces();
        if (personFaces.getFaces().size() > 0) {
            String imageDirectory = FileUtils.personDirectory + "/" +
                    personFaces.getPerson().getPersonId() + "/"
                    + personFaces.getFaces().get(0).getFaceID() + ".jpg";
            holder.radioButton.setBackground(Drawable.createFromPath(imageDirectory));

        }

        boolean isSelected = personFacesSelectors.get(position).isSelected();
        holder.tvIsNew.setVisibility(personFaces.getPerson().getPoints() == 1 ? View.VISIBLE:View.GONE);
        holder.llFrame.setBackgroundResource(isSelected ? R.drawable.radio_selected : R.drawable.radio);
        holder.radioButton.setOnClickListener(view -> {
           for (int i = 0; i < personFacesSelectors.size(); i++) {
               if (personFacesSelectors.get(i).isSelected()) {
                   personFacesSelectors.get(i).setSelected(false);
                   notifyItemChanged(i);
                   break;
               }
           }
            personFacesSelectors.get(position).setSelected(true);
            onPictureSelectedListener.onPictureSelected(personFacesSelectors.get(position).getPersonFaces());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return personFacesSelectors.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView radioButton;
        RelativeLayout llFrame;
        TextView tvIsNew;
        ViewHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.first);
            llFrame = itemView.findViewById(R.id.ll_frame);
            tvIsNew = itemView.findViewById(R.id.tv_is_new);
        }
    }

    public interface OnPictureSelectedListener{
        void onPictureSelected(PersonFaces personFaces);
    }
}
