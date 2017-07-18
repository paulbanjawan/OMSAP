package com.example.pcban.omsap.EquipmentReservation;

/**
 * Created by pcban on 11 Jun 2017.
 */

public class Equipments
{
    String EqID,EqName,EqQuantity,EqAvailStock,Mine;

    public String getEqID()
    {
        return EqID;
    }

    public void setEqID(String TempEqID)
    {
        this.EqID = TempEqID;
    }

    public String getMine()
    {
        return Mine;
    }

    public void setMine(String TempMine)
    {
        this.Mine = TempMine;
    }
    public String getEqName()
    {
        return EqName;
    }

    public void setEqName(String TempEqName)
    {
        this.EqName = TempEqName;
    }

    public String getEqQuantity()
    {
        return EqQuantity;
    }

    public void setEqQuantity(String TempEqQuantity)
    {
        this.EqQuantity = TempEqQuantity;
    }

    public String getEqAvailStock()
    {
        return EqAvailStock;
    }

    public void setEqAvailStock(String TempEqAvailStock)
    {
        this.EqAvailStock = TempEqAvailStock;
    }
}
