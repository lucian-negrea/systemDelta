/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author ro100051
 */
public class RecommendedMcPackage implements Serializable, Comparable<RecommendedMcPackage>{
    private String packageName;
    private ArrayList<McPackage> packageVersions = new ArrayList<>();
    private String tier, group, availability, notes;
    private McPackage recommendedVersion;
    private boolean packageInfoModified = false;
    private boolean recommendedVersionCandidate = false;    
    private int rowIndex;
    // Hide package name-availability if all isRecommended is false
    private boolean showInTable = false;
    private String mcVersion;

    public String getMcVersion() {
        return mcVersion;
    }

    public void setMcVersion(String mcVersion) {
        this.mcVersion = mcVersion;
    }
    

    public McPackage getRecommendedVersion() {
        return recommendedVersion;
    }

    public void setRecommendedVersion(McPackage recommendedVersion) {
        this.recommendedVersion = recommendedVersion;
    }

    public ArrayList<McPackage> getPackageVersions() {
        return packageVersions;
    }

    public void setPackageVersions(ArrayList<McPackage> packageVersions) {
        this.packageVersions = packageVersions;
    }
   
    public boolean isShowInTable() {
        return showInTable;
    }    

    public void setShowInTable(boolean showInTable) {
        this.showInTable = showInTable;
    }

    public void setRecommendedVersionCandidate(boolean recommendedVersionCandidate) {
        this.recommendedVersionCandidate = recommendedVersionCandidate;
    }

    public boolean isRecommendedVersionCandidate() {
        return recommendedVersionCandidate;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.packageName);
        hash = 37 * hash + Objects.hashCode(this.packageVersions);
        hash = 37 * hash + Objects.hashCode(this.availability);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RecommendedMcPackage other = (RecommendedMcPackage) obj;
        if (!Objects.equals(this.packageName, other.packageName)) {
            return false;
        }
        if (!Objects.equals(this.availability, other.availability)) {
            return false;
        }
        if (!Objects.equals(this.packageVersions, other.packageVersions)) {
            return false;
        }
        return true;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
   
    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isPackageInfoModified() {
        return packageInfoModified;
    }

    public void setPackageInfoModified(boolean packageInfoModified) {
        this.packageInfoModified = packageInfoModified;
    }

    @Override
    public int compareTo(RecommendedMcPackage t) {
        return this.getPackageName().compareToIgnoreCase(t.getPackageName());
    }
    
    
}
