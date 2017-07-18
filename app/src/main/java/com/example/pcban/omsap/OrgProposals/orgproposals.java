package com.example.pcban.omsap.OrgProposals;

/**
 * Created by pcban on 7 May 2017.
 */

public class orgproposals {
    String proposalID,ProposalTitle,OrgGeneralObjective,OrgSpecObjective,OrgBudget,OrgDateSent;
    String SubjectName;

    public String getProposalID()
    {
        return proposalID;
    }

    public void setProposalID(String TempID)
    {
        this.proposalID = TempID;
    }

    public String getProposalTitle()
    {
        return ProposalTitle;
    }

    public void setProposalTitle(String TempProposalTitle)
    {
        this.ProposalTitle = TempProposalTitle;
    }

    public String getOrgDateSent()
    {
        return OrgDateSent;
    }
    public void setOrgDateSent(String TempOrgDate)
    {
        this.OrgDateSent = TempOrgDate;
    }

    public String getOrgGeneralObjective()
    {
        return OrgGeneralObjective;
    }
    public void setOrgGeneralObjective(String tempOrgGenObj)
    {
        this.OrgGeneralObjective = tempOrgGenObj;
    }
}