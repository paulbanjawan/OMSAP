package com.example.pcban.omsap.EquipmentReservation;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pcban.omsap.R;
import com.example.pcban.omsap.RoomReservation.Rooms;

import java.util.List;

/**
 * Created by pcban on 11 Jun 2017.
 */

public class EquipmentCardView extends RecyclerView.Adapter<com.example.pcban.omsap.EquipmentReservation.EquipmentCardView.ViewHolder>
{
    Context context;
    List<com.example.pcban.omsap.EquipmentReservation.Equipments> Equipments;

    public EquipmentCardView(List<Equipments> getDataAdapter, Context context)
    {
        super();
        this.Equipments = getDataAdapter;
        this.context = context;
    }


    @Override
    public com.example.pcban.omsap.EquipmentReservation.EquipmentCardView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_reservation_cardview, parent, false);
        com.example.pcban.omsap.EquipmentReservation.EquipmentCardView.ViewHolder viewHolder = new com.example.pcban.omsap.EquipmentReservation.EquipmentCardView.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(com.example.pcban.omsap.EquipmentReservation.EquipmentCardView.ViewHolder holder, int position)
    {
        Equipments getDataAdapter1 =  Equipments.get(position);
        holder.equipment_name.setText(getDataAdapter1.getEqName());
        holder.quantity.setText("Quantity: "+getDataAdapter1.getEqQuantity());
        holder.available_stocks.setText("Available Stocks: "+getDataAdapter1.getEqAvailStock());
        if(getDataAdapter1.getMine().equals("1"))
        {
            holder.mCardView.setBackgroundColor(ContextCompat.getColor(context, R.color.approved));

        }
    }

    @Override
    public int getItemCount()
    {
        return Equipments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView equipment_name,quantity,available_stocks;
        public CardView mCardView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            equipment_name      =   (TextView) itemView.findViewById(R.id.tvEquipmentName);
            quantity            =   (TextView) itemView.findViewById(R.id.tvQuantity);
            available_stocks    =   (TextView) itemView.findViewById(R.id.tvAvailStocks);
            mCardView           =   (CardView) itemView.findViewById(R.id.cvEquip);


        }
    }

}

