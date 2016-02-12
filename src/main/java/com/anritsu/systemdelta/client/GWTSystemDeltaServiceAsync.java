/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.client;

import com.anritsu.systemdelta.shared.Filter;
import com.anritsu.systemdelta.shared.McPackage;
import com.anritsu.systemdelta.shared.PackingStatus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ro100051
 */
public interface GWTSystemDeltaServiceAsync {

    public void getPackageList(Filter filter, AsyncCallback<ArrayList<McPackage>> getPackageListCallback);
    public void generateRepository(ArrayList<McPackage> packages, AsyncCallback<String> generateRepositoryCallback);

    public void getMcVersions(AsyncCallback<List<String>> getMcVersionsCallback);

    public void getAvailabilities(Filter filter, AsyncCallback<ArrayList<String>> getAvailabilitiesCallback);

    public void getCustomers(Filter filter, AsyncCallback<ArrayList<String>> getCustomersCallback);

    public void setMcVersion(Filter f, AsyncCallback<String> setMcVersionCallback);

    public void getPackingStatus(AsyncCallback<PackingStatus> getPackingStatusCallback);

    public void getMcComponents(Filter filter, AsyncCallback<ArrayList<String>> asyncCallback);


    
    
}
