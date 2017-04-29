/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anritsu.mcrepositorymanager.client;

import static com.anritsu.mcrepositorymanager.client.FilterRSSForm.getService;
import com.anritsu.mcrepositorymanager.shared.Filter;
import com.anritsu.mcrepositorymanager.shared.MCBaselineAttributes;
import com.anritsu.mcrepositorymanager.shared.MCPackageActivities;
import com.anritsu.mcrepositorymanager.shared.McPackage;
import com.anritsu.mcrepositorymanager.shared.PackingStatus;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.gwtbootstrap3.client.shared.event.ModalHideEvent;
import org.gwtbootstrap3.client.shared.event.ModalHideHandler;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.PageHeader;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

/**
 *
 * @author ro100051
 */
public class FilterDBForm extends Composite {

    @UiField
    Select availabilities;

    @UiField
    Select customers;

    @UiField
    Select packagesName;

    @UiField
    Select activitiesECR;

    @UiField
    Select activitiesEPR;

    @UiField
    Select recommended;

    @UiField
    Select localDependencies;

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

    @UiField
    HorizontalPanel hPanelLoading;

    @UiField
    Modal downloadLinkModal;

    @UiField
    ModalBody downloadLinkModalBody;

    @UiField
    Button closeDownloadLinkModal;

    @UiField
    Button returnToBaselineSelection;

    @UiField
    PageHeader pageHeader;

    @UiField
    Button repositoryDownloadLink;

    @UiField
    Modal serviceDescriptionModal;

    @UiField
    HTML serviceDescriptionHtml;

    @UiField
    Button serviceDescription;

    private ArrayList<McPackage> packageList;
    private Filter f = new Filter();
    private String mcVersion;
    private Timer t;

    private ListDataProvider<McPackage> dataProvider = new ListDataProvider<>();

    private static FilterDBFormUiBinder uiBinder = GWT.create(FilterDBFormUiBinder.class);

    interface FilterDBFormUiBinder extends UiBinder<Widget, FilterDBForm> {
    }

    public FilterDBForm(String mcVersion) {
        this.mcVersion = mcVersion;
        initWidget(uiBinder.createAndBindUi(this));
        packageListTable.setVisible(false);
        hPanelLoading.setVisible(false);
        pageHeader.setText("MasterClaw " + mcVersion + " Repository Generator");

        dataProvider.addDataDisplay(packageListTable);

        f.setMcVersion(mcVersion);
        // Initiate PackageInfoParser with selected MC version
        getService().initiateParser(f, initiateParserCallback);

        filter.setEnabled(true);
        downloadRepositoryArchive.setVisible(false);
        repositoryDownloadLink.setVisible(false);
        q7admOutput.setHeight("200px");
        q7admOutput.setPlaceholder("Please paste 'yum list' output");

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

        //Building the table
        buildPackageListTable(packageListTable);

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
                hPanelLoading.setVisible(true);
                //generateRepository.setEnabled(false);
                downloadRepositoryArchive.setVisible(false);
                downloadRepositoryArchive.setActive(false);

                repositoryDownloadLink.setVisible(false);
                repositoryDownloadLink.setActive(false);
                packageListTable.setVisible(false);
                f.setRecommended((recommended.getSelectedValue().equalsIgnoreCase("recommended")) || (recommended.getSelectedValue().equalsIgnoreCase("latest validated")));
                f.setCustomer(customers.getSelectedValue());
                f.setAvailability(availabilities.getAllSelectedValues());
                f.setQ7admOutput(q7admOutput.getText());
                f.setMcComponent(packagesName.getAllSelectedValues());
                f.setRecommendedFilter(recommended.getSelectedValue());
                f.setLocalDependencies(localDependencies.getSelectedValue().equalsIgnoreCase("yes"));

                // set activities filter
                List<MCPackageActivities> activitiesList = new ArrayList<>();

                List<String> activitiesECRValues = activitiesECR.getAllSelectedValues();
                for (String string : activitiesECRValues) {
                    String[] activitySplit = string.split(":::");
                    MCPackageActivities mcpa = new MCPackageActivities();
                    mcpa.setActivityType(activitySplit[0]);
                    mcpa.setActivityId(activitySplit[1]);
                    mcpa.setActivityText(activitySplit[2]);
                    activitiesList.add(mcpa);
                }

                List<String> activitiesEPRValues = activitiesEPR.getAllSelectedValues();
                for (String string : activitiesEPRValues) {
                    String[] activitySplit = string.split(":::");
                    MCPackageActivities mcpa = new MCPackageActivities();
                    mcpa.setActivityType(activitySplit[0]);
                    mcpa.setActivityId(activitySplit[1]);
                    mcpa.setActivityText(activitySplit[2]);
                    activitiesList.add(mcpa);
                }
                f.setActivities(activitiesList);

                getService().getPackageList(f, getPackageListCallback);
            }
        });

        generateRepository.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                downloadRepositoryArchive.setVisible(false);
                downloadRepositoryArchive.setEnabled(false);

                repositoryDownloadLink.setVisible(false);
                repositoryDownloadLink.setEnabled(false);
                int downloadCount = 0;
                ArrayList<McPackage> packageList = new ArrayList<>();
                for (McPackage p : dataProvider.getList()) {
                    if (p.getDownloadLinks() != null && !p.getDownloadLinks().isEmpty()) {
                        packageList.add(p);
                    }
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

        recommended.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                switch (recommended.getSelectedValue()) {
                    case "recommended":
                        availabilities.deselectAll();
                        availabilities.setEnabled(false);
                        break;
//                    case "latest validated":
//                        availabilities.deselectAll();
//                        availabilities.selectValues("FCA", "GCA");
//                        break;
                    case "latest":
                        availabilities.deselectAll();
                        availabilities.setEnabled(true);
                        break;
                }
            }
        });

        customers.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (!customers.getSelectedValue().equals("All")) {
                    availabilities.selectValues("SCR");
                } else if (customers.getSelectedValue().equals("All")) {
                    availabilities.deselectAll();

                }
            }
        });

        downloadRepositoryArchive.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open("/DownloadRepoArchive?archiveName=" + downloadRepositoryArchive.getTitle(), "_parent", "location=no");
            }
        });

        repositoryDownloadLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                PopupPanel popupContext = new PopupPanel(true);
                popupContext.setPopupPosition(event.getNativeEvent().getClientX() + Window.getScrollLeft(), event.getNativeEvent().getClientY() + Window.getScrollTop());
                String link = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/DownloadRepoArchive?archiveName=" + downloadRepositoryArchive.getTitle();
                Anchor anchor = new Anchor(link, link);
                popupContext.add(anchor);
                popupContext.show();

            }

        });

    }

    // Initiate parser callback
    AsyncCallback<Boolean> initiateParserCallback = new AsyncCallback<Boolean>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(Boolean result) {
            if (result) {
                getService().getMCBaselineAttributes(getMCBaselinesAttributesCallback);
            }
        }
    };

    // Get MC baseline attributes
    AsyncCallback<MCBaselineAttributes> getMCBaselinesAttributesCallback = new AsyncCallback<MCBaselineAttributes>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(MCBaselineAttributes result) {

            // Populate recommended
            recommended.clear();
            ArrayList<String> recommendedList = new ArrayList<>();
            recommendedList.add("recommended");
            //recommendedList.add("latest validated");
            recommendedList.add("latest");
            for (String s : recommendedList) {
                Option o = new Option();
                o.setValue(s);
                o.setName(s);
                o.setText(s);
                recommended.add(o);
            }
            recommended.setEnabled(true);
            recommended.setSelectedTextFormat("recommended");
            recommended.refresh();

            // Populate availabilities
            availabilities.clear();
            TreeSet<String> sortedAvailabilities = new TreeSet<>(result.getAvailabilities());
            for (String s : result.getAvailabilities()) {
                Option o = new Option();
                o.setName(s);
                o.setText(s);
                availabilities.add(o);
            }
            //availabilities.selectValues("FCA", "GCA");
            availabilities.refresh();
            availabilities.setEnabled(true);

            // Populate customers
            customers.clear();
            TreeSet<String> sortedCustomers = new TreeSet<String>(result.getCustomers());
            for (String s : sortedCustomers) {
                Option o = new Option();
                o.setValue(s);
                o.setName(s);
                o.setText(s);
                customers.add(o);
            };
            customers.refresh();
            customers.setEnabled(true);

            // Populate package name
            packagesName.clear();
            TreeSet<String> sortedPackageNames = new TreeSet<String>(result.getPackageNames());
            for (String s : sortedPackageNames) {
                Option o = new Option();
                o.setValue(s);
                o.setText(s);
                o.setName(s);
                packagesName.add(o);
            }
            packagesName.refresh();
            packagesName.setEnabled(true);

            // populate activity section
            activitiesECR.clear();
            activitiesEPR.clear();
            for (MCPackageActivities mcpa : result.getPackageActivities()) {
                Option o = new Option();
                String value = mcpa.getActivityType() + ":::" + mcpa.getActivityId() + ":::" + mcpa.getActivityText();
                o.setValue(value);
                o.setText(mcpa.getActivityType() + " " + mcpa.getActivityId());
                o.setSubtext(mcpa.getActivityText());
                if (mcpa.getActivityType().equalsIgnoreCase("epr")) {
                    activitiesEPR.add(o);
                } else {
                    activitiesECR.add(o);
                }
            }
            activitiesECR.refresh();
            activitiesECR.setEnabled(true);
            activitiesEPR.refresh();
            activitiesEPR.setEnabled(true);

            // populate localDependencies
            localDependencies.clear();
            Option o1 = new Option();
            o1.setValue("yes");
            o1.setText("Local dependencies");
            o1.setName("Local dependencies");

            Option o2 = new Option();
            o2.setValue("no");
            o2.setText("Remote dependencies");
            o2.setName("Remote dependencies");

            localDependencies.add(o1);
            localDependencies.add(o2);
            localDependencies.refresh();
        }
    };

    // Get the list of MC packages.
    AsyncCallback<ArrayList<McPackage>> getPackageListCallback = new AsyncCallback<ArrayList<McPackage>>() {
        @Override
        public void onFailure(Throwable caught) {
            Notify.notify("Package list could not be retrived!\n" + caught.getMessage());
        }

        @Override
        public void onSuccess(ArrayList<McPackage> result) {
            hPanelLoading.setVisible(false);
            packageListTable.setVisible(true);
            Notify.notify(result.size() + " packages retrived!");
            packageList = result;
            populatePackageListTable(new HashSet(packageList));
            generateRepository.setEnabled(true);
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

    public void populatePackageListTable(HashSet<McPackage> packageList) {
        ArrayList<McPackage> sortedPackageList = new ArrayList(packageList);
        Collections.sort(sortedPackageList, new Comparator<McPackage>() {
            @Override
            public int compare(McPackage t1, McPackage t2) {
                return t1.getName().compareTo(t2.getName());
            }
        });
        dataProvider.getList().clear();
        List<McPackage> list = dataProvider.getList();

        for (McPackage p : packageList) {
//            if (!(f.getQ7admOutput().equals("")) && !p.isDependency()) {
//                p.setAddToRepository(p.isMcPackageSuitableForDownload());            
//            }
            list.add(p);

        }

        // Changing row style when packageversion is greater than q7adm output version        
        packageListTable.setRowStyles(new RowStyles<McPackage>() {
            @Override
            public String getStyleNames(McPackage p, int rowIndex) {
                if (p.getDownloadLinks() == null || p.getDownloadLinks().isEmpty()) {
                    NotifySettings settings = NotifySettings.newSettings();
                    settings.setType(NotifyType.DANGER);
                    settings.setDelay(0);
                    Notify.notify("Please note that not all the dependencies were solved.", settings);
                    //generateRepository.setEnabled(false);
                    return "danger";
                }
                if (p.isMcPackageSuitableForDownload()) {
                    return "info";
                }
                if (p.isDependency()) {
                    return "warning";
                }
                return "";
            }
        });
    }

    public void buildPackageListTable(final CellTable table) {

        table.setPageSize(200);

        // Attach column sort handler
        ListHandler<McPackage> sortHandler = new ListHandler<McPackage>(dataProvider.getList());
        table.addColumnSortHandler(sortHandler);

        Column<McPackage, String> packageNameColumn = new Column<McPackage, String>(new ButtonCell(ButtonType.LINK)) {
            @Override
            public String getValue(McPackage object) {
                return object.getName();
            }
        };
        packageNameColumn.setFieldUpdater(new FieldUpdater<McPackage, String>() {
            @Override
            public void update(int index, McPackage object, String value) {
                Set<String> dependencies = new HashSet();
                for (McPackage p : object.getDependencies()) {
                    dependencies.add(p.getName() + "-" + p.getPackageVersion());
                }
                Set<String> dependencyFor = new HashSet<>();
                for (McPackage p : object.getDependencyFor()) {
                    dependencyFor.add(p.getName() + "-" + p.getPackageVersion());
                }
                Notify.notify("Dependencies: " + Arrays.toString(dependencies.toArray()) + "\nDependency for: " + Arrays.toString(dependencyFor.toArray()));
            }
        });
        packageNameColumn.setSortable(true);
        packageNameColumn.setDefaultSortAscending(true);
        sortHandler.setComparator(packageNameColumn, new Comparator<McPackage>() {
            @Override
            public int compare(McPackage t1, McPackage t2) {
                return t1.getName().compareTo(t2.getName());
            }
        });

        Column<McPackage, String> packageVersionColumn = new Column<McPackage, String>(
                new ButtonCell(ButtonType.LINK)) {
            @Override
            public String getValue(McPackage object) {
                return object.getPackageVersion();
            }
        };

        packageVersionColumn.setFieldUpdater(new FieldUpdater<McPackage, String>() {
            @Override
            public void update(int index, McPackage object, String value) {
                HTML html = new HTML();
                SafeHtmlBuilder sHtmlBuilder = new SafeHtmlBuilder();

                for (String stringLink : object.getDownloadLinks()) {
                    sHtmlBuilder.appendHtmlConstant("<a href='" + stringLink + "'>");
                    sHtmlBuilder.appendEscaped(stringLink);
                    sHtmlBuilder.appendHtmlConstant("</a><br />");
                }
                html.setHTML(sHtmlBuilder.toSafeHtml());
                downloadLinkModalBody.clear();
                downloadLinkModalBody.add(html);
                downloadLinkModal.show();

            }
        });

        Column<McPackage, String> packageQ7admOutputVersionColumn = new Column<McPackage, String>(new ButtonCell(ButtonType.LINK)) {
            @Override
            public String getValue(McPackage object) {
                return object.getQ7admOutputVersion();
            }
        };

        TextColumn<McPackage> packageAvailabilityColumn = new TextColumn<McPackage>() {
            @Override
            public String getValue(McPackage pack) {
                return pack.getAvailability();
            }
        };

        TextColumn<McPackage> packageReleaseDateColumn = new TextColumn<McPackage>() {
            @Override
            public String getValue(McPackage pack) {
                StringBuilder dateFormated = new StringBuilder();
                if (pack.getReleaseDate() != null) {
                    String[] releaseDateArray = pack.getReleaseDate().toString().split(" ");
                    dateFormated.append(releaseDateArray[2] + "-" + releaseDateArray[1] + "-" + releaseDateArray[5]);
                    return dateFormated.toString();
                }
                return "";
            }
        };

        Column<McPackage, String> selectedForDownloadColumn = new Column<McPackage, String>(new ButtonCell()) {
            @Override
            public String getValue(McPackage object) {
                ((ButtonCell) this.getCell()).setEnabled(false);
                if (object.getDownloadLinks().isEmpty()) {
                    return "Missing from DB";
                }
                if (object.isMatchFilter()) {
                    ((ButtonCell) this.getCell()).setEnabled(true);
                }
                if (object.isDependency()) {
                    ((ButtonCell) this.getCell()).setEnabled(false);
                }
                return (object.isAddToRepository()) ? "remove from repository" : "add to repository";
            }
        };

        selectedForDownloadColumn.setFieldUpdater(new FieldUpdater<McPackage, String>() {

            @Override
            public void update(int index, McPackage object, String value) {
                downloadRepositoryArchive.setVisible(false);
                downloadRepositoryArchive.setActive(false);

                repositoryDownloadLink.setVisible(false);
                repositoryDownloadLink.setActive(false);
                object.setAddToRepository(!object.isAddToRepository());

                // Dealing with dependencies
                HashSet<McPackage> pList = new HashSet<>();
                for (McPackage p : dataProvider.getList()) {
                    if (p.isAddToRepository() && p.isMatchFilter()) {
                        p.setDependencySolved(false);
                        pList.add(p);
                    }
                }
                getService().solveDependencies(pList, solveDependenciesCallback);
                Notify.notify("Package " + object.getName() + " " + value + " : " + object.isAddToRepository());
            }

        });

        table.addColumn(packageNameColumn, "Package Name");
        table.addColumn(packageQ7admOutputVersionColumn, "Installed version");
        table.addColumn(packageVersionColumn, "Package Version");
        table.addColumn(packageAvailabilityColumn, "Availability");
        table.addColumn(packageReleaseDateColumn, "Release Date");
        table.addColumn(selectedForDownloadColumn, "Add to repository");
    }

    // Repopulate table after removing a package which mathced the filter
    AsyncCallback<HashSet<McPackage>> solveDependenciesCallback = new AsyncCallback<HashSet<McPackage>>() {
        @Override
        public void onFailure(Throwable caught) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onSuccess(HashSet<McPackage> result) {

            HashSet<McPackage> removed = new HashSet<>();
            for (McPackage p : dataProvider.getList()) {
                if (p.isMatchFilter()) {
                    removed.add(p);
                }
            }
            result.addAll(removed);
            populatePackageListTable(result);

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

            repositoryDownloadLink.setVisible(true);
            repositoryDownloadLink.setEnabled(true);
        }
    };

}
