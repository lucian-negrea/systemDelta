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
@Table(name = "mc_dependencies")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DBMcDependencies.findAll", query = "SELECT d FROM DBMcDependencies d"),
    @NamedQuery(name = "DBMcDependencies.findByDependencyId", query = "SELECT d FROM DBMcDependencies d WHERE d.dependencyId = :dependencyId"),
    @NamedQuery(name = "DBMcDependencies.findByDependencyType", query = "SELECT d FROM DBMcDependencies d WHERE d.dependencyType = :dependencyType"),
    @NamedQuery(name = "DBMcDependencies.findByRequiredComponent", query = "SELECT d FROM DBMcDependencies d WHERE d.requiredComponent = :requiredComponent"),
    @NamedQuery(name = "DBMcDependencies.findByRequiredVersion", query = "SELECT d FROM DBMcDependencies d WHERE d.requiredVersion = :requiredVersion"),
    @NamedQuery(name = "DBMcDependencies.findByRequiredLocal", query = "SELECT d FROM DBMcDependencies d WHERE d.requiredLocal = :requiredLocal")})
public class DBMcDependencies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dependency_id")
    private Integer dependencyId;
    @Column(name = "dependency_type")
    private String dependencyType;
    @Basic(optional = false)
    @Column(name = "required_component")
    private String requiredComponent;
    @Basic(optional = false)
    @Column(name = "required_version")
    private String requiredVersion;
    @Column(name = "required_local")
    private String requiredLocal;
    @JoinColumn(name = "availability_id", referencedColumnName = "availability_id")
    @ManyToOne
    private DBMcAvailabilities availabilityId;

    public DBMcDependencies() {
    }

    public DBMcDependencies(Integer dependencyId) {
        this.dependencyId = dependencyId;
    }

    public DBMcDependencies(Integer dependencyId, String requiredComponent, String requiredVersion) {
        this.dependencyId = dependencyId;
        this.requiredComponent = requiredComponent;
        this.requiredVersion = requiredVersion;
    }

    public Integer getDependencyId() {
        return dependencyId;
    }

    public void setDependencyId(Integer dependencyId) {
        this.dependencyId = dependencyId;
    }

    public String getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(String dependencyType) {
        this.dependencyType = dependencyType;
    }

    public String getRequiredComponent() {
        return requiredComponent;
    }

    public void setRequiredComponent(String requiredComponent) {
        this.requiredComponent = requiredComponent;
    }

    public String getRequiredVersion() {
        return requiredVersion;
    }

    public void setRequiredVersion(String requiredVersion) {
        this.requiredVersion = requiredVersion;
    }

    public String getRequiredLocal() {
        return requiredLocal;
    }

    public void setRequiredLocal(String requiredLocal) {
        this.requiredLocal = requiredLocal;
    }

    public DBMcAvailabilities getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(DBMcAvailabilities availabilityId) {
        this.availabilityId = availabilityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dependencyId != null ? dependencyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DBMcDependencies)) {
            return false;
        }
        DBMcDependencies other = (DBMcDependencies) object;
        if ((this.dependencyId == null && other.dependencyId != null) || (this.dependencyId != null && !this.dependencyId.equals(other.dependencyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.anritsu.mcreleaseportal.dbconnection.dbconnection.beans.DBMcDependencies[ dependencyId=" + dependencyId + " ]";
    }
    
}
