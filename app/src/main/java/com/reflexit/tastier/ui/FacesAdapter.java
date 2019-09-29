package com.reflexit.tastier.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.FaceEntity;
import com.reflexit.tastier.utils.FileUtils;
import java.util.List;

public class FacesAdapter extends RecyclerView.Adapter<FacesAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<FaceEntity> faces;

    public FacesAdapter(Context context, List<FaceEntity> faces) {
        this.mInflater = LayoutInflater.from(context);
        this.faces = faces;
    }

    @Override
    @NonNull
    public FacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_face, parent, false);
        return new FacesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacesAdapter.ViewHolder holder, int position) {
        FaceEntity face = faces.get(position);
            String faceUrl = FileUtils.personDirectory + "/" + face.getOwnerID() + "/" + face.getFaceID() + ".jpg";
            Bitmap myBitmap = BitmapFactory.decodeFile(faceUrl);
            holder.sdFace.setImageBitmap(myBitmap);
    }

    @Override
    public int getItemCount() {
        return faces.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sdFace;
        ViewHolder(View itemView) {
            super(itemView);
            sdFace = itemView.findViewById(R.id.sd_face);
        }

    }
}
