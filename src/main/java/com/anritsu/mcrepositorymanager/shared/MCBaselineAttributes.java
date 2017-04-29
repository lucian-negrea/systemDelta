/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author RO100051
 */
public class MCBaselineAttributes implements Serializable{
    
    private String mcVersion;
    private HashSet<String> customers;
    private HashSet<String> availabilities;
    private HashSet<String> packageNames;
    private HashSet<MCPackageActivities> packageActivities;

    public HashSet<MCPackageActivities> getPackageActivities() {
        return packageActivities;
    }

    public void setPackageActivities(HashSet<MCPackageActivities> packageActivities) {
        this.packageActivities = packageActivities;
    }
    
    public String getMcVersion() {
        return mcVersion;
    }

    public void setMcVersion(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    public HashSet<String> getCustomers() {
        return customers;
    }

    public void setCustomers(HashSet<String> customers) {
        this.customers = customers;
    }

    public HashSet<String> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(HashSet<String> availabilities) {
        this.availabilities = availabilities;
    }

    public HashSet<String> getPackageNames() {
        return packageNames;
    }

    public void setPackageNames(HashSet<String> packageNames) {
        this.packageNames = packageNames;
    } 
    
}
