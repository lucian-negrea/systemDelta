/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.shared;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ro100051
 */
public class MCPackageActivities implements Serializable, Comparable<MCPackageActivities> {
    private String activityId;
    private String activityText;
    private String activityType;
    private String releaseId;

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.activityId);
        hash = 89 * hash + Objects.hashCode(this.activityText);
        hash = 89 * hash + Objects.hashCode(this.activityType);
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
        final MCPackageActivities other = (MCPackageActivities) obj;
        if (!Objects.equals(this.activityId, other.activityId)) {
            return false;
        }
        if (!Objects.equals(this.activityText, other.activityText)) {
            return false;
        }
        if (!Objects.equals(this.activityType, other.activityType)) {
            return false;
        }
        return true;
    }

   
    
    
    
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    @Override
    public int compareTo(MCPackageActivities activity) {
        return this.getActivityId().compareToIgnoreCase(activity.getActivityId());
    }

    @Override
    public String toString() {
        return activityType + activityId;
    }
    
}
