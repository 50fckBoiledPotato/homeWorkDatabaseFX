package com.pepper.homeWorkDatabaseFx;

import java.time.LocalDate;


public class IncomeJoinPartner 
{
    private Integer incomeId;
    private Integer partnerIdIn;    
    private int InAmount;
    private String InProject;
    private LocalDate InCreated;
    private LocalDate InApproved;
    private  String partName;
    private String partContact;
    private Long projectCount;

    public IncomeJoinPartner(Integer incId, Integer partnerIdIn, int amount, String project, LocalDate created, LocalDate approved, String name, String contact) {
        this.incomeId = incId;
        this.partnerIdIn = partnerIdIn;        
        this.InAmount = amount;
        this.InProject = project;
        this.InCreated = created;
        this.InApproved = approved;        
        this.partName = name;
        this.partContact = contact;
        projectCount= 0L;
    }

    public IncomeJoinPartner(String name, String contact, Long projectCount) {
        this.partName = name;
        this.partContact = contact;
        this.projectCount = projectCount;
    }
    

    public int getId() {
        return incomeId;
    }
    public Integer getPartnerIdIn() {
        return partnerIdIn;
    }
    public int getAmount() {
        return InAmount;
    }
    public String getProject() {
        return InProject;
    }
    public LocalDate getCreated() {
        return InCreated;
    }
    public LocalDate getApproved() {
        return InApproved;
    }
    public Integer getIncomeId() {
        return incomeId;
    }
    public Long getProjectCount() {
        return projectCount;
    }
    
    
    public String getName() {
        return partName;
    }
    public String getContact() {
        return partContact;
    }

    
    public void setAmount(int amount) {
        this.InAmount = amount;
    }
    public void setProject(String project) {
        this.InProject = project;
    }
    public void setCreated(LocalDate created) {
        this.InCreated = created;
    }
    public void setApproved(LocalDate approved) {
        this.InApproved = approved;
    }
    public void setName(String name) {
        this.partName = name;
    }
    public void setContact(String contact) {
        this.partContact = contact;
    }
    
    
}
