/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.client;

import com.anritsu.systemdelta.shared.Filter;
import com.anritsu.systemdelta.shared.McPackage;
import com.anritsu.systemdelta.shared.PackingStatus;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ro100051
 */
@RemoteServiceRelativePath("gwtsystemdeltaservice")
public interface GWTSystemDeltaService extends RemoteService {

    public ArrayList<String> getAvailabilities(Filter filter);
    public ArrayList<String> getCustomers(Filter filter);
    public ArrayList<McPackage> getPackageList(Filter filter);
    public String generateRepository(ArrayList<McPackage> packages);
    public List<String> getMcVersions();
    public String setMcVersion(Filter f);
    public PackingStatus getPackingStatus();
    public ArrayList<String> getMcComponents(Filter filter);
}
