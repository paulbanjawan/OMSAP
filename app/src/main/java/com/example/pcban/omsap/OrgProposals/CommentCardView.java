package com.example.pcban.omsap.OrgProposals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pcban.omsap.R;

import java.util.List;

/**
 * Created by pcban on 17 May 2017.
 */

public class CommentCardView extends RecyclerView.Adapter<CommentCardView.ViewHolder>
{
    Context context;

    List<Comments> Comments;

    public CommentCardView (List<Comments> getDataAdapter, Context context) {
        super();
        this.Comments = getDataAdapter;
        this.context = context;
    }

    @Override
    public CommentCardView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab3cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentCardView.ViewHolder holder, int position) {

        Comments getDataAdapter1 =  Comments.get(position);
        holder.comment.setText(getDataAdapter1.getComment());
        holder.commentAuthor.setText(getDataAdapter1.getCommentAuthor());
        holder.commentDate.setText(getDataAdapter1.getCommentDate());
    }

    @Override
    public int getItemCount() {

        return Comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView commentAuthor,comment,commentDate;


        public ViewHolder(View itemView)
        {
            super(itemView);
            commentAuthor   =   (TextView) itemView.findViewById(R.id.tvAuthor) ;
            comment         =   (TextView) itemView.findViewById(R.id.tvComment);
            commentDate     =   (TextView) itemView.findViewById(R.id.tvDate2);
        }
    }
}
