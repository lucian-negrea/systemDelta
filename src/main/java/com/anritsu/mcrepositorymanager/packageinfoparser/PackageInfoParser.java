/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.packageinfoparser;

import com.anritsu.mcrepositorymanager.shared.Filter;
import com.anritsu.mcrepositorymanager.shared.McPackage;
import java.util.HashSet;

/**
 *
 * @author RO100051
 */
public interface PackageInfoParser {
    public HashSet<McPackage> getPackageList(Filter filter);
    public HashSet<String> getPackageNameList();
    public HashSet<String> getCustomers();
    public HashSet<String> getAvailability();
    public String getMCVersion();
}
