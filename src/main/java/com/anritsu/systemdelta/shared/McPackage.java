/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.shared;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

/**
 * 
 * @author RO100051
 */
public class McPackage implements Serializable, Comparable<McPackage>{
    private String name;
    private String fileName;
    private String packageVersion;
    private String availability;
    private String downloadLink;
    private String mcVersion;
    private HashSet<String> customerList;
    private boolean addToRepository = true;
    private String q7admOutputVersion;
    private long size;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getQ7admOutputVersion() {
        return q7admOutputVersion;
    }

    public void setQ7admOutputVersion(String q7admOutputVersion) {
        this.q7admOutputVersion = q7admOutputVersion;
    }

    public boolean isAddToRepository() {
        return addToRepository;
    }

    public void setAddToRepository(boolean addToRepository) {
        this.addToRepository = addToRepository;
    }

    public String getMcVersion() {
        return mcVersion;
    }

    public void setMcVersion(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    public HashSet<String> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(HashSet<String> customerList) {
        this.customerList = customerList;
    }

    public McPackage() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.packageVersion);
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
        final McPackage other = (McPackage) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.packageVersion, other.packageVersion)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    @Override
    public int compareTo(McPackage t) {
        return this.getName().toLowerCase().compareTo(t.getName().toLowerCase());
    }
    
    
}
