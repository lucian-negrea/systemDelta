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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

/**
 *
 * @author ro100051
 */
public class SelectFilterForm extends Composite {

    @UiField
    ButtonGroup selectMCVersionGroup;

    @UiField
    ButtonGroup selectMCVersionGroupRM;

    @UiField
    ButtonGroup selectMCVersionGroupRSS;

    @UiField
    Form authenticationForm;

    @UiField
    Button releaseManagement;

    @UiField
    Input formPassword;

    @UiField
    Button authenticate;

    @UiField
    Modal serviceDescriptionModal;

    @UiField
    Button serviceDescription;

    @UiField
    HTML serviceDescriptionHtml;

    private String sourceXLS = "";
    private String sourceDB = "";
    private static SelectFilterFormUiBinder uiBinder = GWT.create(SelectFilterFormUiBinder.class);

    interface SelectFilterFormUiBinder extends UiBinder<Widget, SelectFilterForm> {
    }

    public SelectFilterForm() {
        initWidget(uiBinder.createAndBindUi(this));
        selectMCVersionGroupRM.setVisible(false);
        authenticationForm.setVisible(false);
        getService().getSourceXLS(getSourceXLSCallback);
        getService().getSourceDB(getSourceDBCallback);
        getService().getMcVersions(getMcVersionsCallback);
        getService().isAuthenticated(isAuthenticatedCallback);

        releaseManagement.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                releaseManagement.setVisible(false);
                authenticationForm.setVisible(true);
                //getService().isAuthenticated(isAuthenticatedCallback);

            }
        });

        serviceDescription.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                serviceDescriptionModal.show();
            }
        });

        authenticate.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getService().authenticate(formPassword.getText(), authenticateCallback);
            }
        });
    }

    public AsyncCallback<String> getSourceXLSCallback = new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(String result) {
            sourceXLS = result;
        }
    };

    public AsyncCallback<String> getSourceDBCallback = new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(String result) {
            sourceDB = result;
        }
    };

    public AsyncCallback<Boolean> authenticateCallback = new AsyncCallback<Boolean>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(Boolean result) {
            if (result) {
                getService().isAuthenticated(isAuthenticatedCallback);
            } else {
                Notify.notify("Wrong password!");
                formPassword.setText("");
            }
        }
    };

    public AsyncCallback<Boolean> isAuthenticatedCallback = new AsyncCallback<Boolean>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(Boolean result) {
            if (result) {
                selectMCVersionGroupRM.setVisible(true);
                releaseManagement.setVisible(false);
                authenticationForm.setVisible(false);
            } else {
                releaseManagement.setVisible(true);
                authenticationForm.setVisible(false);
            }
        }
    };

    private AsyncCallback<List<String>> getMcVersionsCallback = new AsyncCallback<List<String>>() {
        @Override
        public void onFailure(Throwable caught) {
            Notify.notify("Server connection error: " + caught.getMessage(), NotifyType.INFO);
        }

        @Override
        public void onSuccess(List<String> result) {
            for (String s : result) {
                Button b = new Button();
                b.setSize(ButtonSize.LARGE);
                b.setId(s);
                b.setType(ButtonType.INFO);
                b.setText("Repository Generator: " + s);
                b.setMarginLeft(5);
                b.setMarginRight(5);
                b.setWidth(String.valueOf(100 / result.size() - 1) + "%");
                b.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        Object source = event.getSource();

                        if (source instanceof Button) {
                            Button selectedMCVersionButton = (Button) source;
                            if (sourceXLS.contains(selectedMCVersionButton.getId())) {
                                // Select FilterRSSForm which is using RSS files
                                FilterRSSForm filterRSSForm = new FilterRSSForm(selectedMCVersionButton.getId());
                                RootPanel.get().clear();
                                RootPanel.get().add(filterRSSForm);
                            } else {
                                // Select FilterDBForm which is using DB info
                                FilterDBForm filterDBForm = new FilterDBForm(selectedMCVersionButton.getId());
                                RootPanel.get().clear();
                                RootPanel.get().add(filterDBForm);

                            }
                        }
                    }
                });
                selectMCVersionGroup.add(b);

                Button bRSS = new Button();
                bRSS.setId("RSS" + s);
                bRSS.setSize(ButtonSize.LARGE);
                bRSS.setType(ButtonType.INFO);
                bRSS.setText("Recommendation List: " + s);
                bRSS.setMarginTop(5);
                bRSS.setMarginLeft(5);
                bRSS.setMarginRight(5);
                bRSS.setWidth(String.valueOf(100 / result.size() - 1) + "%");
                bRSS.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        Object source = event.getSource();
                        if (source instanceof Button) {
                            Button selectedMCVersionButton = (Button) source;
                            if (sourceXLS.contains(selectedMCVersionButton.getId())) {
                                Window.Location.assign("http://intranet2.eu.anritsu.com/SBU6/RandD/Release Status Sheet/MC-" + selectedMCVersionButton.getId().replaceAll("RSS", "") + "-ReleaseStatus.xlsx");
                            } else {
                                Window.Location.assign(Window.Location.getHref() + "/DownloadRepoArchive?archiveName=rss/" + "MC-" + selectedMCVersionButton.getId().replaceAll("RSS", "") + "-ReleaseStatus.xlsx");
                            }
                        }
                    }
                });
                selectMCVersionGroupRSS.add(bRSS);

                Button bRM = new Button();
                if (sourceXLS.contains(s)) {
                    bRM.setEnabled(false);
                }
                bRM.setId("RM" + s);
                bRM.setSize(ButtonSize.LARGE);
                bRM.setType(ButtonType.INFO);
                bRM.setText("Release Management: " + s);
                bRM.setMarginTop(5);
                bRM.setMarginLeft(5);
                bRM.setMarginRight(5);
                bRM.setWidth(String.valueOf(100 / result.size() - 1) + "%");
                bRM.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        Object source = event.getSource();
                        if (source instanceof Button) {
                            Button selectedMCVersionButton = (Button) source;
                            RootPanel.get().clear();
                            ReleaseManagement releaseManagement = new ReleaseManagement(selectedMCVersionButton.getId().replaceAll("RM", ""));
                            RootPanel.get().add(releaseManagement);

                        }
                    }
                });
                selectMCVersionGroupRM.add(bRM);
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
