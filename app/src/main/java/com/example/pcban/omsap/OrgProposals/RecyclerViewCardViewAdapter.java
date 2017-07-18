package com.example.pcban.omsap.OrgProposals;

/**
 * Created by pcban on 7 May 2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

import com.example.pcban.omsap.R;

import java.util.List;

public class RecyclerViewCardViewAdapter extends RecyclerView.Adapter<RecyclerViewCardViewAdapter.ViewHolder> {

    Context context;

    List<orgproposals> orgproposals;

    public RecyclerViewCardViewAdapter(List<orgproposals> getDataAdapter, Context context){

        super();

        this.orgproposals = getDataAdapter;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        orgproposals getDataAdapter1 =  orgproposals.get(position);
        if(getDataAdapter1.getProposalTitle().length()>=20)
        {
            holder.ProposalTitle.setText(getDataAdapter1.getProposalTitle()+"...");
        }
        else
        {
            holder.ProposalTitle.setText(getDataAdapter1.getProposalTitle());
        }
        holder.OrgDateSent.setText(getDataAdapter1.getOrgDateSent());
        holder.proposalID.setText(getDataAdapter1.getProposalID());
        holder.OrgGeneralObjective.setText(getDataAdapter1.getOrgGeneralObjective());
    }

    @Override
    public int getItemCount() {

        return orgproposals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ProposalTitle,OrgDateSent,proposalID,OrgGeneralObjective;


        public ViewHolder(View itemView)
        {
            super(itemView);
            ProposalTitle       = (TextView) itemView.findViewById(R.id.TextViewCard) ;
            OrgDateSent         = (TextView) itemView.findViewById(R.id.Date);
            proposalID          = (TextView) itemView.findViewById(R.id.orgid);
            OrgGeneralObjective = (TextView) itemView.findViewById(R.id.tvGenObjective);
        }
    }
}
