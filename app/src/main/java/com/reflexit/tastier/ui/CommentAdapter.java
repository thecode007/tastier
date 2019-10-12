package com.reflexit.tastier.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Comment;
import java.util.List;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment>  comments) {
        this.mInflater = LayoutInflater.from(context);
        this.comments = comments;
    }

    @Override
    @NonNull
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.tvComment.setText(comments.get(position).getComment());
    }


    public void add(Comment comment) {
        comments.add(comment);
        notifyItemInserted(comments.size() - 1);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvComment;
        ViewHolder(View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
