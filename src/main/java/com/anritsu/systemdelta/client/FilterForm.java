/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.systemdelta.client;

import com.anritsu.systemdelta.shared.Filter;
import com.anritsu.systemdelta.shared.McPackage;
import com.anritsu.systemdelta.shared.PackingStatus;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import java.util.ArrayList;
import java.util.List;
import org.gwtbootstrap3.client.shared.event.ModalHideEvent;
import org.gwtbootstrap3.client.shared.event.ModalHideHandler;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;

import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;


/**
 *
 * @author ro100051
 */
public class FilterForm extends Composite {

    @UiField
    Select mcVersion;

    @UiField
    Select availability;

    @UiField
    Select customer;

    @UiField
    Select mcComponent;

    @UiField
    TextArea q7admOutput;

    @UiField
    Button filter;

    @UiField
    CellTable packageListTable;

    @UiField
    Button generateRepository;

    @UiField
    Button downloadRepositoryArchive;

    @UiField
    Modal generateModal;

    @UiField
    Button cancelPackageGeneration;

    @UiField
    HTML packageGenerationMessage;

    @UiField
    ProgressBar totalProgressBar;
    
    
    private ListDataProvider<McPackage> dataProvider;
    private Filter f = new Filter();
    private Timer t;

    private static FilterFormUiBinder uiBinder = GWT.create(FilterFormUiBinder.class);

    interface FilterFormUiBinder extends UiBinder<Widget, FilterForm> {
    }

    public FilterForm() {
        initWidget(uiBinder.createAndBindUi(this));
        getService().getMcVersions(getMcVersionsCallback);
        downloadRepositoryArchive.setVisible(false);

        q7admOutput.setHeight("200px");
        q7admOutput.setPlaceholder("Please paste q7adm output");

        mcVersion.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                f.setMcVersion(mcVersion.getSelectedValue());
                getService().setMcVersion(f, setMcVersionCallback);

            }
        });

        //Building the table
        buildPackageListTable();

        cancelPackageGeneration.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                generateModal.hide();

            }
        });

        generateModal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                t.cancel();
            }
        });

        filter.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                downloadRepositoryArchive.setVisible(false);
                downloadRepositoryArchive.setActive(false);
                f.setMcVersion(mcVersion.getSelectedValue());
                f.setCustomer(customer.getSelectedValue());
                f.setAvailability(availability.getAllSelectedValues());
                f.setQ7admOutput(q7admOutput.getText());
                f.setMcComponent(mcComponent.getAllSelectedValues());
                getService().getPackageList(f, getPackageListCallback);
            }
        });

        generateRepository.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                downloadRepositoryArchive.setVisible(false);
                downloadRepositoryArchive.setEnabled(false);
                int downloadCount = 0;
                ArrayList<McPackage> packageList = new ArrayList<>();
                for (McPackage p : dataProvider.getList()) {
                    packageList.add(p);
                    if (p.isAddToRepository()) {
                        downloadCount++;
                    }
                }
                totalProgressBar.setPercent(0);
                packageGenerationMessage.setHTML("");
                generateModal.show();
                getService().generateRepository(packageList, generateRepositoryCallback);

                // Call status each 5 sec
                t = new Timer() {
                    @Override
                    public void run() {
                        getService().getPackingStatus(getPackingStatusCallback);
                    }
                };
                t.scheduleRepeating(1500);

            }
        });

        downloadRepositoryArchive.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open("/DownloadRepoArchive?archiveName=" + downloadRepositoryArchive.getTitle(), "_parent", "location=no");

            }
        });

    }

    public void buildPackageListTable() {
        packageListTable.setPageSize(200);
        TextColumn<McPackage> packageNameColumn = new TextColumn<McPackage>() {
            @Override
            public String getValue(McPackage pack) {
                return pack.getName();
            }
        };

//        TextColumn<McPackage> packageVersionColumn = new TextColumn<McPackage>() {
//            @Override
//            public String getValue(McPackage pack) {
//                return pack.getPackageVersion();
//            }
//        };
        Column<McPackage,SafeHtml> packageVersionColumn = new Column<McPackage,SafeHtml>(
            new SafeHtmlCell()){
            @Override
            public SafeHtml getValue(McPackage object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.appendHtmlConstant("<a href='" + object.getDownloadLink() + "'>");
                sb.appendEscaped(object.getPackageVersion());
                sb.appendHtmlConstant("</a>");
                return sb.toSafeHtml();
                
            }
            };
        

        TextColumn<McPackage> packageQ7admOutputVersionColumn = new TextColumn<McPackage>() {
            @Override
            public String getValue(McPackage pack) {
                return pack.getQ7admOutputVersion();
            }
        };

        TextColumn<McPackage> packageAvailabilityColumn = new TextColumn<McPackage>() {
            @Override
            public String getValue(McPackage pack) {
                return pack.getAvailability();
            }
        };

        Column<McPackage, String> selectedForDownloadColumn = new Column<McPackage, String>(new ButtonCell()) {

            @Override
            public String getValue(McPackage object) {
                //Notify.notify(object.getName() + " ->" + object.isAddToRepository());
                return (object.isAddToRepository()) ? "remove from repository" : "add to repository";
            }

        };

        selectedForDownloadColumn.setFieldUpdater(new FieldUpdater<McPackage, String>() {

            @Override
            public void update(int index, McPackage object, String value) {
                object.setAddToRepository(!object.isAddToRepository());
                Notify.notify("Package " + object.getName() + " " + value);
                packageListTable.redraw();
            }

        });

        packageListTable.addColumn(packageNameColumn, "Package Name");
        packageListTable.addColumn(packageQ7admOutputVersionColumn, "Installed version");
        packageListTable.addColumn(packageVersionColumn, "Package Version");
        packageListTable.addColumn(packageAvailabilityColumn, "Availability");
        packageListTable.addColumn(selectedForDownloadColumn, "Add to repository");
    }

    public void populatePackageListTable(ArrayList<McPackage> packageList) {
        dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(packageListTable);
        List<McPackage> list = dataProvider.getList();

        for (McPackage p : packageList) {
            if (!(f.getQ7admOutput().equals(""))) {
                p.setAddToRepository(p.isMcPackageSuitableForDownload());
            }
            list.add(p);

        }

        // Changing row style when packageversion is greater than q7adm output version
        packageListTable.setRowStyles(new RowStyles<McPackage>() {
            @Override
            public String getStyleNames(McPackage p, int rowIndex) {

                return (p.isMcPackageSuitableForDownload()) ? "info" : "";

            }
        });
    }

    // Get available versions from the server
    AsyncCallback<List<String>> getMcVersionsCallback = new AsyncCallback<List<String>>() {
        @Override
        public void onFailure(Throwable caught) {
            Notify.notify("MC version list could not be generated!\n" + caught.getMessage());
        }

        @Override
        public void onSuccess(List<String> result) {
            for (String s : result) {
                Option o = new Option();
                o.setValue(s);
                o.setName(s);
                o.setText(s);
                mcVersion.add(o);
            }
            mcVersion.refresh();
        }
    };

    // Setting MC version to filter for
    AsyncCallback<String> setMcVersionCallback = new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
            Notify.notify("Ups! there were some errors on server!\n" + caught.getMessage());
        }

        @Override
        public void onSuccess(String result) {
            Notify.notify("MC " + result + " selected.");
            getService().getAvailabilities(f, getAvailabilitiesCallback);
            getService().getCustomers(f, getCustomerCallback);
            getService().getMcComponents(f, getMcComponentCallback);
            filter.setEnabled(true);
        }
    };

    // Generate package list repository
    AsyncCallback<String> generateRepositoryCallback = new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
            Notify.notify("Package list repository could not be generated!\n" + caught.getMessage());
        }

        @Override
        public void onSuccess(String result) {
            Notify.notify("Archive repository succesfully created!");
            t.cancel();
            generateModal.hide();
            downloadRepositoryArchive.setVisible(true);
            downloadRepositoryArchive.setEnabled(true);
            downloadRepositoryArchive.setTitle(result);
        }
    };

    // Get the list of MC packages. Fileter to be implemented!
    AsyncCallback<ArrayList<McPackage>> getPackageListCallback = new AsyncCallback<ArrayList<McPackage>>() {
        @Override
        public void onFailure(Throwable caught) {
            Notify.notify("Package list could not be retrived!\n" + caught.getMessage());
        }

        @Override
        public void onSuccess(ArrayList<McPackage> result) {
            Notify.notify(result.size() + " packages retrived!");
            
            populatePackageListTable(result);
            generateRepository.setEnabled(true);
        }
    };

    // Create an asynchronous callback to handle the result.
    final AsyncCallback<ArrayList<String>> getCustomerCallback = new AsyncCallback<ArrayList<String>>() {

        public void onSuccess(ArrayList<String> result) {
            customer.clear();
            for (String s : result) {
                Option o = new Option();
                o.setValue(s);
                o.setName(s);
                o.setText(s);
                customer.add(o);
            };
            customer.refresh();
            customer.setEnabled(true);

        }

        public void onFailure(Throwable caught) {
            customer.setEnabled(true);
            customer.setHeader("MasterClaw customer list could not be retrived from server!");
        }
    };

    AsyncCallback<ArrayList<String>> getMcComponentCallback = new AsyncCallback<ArrayList<String>>() {
        @Override
        public void onFailure(Throwable caught) {
            availability.setEnabled(true);
            availability.setHeader("MasterClaw availability could not be retrived from server!");
        }

        @Override
        public void onSuccess(ArrayList<String> result) {
            mcComponent.clear();
            for (String s : result) {
                Option o = new Option();
                o.setValue(s);
                o.setText(s);
                o.setName(s);
                mcComponent.add(o);
            }
            mcComponent.refresh();
            mcComponent.setEnabled(true);
        }
    };

    // Create an asynchronous callback to handle the result.
    final AsyncCallback<ArrayList<String>> getAvailabilitiesCallback = new AsyncCallback<ArrayList<String>>() {

        public void onSuccess(ArrayList<String> result) {
            availability.clear();
            for (String s : result) {
                Option o = new Option();
                o.setValue(s);
                o.setName(s);
                o.setText(s);
                availability.add(o);
            }
            availability.refresh();
            availability.setEnabled(true);
        }

        public void onFailure(Throwable caught) {
            availability.setEnabled(true);
            availability.setHeader("MasterClaw availability could not be retrived from server!");
        }
    };

    AsyncCallback<PackingStatus> getPackingStatusCallback = new AsyncCallback<PackingStatus>() {
        @Override
        public void onFailure(Throwable caught) {
            //Notify.notify("Packing status couldn't be retrived!\n" + caught.getMessage());
        }

        @Override
        public void onSuccess(PackingStatus result) {
            long totalSizeToBeProcessed = 1;
            long totalSizeDownloaded = 1;
            long totalSizeArchived = 1;
            for (McPackage p : result.getPackageList()) {
                totalSizeToBeProcessed += p.getPackageSize();
                if (result.getDownloadedPackages().contains(p.getFileName())) {
                    totalSizeDownloaded += p.getPackageSize();
                }
                if (result.getArchivedPackages().contains(p.getFileName())) {
                    totalSizeArchived += p.getPackageSize();
                }
            }

//            Notify.notify("Total: " + totalSizeToBeProcessed +
//                    "\nDownloaded: " + totalSizeDownloaded +
//                    "\nArchived: " + totalSizeArchived);
            double progress = Double.parseDouble(String.valueOf((totalSizeDownloaded) * 100 / (totalSizeToBeProcessed)));
            totalProgressBar.setPercent(progress);
            totalProgressBar.setText("Total progress: [ " + progress + "% ]");

            packageGenerationMessage.setHTML("Processing [" + (result.getArchivedPackages().size() + 1) + "/" + result.getPackageList().size() + "]: " + result.getProcessingPackage().getFileName()
                    + " - [ "
                    + result.getProcessingPackage().getPackageDownloadedSize() / 1024 + " of " + result.getProcessingPackage().getPackageSize() / 1024 + " KB ]");
        }
    };

    public static GWTSystemDeltaServiceAsync getService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        return GWT.create(GWTSystemDeltaService.class);
    }
}
