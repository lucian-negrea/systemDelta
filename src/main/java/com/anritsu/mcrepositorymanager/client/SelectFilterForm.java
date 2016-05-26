/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

/**
 *
 * @author ro100051
 */
public class SelectFilterForm extends Composite {
    
    @UiField
    ButtonGroup selectMCVersionGroup;
    
    
    private static SelectFilterFormUiBinder uiBinder = GWT.create(SelectFilterFormUiBinder.class);
    
    interface SelectFilterFormUiBinder extends UiBinder<Widget, SelectFilterForm> {
    }
    
    public SelectFilterForm() {
        getService().getMcVersions(getMcVersionsCallback);
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    private AsyncCallback<List<String>> getMcVersionsCallback = new AsyncCallback<List<String>>() {
        @Override
        public void onFailure(Throwable caught) {
            Notify.notify("Server connection error: " + caught.getMessage(), NotifyType.INFO);
        }

        @Override
        public void onSuccess(List<String> result) {
            for(String s: result){
                Button b = new Button();
                b.setId(s);
                b.setType(ButtonType.INFO);
                b.setText(s);
                b.setMarginLeft(5);
                b.setMarginRight(5);
                b.setWidth(String.valueOf(100/result.size() - 1) + "%");
                b.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        Object source = event.getSource();
                        
                        if(source instanceof Button){
                            Button selectedMCVersionButton = (Button) source;
                            if(selectedMCVersionButton.getId().equalsIgnoreCase("8.0")){
                                // Select FilterDBForm which is using DB info
                                FilterDBForm filterDBForm = new FilterDBForm(selectedMCVersionButton.getId());
                                RootPanel.get().clear();
                                RootPanel.get().add(filterDBForm);                                
                            }else{
                                // Select FilterRSSForm which is using RSS files
                                FilterRSSForm filterRSSForm = new FilterRSSForm(selectedMCVersionButton.getId());
                                RootPanel.get().clear();
                                RootPanel.get().add(filterRSSForm);
                                
                            }
                        }
                    }
                });
                selectMCVersionGroup.add(b);
            }
        }
    };
    
    public static GWTMCRepositoryManagerServiceAsync getService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        return GWT.create(GWTMCRepositoryManagerService.class);
    }
}
