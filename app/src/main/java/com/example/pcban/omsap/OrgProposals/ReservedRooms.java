package com.example.pcban.omsap.OrgProposals;

/**
 * Created by pcban on 29 May 2017.
 */

public class ReservedRooms
{
    String RoomID,RoomBldg,RoomNum,RsvID,RsvStartDate,RsvEndDate,RsvStartTime,RsvEndTime,RsvStatus,RsvStatusName;

    public String getRsvStatusName()
    {
        return RsvStatusName;
    }

    public void setRsvStatusName(String TempRsvStatusName)
    {
        this.RsvStatusName = TempRsvStatusName;
    }

    public String getRsvStatus()
    {
        return RsvStatus;
    }

    public void setRsvStatus(String TempRsvStatus)
    {
        this.RsvStatus = TempRsvStatus;
    }

    public String getRsvStartTime()
    {
        return RsvStartTime;
    }

    public void setRsvStartTime(String TempRsvStartTime)
    {
        this.RsvStartTime = TempRsvStartTime;
    }

    public String getRsvEndTime()
    {
        return RsvEndTime;
    }

    public void setRsvRsvEndTime(String TempRsvEndTime)
    {
        this.RsvEndTime = TempRsvEndTime;
    }

    public String getRsvEndDate()
    {
        return RsvEndDate;
    }

    public void setRsvEndDate(String TempRsvEndDate)
    {
        this.RsvEndDate = TempRsvEndDate;
    }


    public void setRsvID(String TempRsvID)
    {
        this.RsvID = TempRsvID;
    }

    public String getRsvID()
    {
        return RsvID;
    }

    public void setRsvStartDate(String TempRsvStartDate)
    {
        this.RsvStartDate = TempRsvStartDate;
    }

    public String getRsvStartDate()
    {
        return RsvStartDate;
    }

    public void setRoomID(String TempRoomID)
    {
        this.RoomID = TempRoomID;
    }
    public String getRoomBldg()
    {
        return RoomBldg;
    }

    public void setRoomBldg(String TempRoomBldg)
    {
        this.RoomBldg = TempRoomBldg;
    }

    public String getRoomNum()
    {
        return RoomNum;
    }

    public void setRoomNum(String TempRoomNum)
    {
        this.RoomNum = TempRoomNum;
    }
}
