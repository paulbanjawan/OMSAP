package com.example.pcban.omsap.OrgProposals;

/**
 * Created by pcban on 17 May 2017.
 */

public class ProposalStatus
{
    String SCC_Status,SADU_Status,EDO_Status,SDAS_Status,ACCOUNTING_Status,entity,status;


    public String getEntity()
    {
        return entity;
    }

    public void setEntity(String tempEntity)
    {
        this.entity = tempEntity;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String tempStatus)
    {
        this.status = tempStatus;
    }

    public String getSCC_Status()
    {
        return SCC_Status;
    }

    public void setSCC_Status(String TempSCC_Status)
    {
        this.SCC_Status = TempSCC_Status;
    }

    public String getSADU_Status()
    {
        return SADU_Status;
    }

    public void setSADU_Status(String TempSADU_Status)
    {
        this.SADU_Status = TempSADU_Status;
    }

    public String getEDO_Status()
    {
        return EDO_Status;
    }

    public void setEDO_Status(String TempEDO_Status)
    {
        this.EDO_Status = TempEDO_Status;
    }

    public String getSDAS_Status()
    {
        return SDAS_Status;
    }

    public void setSDAS_Status(String TempSDAS_Status)
    {
        this.SDAS_Status = TempSDAS_Status;
    }

    public String getACCOUNTING_Status()
    {
        return ACCOUNTING_Status;
    }

    public void setACCOUNTING_Status(String TempACCOUNTING_Status)
    {
        this.ACCOUNTING_Status = TempACCOUNTING_Status;
    }

}
