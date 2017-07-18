package com.example.pcban.omsap.OrgProposals;

import android.content.Context;
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
 * Created by pcban on 11 Jun 2017.
 */

public class EquipmentCardView extends RecyclerView.Adapter<EquipmentCardView.ViewHolder>
{
    Context context;
    List<com.example.pcban.omsap.OrgProposals.Equipments> Equipments;

    public EquipmentCardView(List<com.example.pcban.omsap.OrgProposals.Equipments> getDataAdapter, Context context)
    {
        super();
        this.Equipments = getDataAdapter;
        this.context = context;
    }


    @Override
    public EquipmentCardView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab5equipment_reservation_cardview, parent, false);
        EquipmentCardView.ViewHolder viewHolder = new EquipmentCardView.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EquipmentCardView.ViewHolder holder, int position)
    {
        com.example.pcban.omsap.OrgProposals.Equipments getDataAdapter1 =  Equipments.get(position);
        holder.equipment_name.setText(getDataAdapter1.getEqName());
        holder.quantity.setText(getDataAdapter1.getEqQuantity());
        holder.date.setText(getDataAdapter1.getDate());

        if(getDataAdapter1.getStatus().equals("1")) //PENDING
        {
            holder.status.setText("Pending");
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.pending));
            holder.imgstatus.setImageResource(R.drawable.pending);

        }
        if(getDataAdapter1.getStatus().equals("2")) //CONFIRMED
        {
            holder.status.setText("Approved");
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.approved));
            holder.imgstatus.setImageResource(R.drawable.checked);
        }
        if(getDataAdapter1.getStatus().equals("3")) //DECLINED
        {
            holder.status.setText("Declined");
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.redhaze));
            holder.imgstatus.setImageResource(R.drawable.declined);

        }
        if(getDataAdapter1.getStatus().equals("4")) //CANCELLED
        {
            holder.status.setText("Cancelled");
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.cancelled));
            holder.imgstatus.setImageResource(R.drawable.cancel);

        }
        if(getDataAdapter1.getStatus().equals("5")) //CANCELLED
        {
            holder.status.setText("With Comments");
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.withcomment));
            holder.imgstatus.setImageResource(R.drawable.danger);

        }

    }

    @Override
    public int getItemCount()
    {
        return Equipments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView equipment_name,quantity,date,status;
        public CardView mCardView;
        public ImageView imgstatus;

        public ViewHolder(View itemView)
        {
            super(itemView);
            equipment_name      =   (TextView) itemView.findViewById(R.id.tvEquipmentName);
            quantity            =   (TextView) itemView.findViewById(R.id.tvQty);
            date                =   (TextView) itemView.findViewById(R.id.tvDate2);
            mCardView           =   (CardView) itemView.findViewById(R.id.cvEquip);
            status          =   (TextView) itemView.findViewById(R.id.tvRsvStatus);
            imgstatus           =   (ImageView) itemView.findViewById(R.id.ivStatus);


        }
    }

}

