/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.dbbeans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ro100051
 */
@Entity
@Table(name = "mc_activities")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DBMcActivities.findAll", query = "SELECT d FROM DBMcActivities d"),
    @NamedQuery(name = "DBMcActivities.findByActivityId", query = "SELECT d FROM DBMcActivities d WHERE d.activityId = :activityId"),
    @NamedQuery(name = "DBMcActivities.findByActivityType", query = "SELECT d FROM DBMcActivities d WHERE d.activityType = :activityType"),
    @NamedQuery(name = "DBMcActivities.findByActivityActivityId", query = "SELECT d FROM DBMcActivities d WHERE d.activityActivityId = :activityActivityId"),
    @NamedQuery(name = "DBMcActivities.findByActivityUri", query = "SELECT d FROM DBMcActivities d WHERE d.activityUri = :activityUri"),
    @NamedQuery(name = "DBMcActivities.findByActivityText", query = "SELECT d FROM DBMcActivities d WHERE d.activityText = :activityText")})
public class DBMcActivities implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "activity_id")
    private Integer activityId;
    @Basic(optional = false)
    @Column(name = "activity_type")
    private String activityType;
    @Column(name = "activity_activity_id")
    private String activityActivityId;
    @Column(name = "activity_uri")
    private String activityUri;
    @Column(name = "activity_text")
    private String activityText;
    @JoinColumn(name = "release_id", referencedColumnName = "release_id")
    @ManyToOne(optional = false)
    private DBMcReleases releaseId;

    public DBMcActivities() {
    }

    public DBMcActivities(Integer activityId) {
        this.activityId = activityId;
    }

    public DBMcActivities(Integer activityId, String activityType) {
        this.activityId = activityId;
        this.activityType = activityType;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityActivityId() {
        return activityActivityId;
    }

    public void setActivityActivityId(String activityActivityId) {
        this.activityActivityId = activityActivityId;
    }

    public String getActivityUri() {
        return activityUri;
    }

    public void setActivityUri(String activityUri) {
        this.activityUri = activityUri;
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
    }

    public DBMcReleases getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(DBMcReleases releaseId) {
        this.releaseId = releaseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityId != null ? activityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DBMcActivities)) {
            return false;
        }
        DBMcActivities other = (DBMcActivities) object;
        if ((this.activityId == null && other.activityId != null) || (this.activityId != null && !this.activityId.equals(other.activityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.anritsu.mcreleaseportal.dbconnection.dbconnection.beans.DBMcActivities[ activityId=" + activityId + " ]";
    }
    
}
