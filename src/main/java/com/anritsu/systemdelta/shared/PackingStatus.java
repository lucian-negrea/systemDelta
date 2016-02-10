/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ro100051
 */
public class PackingStatus implements Serializable{
    
    private ArrayList<McPackage> packageList = new ArrayList<>();
    private ArrayList<String> downloadedPackages = new ArrayList<>();
    private ArrayList<String> archivedPackages = new ArrayList<>();
    private McPackage processingPackage;
    private long packageDownloadedSize = 0;
    
    

    public long getPackageDownloadedSize() {
        return packageDownloadedSize;
    }

    public void setPackageDownloadedSize(long packageDownloadedSize) {
        this.packageDownloadedSize = packageDownloadedSize;
    }

    public ArrayList<String> getDownloadedPackages() {
        return downloadedPackages;
    }

    public void setDownloadedPackages(ArrayList<String> downloadedPackages) {
        this.downloadedPackages = downloadedPackages;
    }

    public ArrayList<String> getArchivedPackages() {
        return archivedPackages;
    }

    public void setArchivedPackages(ArrayList<String> archivedPackages) {
        this.archivedPackages = archivedPackages;
    }

    public ArrayList<McPackage> getPackageList() {
        return packageList;
    }

    public void setPackageList(ArrayList<McPackage> packageList) {
        this.packageList = packageList;
    }

    public McPackage getProcessingPackage() {
        return processingPackage;
    }

    public void setProcessingPackage(McPackage processingPackage) {
        this.processingPackage = processingPackage;
    }

   
}