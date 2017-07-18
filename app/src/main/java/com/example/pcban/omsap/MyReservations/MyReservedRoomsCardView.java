package com.example.pcban.omsap.MyReservations;

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
 * Created by pcban on 29 May 2017.
 */

public class MyReservedRoomsCardView extends RecyclerView.Adapter<MyReservedRoomsCardView.ViewHolder>
{
    Context context;
    List<ReservedRooms> ReservedRooms;

    public MyReservedRoomsCardView (List<ReservedRooms> getDataAdapter, Context context)
    {
        super();
        this.ReservedRooms = getDataAdapter;
        this.context = context;
    }

    @Override
    public MyReservedRoomsCardView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.org_room_reserved_cardview, parent, false);
        MyReservedRoomsCardView.ViewHolder viewHolder = new MyReservedRoomsCardView.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyReservedRoomsCardView.ViewHolder holder, int position)
    {
        ReservedRooms getDataAdapter1 =  ReservedRooms.get(position);
        holder.room.setText(getDataAdapter1.getRoomBldg()+""+getDataAdapter1.getRoomNum());
        holder.time.setText(getDataAdapter1.getRsvStartTime()+" - "+getDataAdapter1.getRsvEndTime());


        if(getDataAdapter1.getRsvStartDate().trim().equals(getDataAdapter1.getRsvEndDate().trim()))
        {
            holder.date.setText(getDataAdapter1.getRsvStartDate());
        }
        else
        {
            holder.date.setText(getDataAdapter1.getRsvStartDate()+" to "+getDataAdapter1.getRsvEndDate());
        }
        holder.status.setText(getDataAdapter1.getRsvStatusName());

        if(getDataAdapter1.getRsvStatus().equals("1")) //PENDING
        {
            holder.cvReservedRoom.setBackgroundColor(ContextCompat.getColor(context, R.color.pending));
            holder.imgstatus.setImageResource(R.drawable.pending);

        }
        if(getDataAdapter1.getRsvStatus().equals("2")) //CONFIRMED
        {
            holder.cvReservedRoom.setBackgroundColor(ContextCompat.getColor(context, R.color.approved));
            holder.imgstatus.setImageResource(R.drawable.checked);
        }
        if(getDataAdapter1.getRsvStatus().equals("3")) //DECLINED
        {
            holder.cvReservedRoom.setBackgroundColor(ContextCompat.getColor(context, R.color.redhaze));
            holder.imgstatus.setImageResource(R.drawable.declined);

        }
        if(getDataAdapter1.getRsvStatus().equals("4")) //CANCELLED
        {
            holder.cvReservedRoom.setBackgroundColor(ContextCompat.getColor(context, R.color.cancelled));
            holder.imgstatus.setImageResource(R.drawable.cancel);

        }
        if(getDataAdapter1.getRsvStatus().equals("5")) //CANCELLED
        {
            holder.cvReservedRoom.setBackgroundColor(ContextCompat.getColor(context, R.color.withcomment));
            holder.imgstatus.setImageResource(R.drawable.danger);

        }

    }

    @Override
    public int getItemCount()
    {
        return ReservedRooms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView date,time,room,status;
        public ImageView imgstatus;
        public CardView cvReservedRoom;
        public ViewHolder(View itemView)
        {
            super(itemView);
            room            =   (TextView) itemView.findViewById(R.id.tvEquipmentName);
            date            =   (TextView) itemView.findViewById(R.id.tvDate2);
            time            =   (TextView) itemView.findViewById(R.id.tvTime2);
            status          =   (TextView) itemView.findViewById(R.id.tvRsvStatus);
            imgstatus       =   (ImageView) itemView.findViewById(R.id.ivStatus);
            cvReservedRoom  =   (CardView) itemView.findViewById(R.id.cvReservedRoom);
        }
    }


}
