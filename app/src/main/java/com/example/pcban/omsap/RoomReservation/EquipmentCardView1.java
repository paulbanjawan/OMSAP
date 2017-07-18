package com.example.pcban.omsap.RoomReservation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.pcban.omsap.R;

import java.util.List;

/**
 * Created by pcban on 29 May 2017.
 */

public class EquipmentCardView1 extends RecyclerView.Adapter<EquipmentCardView1.ViewHolder>
{
    Context context;
    List<Rooms> Rooms;

    public EquipmentCardView1(List<Rooms> getDataAdapter, Context context)
    {
        super();
        this.Rooms = getDataAdapter;
        this.context = context;
    }


    @Override
    public EquipmentCardView1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_search_cardview, parent, false);
        EquipmentCardView1.ViewHolder viewHolder = new EquipmentCardView1.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EquipmentCardView1.ViewHolder holder, int position)
    {
        Rooms getDataAdapter1 =  Rooms.get(position);
        holder.room.setText(getDataAdapter1.getRoomBldg()+""+getDataAdapter1.getRoomNum());
    }

    @Override
    public int getItemCount()
    {
        return Rooms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView room;

        public ViewHolder(View itemView)
        {
            super(itemView);
            room   =   (TextView) itemView.findViewById(R.id.tvEquipmentName) ;
        }
    }

}
