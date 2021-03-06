/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.client;

import com.anritsu.mcrepositorymanager.client.utils.DynamicSelectionCell;
import com.anritsu.mcrepositorymanager.client.utils.TextAreaInputCell;
import com.anritsu.mcrepositorymanager.shared.Filter;
import com.anritsu.mcrepositorymanager.shared.MCPackageActivities;
import com.anritsu.mcrepositorymanager.shared.McPackage;
import com.anritsu.mcrepositorymanager.shared.RecommendedMcPackage;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Lead;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

/**
 *
 * @author ro100051
 */
public class ReleaseManagement extends Composite {

    @UiField
    Button returnToBaselineSelection;

    @UiField
    Button refreshTable;

    @UiField
    Button showAllPackages;

    @UiField
    Lead selectedVersion;

    @UiField
    CellTable packageListTable;

    @UiField
    HorizontalPanel hPanelLoading;

    @UiField
    Button exportRSS;
    
    @UiField
    Button serviceDescription;
    
    @UiField
    Modal serviceDescriptionModal;

    ArrayList<RecommendedMcPackage> mcPackages = new ArrayList<>();

    final String mcVersion;

    private ListDataProvider<RecommendedMcPackage> dataProvider = new ListDataProvider<>();

    private static ReleaseManagementUiBinder uiBinder = GWT.create(ReleaseManagementUiBinder.class);

    interface ReleaseManagementUiBinder extends UiBinder<Widget, ReleaseManagement> {
    }

    public ReleaseManagement(String mcVer) {
        initWidget(uiBinder.createAndBindUi(this));
        hPanelLoading.setVisible(true);
        packageListTable.setVisible(false);
        this.mcVersion = mcVer;
        Filter filter = new Filter();
        filter.setMcVersion(mcVer);

        // Cosmetic for buttons
        returnToBaselineSelection.setMarginLeft(5);
        returnToBaselineSelection.setMarginRight(5);
        returnToBaselineSelection.setMarginBottom(5);

        refreshTable.setMarginLeft(5);
        refreshTable.setMarginRight(5);
        refreshTable.setMarginBottom(5);

        showAllPackages.setMarginLeft(5);
        showAllPackages.setMarginRight(5);
        showAllPackages.setMarginBottom(5);

        exportRSS.setMarginLeft(5);
        exportRSS.setMarginRight(5);
        exportRSS.setMarginBottom(5);

        // Initiate PackageInfoParser with selected MC version
        getService().initiateParser(filter, initiateParserCallback);

        selectedVersion.setText("MasterClaw " + mcVer + " recommendation list:");
        
        serviceDescription.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                serviceDescriptionModal.show();
            }
        });
        
        returnToBaselineSelection.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RootPanel.get().clear();
                SelectFilterForm selectFilterForm = new SelectFilterForm();
                RootPanel.get().add(selectFilterForm);
            }
        });

        refreshTable.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RootPanel.get().clear();
                ReleaseManagement releaseManagement = new ReleaseManagement(mcVersion);
                RootPanel.get().add(releaseManagement);
            }
        });
        
        showAllPackages.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (((Button) event.getSource()).getText().contains("Show")) {
                    ((Button) event.getSource()).setText("Hide marked packages");
                    for (RecommendedMcPackage p : mcPackages) {
                        if (!dataProvider.getList().contains(p)) {
                            dataProvider.getList().add(p);
                        }
                    }
                    packageListTable.redraw();

                } else if (((Button) event.getSource()).getText().contains("Hide")) {
                    ((Button) event.getSource()).setText("Show all packages");
                    for (RecommendedMcPackage p : mcPackages) {
                        if (!p.isShowInTable()) {
                            dataProvider.getList().remove(p);
                        }
                    }
                }
            }
        });

        exportRSS.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getService().generateRSS(new HashSet(mcPackages), generateRSSAsyncCallback);
            }
        });

    }

    AsyncCallback<String> generateRSSAsyncCallback = new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(String result) {
            Window.open("/DownloadRepoArchive?archiveName=" + result, "_parent", "location=no");
        }
    };

    // Initiate parser callback
    AsyncCallback<Boolean> initiateParserCallback = new AsyncCallback<Boolean>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(Boolean result) {
            if (result) {
                getService().getPackageListForReleaseManagement(getPackageListForReleaseManagementCallback);
            }
        }
    };

    AsyncCallback<ArrayList<RecommendedMcPackage>> getPackageListForReleaseManagementCallback = new AsyncCallback<ArrayList<RecommendedMcPackage>>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(ArrayList<RecommendedMcPackage> result) {
            Collections.sort(result);
            mcPackages = result;
            buildPackageListTable(packageListTable);
            populatePackageListtable(mcPackages);
            hPanelLoading.setVisible(false);
            packageListTable.setVisible(true);
        }

    };

    AsyncCallback<RecommendedMcPackage> updateRecommendedVersionCallback = new AsyncCallback<RecommendedMcPackage>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(RecommendedMcPackage result) {
            Notify.notify("Package " + result.getPackageName() + " succesfully updated!");
        }
    };

    AsyncCallback<RecommendedMcPackage> removeFromTableCallback = new AsyncCallback<RecommendedMcPackage>() {
        @Override
        public void onFailure(Throwable caught) {

        }

        @Override
        public void onSuccess(RecommendedMcPackage result) {
            Notify.notify(result.getPackageName() + "-" + result.getAvailability() + ": removed from table!");
            if (showAllPackages.getText().contains("Show")) {
                dataProvider.getList().remove(result);
            }
            packageListTable.redraw();
        }
    };

    public void populatePackageListtable(ArrayList<RecommendedMcPackage> packageList) {
        //Notify.notify("Adding: " + packageList.size() + " packages!");

        dataProvider.addDataDisplay(packageListTable);

        List<RecommendedMcPackage> list = dataProvider.getList();
        list.clear();

        for (RecommendedMcPackage p : packageList) {
            if (p.isShowInTable()) {
                list.add(p);
            }
        }

        // Changing row style when new recommended candidate available        
        packageListTable.setRowStyles(new RowStyles<RecommendedMcPackage>() {
            @Override
            public String getStyleNames(RecommendedMcPackage p, int rowIndex) {
                String result = "";
                if (p.isRecommendedVersionCandidate()) {
                    result = "info";
                }
                if (p.getRecommendedVersion() == null) {
                    result = "danger";
                }

                return result;
            }
        });
    }

    public void buildPackageListTable(final CellTable table) {
        table.setPageSize(200);

        // Attach a column sort handler 
        ListHandler<RecommendedMcPackage> sortHandler = new ListHandler<RecommendedMcPackage>(dataProvider.getList());
        table.addColumnSortHandler(sortHandler);

        // Hide from table
        Column<RecommendedMcPackage, String> hidePackage = new Column<RecommendedMcPackage, String>(new ButtonCell(ButtonType.INFO, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(RecommendedMcPackage object) {
                ((ButtonCell) this.getCell()).setEnabled(object.isShowInTable());
                return "Remove";
            }
        };
        hidePackage.setFieldUpdater(new FieldUpdater<RecommendedMcPackage, String>() {
            @Override
            public void update(int index, RecommendedMcPackage object, String value) {
                object.setShowInTable(false);
                table.redraw();
                getService().removeFromTable(object, removeFromTableCallback);
            }
        });

        // Component Tier
        TextColumn<RecommendedMcPackage> packageTier = new TextColumn<RecommendedMcPackage>() {
            @Override
            public String getValue(RecommendedMcPackage pack) {
                return pack.getTier().replaceAll("Anritsu/MasterClaw/", "");
            }
        };

        // Component Group
        TextColumn<RecommendedMcPackage> packageGroup = new TextColumn<RecommendedMcPackage>() {
            @Override
            public String getValue(RecommendedMcPackage pack) {
                return pack.getGroup();
            }
        };

        // Component name
        Column<RecommendedMcPackage, String> packageName = new Column<RecommendedMcPackage, String>(new ButtonCell(ButtonType.LINK)) {
            @Override
            public String getValue(RecommendedMcPackage object) {
                return object.getPackageName();
            }
        };
        packageName.setSortable(true);
        packageName.setDefaultSortAscending(true);
        sortHandler.setComparator(packageName, new Comparator<RecommendedMcPackage>() {
            @Override
            public int compare(RecommendedMcPackage t1, RecommendedMcPackage t2) {
                return t1.getPackageName().compareTo(t2.getPackageName());
            }
        });

        // Component version        
        Column<RecommendedMcPackage, String> packageVersion = new Column<RecommendedMcPackage, String>(new DynamicSelectionCell(new ArrayList())) {
            @Override
            public String getValue(RecommendedMcPackage object) {
                ((DynamicSelectionCell) this.getCell()).clear();
                String recommendedVersion = "n\\a";
                if (object.getRecommendedVersion() != null && object.getRecommendedVersion().getPackageVersion() != null && object.getMcVersion().equalsIgnoreCase(mcVersion)) {
                    recommendedVersion = object.getRecommendedVersion().getPackageVersion();
                }
                ((DynamicSelectionCell) this.getCell()).addOption(recommendedVersion);
                for (McPackage version : object.getPackageVersions()) {
                    String v = version.getPackageVersion();
                    if (!v.equalsIgnoreCase(recommendedVersion)) {
                        ((DynamicSelectionCell) this.getCell()).addOption(version.getPackageVersion());
                    }

                }
                return recommendedVersion;
            }
        };
        
        packageVersion.setFieldUpdater(new FieldUpdater<RecommendedMcPackage, String>() {
            @Override
            public void update(int index, RecommendedMcPackage object, String value) {
                object.setPackageInfoModified(true);
                for (McPackage p : object.getPackageVersions()) {
                    if (p.getPackageVersion().equalsIgnoreCase(value)) {
                        object.setRecommendedVersion(p);
                    }
                }
                table.redrawRow(index);
            }
        });

        // Component availability
        TextColumn<RecommendedMcPackage> packageAvailability = new TextColumn<RecommendedMcPackage>() {
            @Override
            public String getValue(RecommendedMcPackage pack) {
                return pack.getAvailability();
            }
        };
        packageAvailability.setSortable(true);
        sortHandler.setComparator(packageAvailability, new Comparator<RecommendedMcPackage>() {
            @Override
            public int compare(RecommendedMcPackage t1, RecommendedMcPackage t2) {
                return t1.getAvailability().compareTo(t2.getAvailability());
            }
        });

        // Component customers
        TextColumn<RecommendedMcPackage> packageCustomers = new TextColumn<RecommendedMcPackage>() {
            @Override
            public String getValue(RecommendedMcPackage pack) {
                if (pack.getRecommendedVersion() != null && !pack.getRecommendedVersion().getCustomerList().contains("All")) {
                    return Arrays.toString(pack.getRecommendedVersion().getCustomerList().toArray());
                }
                return "";
            }
        };
        
        // Component release date
        TextColumn<RecommendedMcPackage> packageReleaseDate = new TextColumn<RecommendedMcPackage>() {
            @Override
            public String getValue(RecommendedMcPackage pack) {
                StringBuilder dateFormated = new StringBuilder();
                if (pack.getRecommendedVersion() != null && pack.getRecommendedVersion().getReleaseDate() != null) {
                    String[] releaseDateArray = pack.getRecommendedVersion().getReleaseDate().toString().split(" ");
                    dateFormated.append(releaseDateArray[2]).append(".").append(releaseDateArray[1]).append(".").append(releaseDateArray[5]);
                    return dateFormated.toString();
                }
                return "";
            }
        };
        
        // Component lessRecommended
        Column<RecommendedMcPackage, Boolean> packageLessRecommended = new Column<RecommendedMcPackage,Boolean>(new CheckboxCell(true,false)) {
            @Override
            public Boolean getValue(RecommendedMcPackage pack) {
                if (pack.getRecommendedVersion() != null) {
                    return pack.getRecommendedVersion().isLessRecommended();
                }
                return false;
            }
        };
        packageLessRecommended.setFieldUpdater(new FieldUpdater<RecommendedMcPackage, Boolean>() {
            @Override
            public void update(int index, RecommendedMcPackage object, Boolean value) {
                object.setPackageInfoModified(true);
                object.getRecommendedVersion().setLessRecommended(value);
                table.redrawRow(index);                
            }
           
        });

        // Component activity
        TextColumn<RecommendedMcPackage> packageActivity = new TextColumn<RecommendedMcPackage>() {
            @Override
            public String getValue(RecommendedMcPackage pack) {
                if (pack.getRecommendedVersion() != null) {
                    Set<String> activitiesSet = new HashSet<String>();
                    StringBuilder activities = new StringBuilder();
                    for (MCPackageActivities pa : pack.getRecommendedVersion().getActivities()) {
                        if (!pa.getActivityType().equalsIgnoreCase("EPR")) {
                            activitiesSet.add(pa.getActivityType() + pa.getActivityId());                            
                        }
                    }
                    for(String s: activitiesSet){
                        activities.append(s + " ");
                    }
                    return activities.toString();
                }
                return "";
            }
        };

        // Component notes
        Column<RecommendedMcPackage, String> packageNotes = new Column<RecommendedMcPackage, String>(new TextAreaInputCell()) {
            @Override
            public String getValue(RecommendedMcPackage pack) {
                try {
                    return pack.getRecommendedVersion().getNotes();
                } catch (Exception exp) {
                    return "";
                }

            }
        };
        packageNotes.setFieldUpdater(new FieldUpdater<RecommendedMcPackage, String>() {
            @Override
            public void update(int index, RecommendedMcPackage object, String value) {
                object.setPackageInfoModified(true);
                object.getRecommendedVersion().setNotes(value);
                table.redrawRow(index);
            }
        });

        // Update package
        Column<RecommendedMcPackage, String> updatePackage = new Column<RecommendedMcPackage, String>(new ButtonCell(ButtonType.INFO)) {
            @Override
            public String getValue(RecommendedMcPackage object) {
                String result = "Update";
                if (!object.isShowInTable()) {
                    result = "Add to table";
                    if (object.getRecommendedVersion() != null
                            && !object.getRecommendedVersion().getPackageVersion().equalsIgnoreCase("n\\a")) {
                        ((ButtonCell) this.getCell()).setEnabled(true);
                    } else {
                        ((ButtonCell) this.getCell()).setEnabled(false);
                    }
                    return result;
                } else {
                    ((ButtonCell) this.getCell()).setEnabled(object.isPackageInfoModified());
                    return result;
                }
            }
        };
        updatePackage.setFieldUpdater(new FieldUpdater<RecommendedMcPackage, String>() {
            @Override
            public void update(int index, RecommendedMcPackage object, String value) {
                object.setRowIndex(index);
                object.setPackageInfoModified(false);
                object.setRecommendedVersionCandidate(false);
                object.setShowInTable(true);
                packageListTable.redraw();
                getService().updateRecommendedVersion(object, updateRecommendedVersionCallback);

            }
        });

        table.addColumn(hidePackage, "Remove");
        table.addColumn(packageTier, "Tier");
        table.addColumn(packageGroup, "Group");
        table.addColumn(packageName, "Package Name");
        table.addColumn(packageVersion, "Recommended version");
        table.addColumn(packageAvailability, "Availability");
        table.addColumn(packageCustomers, "Customers");
        table.addColumn(packageReleaseDate, "Release date");
        table.addColumn(packageLessRecommended, "LR");
        table.addColumn(packageActivity, "Activity");
        table.addColumn(packageNotes, "Notes");
        table.addColumn(updatePackage, "Save changes");
    }

    public static GWTMCRepositoryManagerServiceAsync getService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        return GWT.create(GWTMCRepositoryManagerService.class);
    }

}
