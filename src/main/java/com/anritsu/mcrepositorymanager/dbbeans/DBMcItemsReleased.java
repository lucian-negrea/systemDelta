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
@Table(name = "mc_items_released")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DBMcItemsReleased.findAll", query = "SELECT d FROM DBMcItemsReleased d"),
    @NamedQuery(name = "DBMcItemsReleased.findByItemId", query = "SELECT d FROM DBMcItemsReleased d WHERE d.itemId = :itemId"),
    @NamedQuery(name = "DBMcItemsReleased.findByItemType", query = "SELECT d FROM DBMcItemsReleased d WHERE d.itemType = :itemType"),
    @NamedQuery(name = "DBMcItemsReleased.findByItemLocation", query = "SELECT d FROM DBMcItemsReleased d WHERE d.itemLocation = :itemLocation")})
public class DBMcItemsReleased implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "item_id")
    private Integer itemId;
    @Basic(optional = false)
    @Column(name = "item_type")
    private String itemType;
    @Basic(optional = false)
    @Column(name = "item_location")
    private String itemLocation;
    @JoinColumn(name = "release_id", referencedColumnName = "release_id")
    @ManyToOne
    private DBMcReleases releaseId;

    public DBMcItemsReleased() {
    }

    public DBMcItemsReleased(Integer itemId) {
        this.itemId = itemId;
    }

    public DBMcItemsReleased(Integer itemId, String itemType, String itemLocation) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.itemLocation = itemLocation;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
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
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DBMcItemsReleased)) {
            return false;
        }
        DBMcItemsReleased other = (DBMcItemsReleased) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.anritsu.mcreleaseportal.dbconnection.dbconnection.beans.DBMcItemsReleased[ itemId=" + itemId + " ]";
    }
    
}
