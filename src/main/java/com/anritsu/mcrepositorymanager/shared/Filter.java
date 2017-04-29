/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RO100051
 */
public class Filter implements Serializable{
    private String q7admOutput="";
    private String mcVersion = "";
    private List<String> availability = new ArrayList<>();
    private String customer = "";
    private List<String> mcComponent = new ArrayList<>();
    private List<MCPackageActivities> activities = new ArrayList<>();
    private boolean recommended = true;
    private String recommendedFilter = "recommended";
    private boolean localDependencies = false;

    public boolean isLocalDependencies() {
        return localDependencies;
    }

    public void setLocalDependencies(boolean localDependencies) {
        this.localDependencies = localDependencies;
    }
    
    public String getRecommendedFilter() {
        return recommendedFilter;
    }

    public void setRecommendedFilter(String recommendedFilter) {
        this.recommendedFilter = recommendedFilter;
    }

    public Filter() {
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    
    public List<MCPackageActivities> getActivities() {
        return activities;
    }

    public void setActivities(List<MCPackageActivities> activities) {
        this.activities = activities;
    }
    
    public List<String> getMcComponent() {
        return mcComponent;
    }

    public void setMcComponent(List<String> mcComponent) {
        this.mcComponent = mcComponent;
    }

    public String getQ7admOutput() {
        return q7admOutput;
    }

    public void setQ7admOutput(String q7admOutput) {
        this.q7admOutput = q7admOutput;
    }

    public String getMcVersion() {
        return mcVersion;
    }

    public void setMcVersion(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    public List<String> getAvailability() {
        return availability;
    }

    public void setAvailability(List<String> availability) {
        this.availability = availability;
    }    
}
