package com.example.pcban.omsap.OrgProposals;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pcban.omsap.R;

import java.util.List;

/**
 * Created by pcban on 17 May 2017.
 */

public class StatusCardView extends RecyclerView.Adapter<StatusCardView.ViewHolder>
{
    Context context;

    List<ProposalStatus> ProposalStatus;

    public StatusCardView (List<ProposalStatus> getDataAdapter, Context context) {
        super();
        this.ProposalStatus = getDataAdapter;
        this.context = context;
    }

    @Override
    public StatusCardView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab2statuscardview, parent, false);

        StatusCardView.ViewHolder viewHolder = new StatusCardView.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StatusCardView.ViewHolder holder, int position) {
        /*
        * 1 - for review - light  blue
        * 2 - with comment - red
        * 3 - approved - green
        * */
        ProposalStatus getDataAdapter =  ProposalStatus.get(position);


        if(getDataAdapter.getStatus().equals("1"))
        {
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.review));


            holder.ivStatus.setImageResource(R.drawable.eyeoutline);
            holder.statusEntity.setText(getDataAdapter.getEntity());
            holder.statusName.setText("For Review");
            holder.statusInfo.setText("The proposal is currently on review");
        }
        /*else*/ if (getDataAdapter.getStatus().equals("2"))
        {
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.withcomment));

            holder.ivStatus.setImageResource(R.drawable.danger);

            holder.statusEntity.setText(getDataAdapter.getEntity());
            holder.statusName.setText("With Comment");
            holder.statusInfo.setText("See the comments tab");
        }
        /*else*/ if (getDataAdapter.getStatus().equals("3"))
        {
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.approved));

            holder.ivStatus.setImageResource(R.drawable.checked);
            holder.statusEntity.setText(getDataAdapter.getEntity());
            holder.statusName.setText("Approved");
            holder.statusInfo.setText("This proposal has ben approved");
        }
        /*else*/if (getDataAdapter.getStatus().equals("0"))
        {
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.nostatus));
//            holder.ivStatus.setImageResource(R.drawable.checked);
            holder.ivStatus.setVisibility(View.INVISIBLE);
            holder.statusEntity.setText(getDataAdapter.getEntity());
            holder.statusName.setText("Unavailable");
            holder.statusInfo.setText("");
        }

//        holder.statusName.setText(getDataAdapter.getStatus());

    }

    @Override
    public int getItemCount() {

        return ProposalStatus.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView statusInfo,statusName,statusEntity;
        public CardView mCardView;
        public ImageView ivStatus;
        public ViewHolder(View itemView)
        {
            super(itemView);
            statusName      =   (TextView) itemView.findViewById(R.id.tvStatusName);
            statusInfo      =   (TextView) itemView.findViewById(R.id.tvStatusInfo);
            statusEntity    =   (TextView) itemView.findViewById(R.id.tvEntity);
            mCardView       =   (CardView) itemView.findViewById(R.id.cvComment);
            ivStatus        =   (ImageView)itemView.findViewById(R.id.iconStatus);

        }
    }
}
