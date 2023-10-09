package com.pepper.homeWorkDatabaseFx;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HomeAppController extends Application
{
    private static Scene scene;
    
    
    private IncomeRepository incomeRepo;
    
    private PartnerRepository partnerRepo;
    
    private List<Income> top25income;
    private List<Income> unpaid;
    
    
    @FXML
    ComboBox<String> comboBox;
    @FXML
    private Button button;
    @FXML
    private TableView table;
    private ObservableList<Income> incomeList;
    private ObservableList<Partner> partnerList;
    
    @Override
public void start(Stage stage) throws Exception {
    HBox hbox = new HBox();
    VBox root = new VBox();
    Scene scene = new Scene(root, 1000, 800);
    stage.setScene(scene);
    stage.show();

    comboBox = new ComboBox<>();
    comboBox.getItems().addAll("Legutóbbi 25 bevétel", "Kintlévőségek", "Legutóbbi bevétel részletei", "Partnerek listája");
    comboBox.getSelectionModel().select(0);

    button = new Button("Betöltése");
    button.setOnAction(event -> loadQuery());

    table = new TableView<>();
    table.prefHeightProperty().bind(root.heightProperty());

    hbox.getChildren().addAll(comboBox, button);
    root.getChildren().addAll(hbox, table);

    incomeList = FXCollections.observableArrayList();
    partnerList = FXCollections.observableArrayList();
    

    context = new SpringApplicationBuilder(HomeWorkDatabaseFxApp.class).run();
    incomeRepo = context.getBean(IncomeRepository.class);
    partnerRepo = context.getBean(PartnerRepository.class);
    
    
    
}

    @FXML
    public void loadQuery()
    {
        switch(comboBox.getSelectionModel().getSelectedIndex())
        {
            case 0 -> loadLatest25Incomes();
            case 1 -> loadUnpaid();
            case 2 -> getLatestIncomeWithPartner();
            case 3 -> getPartnerTable();
        }
        
    }
    
    // QUERIES
    private void loadLatest25Incomes() 
    {        
        clearTable();        
        createIncomeTable();        
        
        List<Income> incomeList = incomeRepo.findTop25ByOrderByCreatedDesc();
        ObservableList<Income> observableIncomeList = FXCollections.observableArrayList(incomeList);
        table.setItems(observableIncomeList);
    }

    
    private void loadUnpaid() // KINTLÉVŐSÉGEK
    {        
        clearTable();
        createIncomeTable();
        
        unpaid = incomeRepo.findByApprovedIsNull();
        ObservableList<Income> unpaidObs = FXCollections.observableArrayList(unpaid);
        table.setItems(unpaidObs);
    }    
    public void getLatestIncomeWithPartner() //LEGUTÓBBI INCOME
    {
        clearTable();
        
        
        Income income = incomeRepo.findLatestIncomeWithPartnerDetails();
        ObservableList<Income> latestIn = FXCollections.observableArrayList(income);
        
        ObservableList<IncomeJoinPartner> join = FXCollections.observableArrayList();
        
        
        int partnerId = income.getPartner(); // Partner azonosító kinyerése az Income objektumból        
        Optional<Partner> latestPartnerOptional = partnerRepo.findById(partnerId); // Partner lekérdezése az azonosító alapján
 
        Partner latestPartner = latestPartnerOptional.orElse(null);
        if(latestPartner == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hibás azonosító");
            alert.setHeaderText("Nem található partner");
            alert.showAndWait();
        } else {
            createIncomeWithPartnerTable();
            Partner partner = latestPartnerOptional.get();
            
            IncomeJoinPartner ijp = new IncomeJoinPartner(income.getId(), income.getPartnerId(), income.getAmount(), income.getProject(), income.getCreated(), income.getApproved(), partner.getName(), partner.getContact());
            join.addAll(ijp);
            
            table.setItems(join);
            
        }
    }
    
    public void getPartnerTable() // PARTNER LISTA PROJEKTEKKEL
    {
        clearTable();
        createPartnerTable();
        
        ObservableList<IncomeJoinPartner> partnerPrCount = FXCollections.observableArrayList();
        
        List<Object[]> partnerList = partnerRepo.findPartnersWithProjectCounts();
        for(Object[] row: partnerList){
            Partner partner = (Partner) row[0];
            Long projectCount = (Long) row[1];
            IncomeJoinPartner partnerProjCount = new IncomeJoinPartner(partner.getName(), partner.getContact(), projectCount);
            partnerPrCount.add(partnerProjCount);
            
        }
        table.setItems(partnerPrCount);
    }
       
    @Override
    public void init() throws Exception 
    {
        super.init();
        context = new SpringApplicationBuilder(HomeWorkDatabaseFxApp.class).run();
    }
    
    private static ConfigurableApplicationContext context;
    public ConfigurableApplicationContext getContext()
    {
        return  context;
    }

    private void createIncomeTable() 
    {
        // Create and configure the table columns
        TableColumn<Income, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<Income, Integer> partnerCol = new TableColumn<>("Partner's ID");
        partnerCol.setCellValueFactory(new PropertyValueFactory<>("partner"));
        partnerCol.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<Income, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<Income, String> projectColumn = new TableColumn<>("Project");
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("project"));
        projectColumn.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<Income, LocalDateTime> createdColumn = new TableColumn<>("Created");
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
        createdColumn.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<Income, LocalDateTime> approvedColumn = new TableColumn<>("Approved");
        approvedColumn.setCellValueFactory(new PropertyValueFactory<>("approved"));
        approvedColumn.setMinWidth(Region.USE_PREF_SIZE);
        
        table.getColumns().addAll(idCol,partnerCol, amountCol, projectColumn, createdColumn, approvedColumn);
        
    
    }

    private void createPartnerTable() 
    {
        TableColumn<IncomeJoinPartner, String> nameCol = new TableColumn<>("Partner name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<IncomeJoinPartner, String> contactCol = new TableColumn<>("Partner contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactCol.setMinWidth(Region.USE_PREF_SIZE);
        
        TableColumn<IncomeJoinPartner, Long> projectCountCol = new TableColumn<>("Project Count");
        projectCountCol.setCellValueFactory(new PropertyValueFactory<>("projectCount"));
        projectCountCol.setMinWidth(Region.USE_PREF_SIZE);
        
        table.getColumns().addAll(nameCol, contactCol, projectCountCol);
    }
    
    private void createIncomeWithPartnerTable()
    {
        TableColumn<IncomeJoinPartner, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("incomeId"));
        idCol.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<IncomeJoinPartner, Integer> partnerCol = new TableColumn<>("Partner's ID");
        partnerCol.setCellValueFactory(new PropertyValueFactory<>("partnerIdIn"));
        partnerCol.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<IncomeJoinPartner, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<IncomeJoinPartner, String> projectColumn = new TableColumn<>("Project");
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("project"));
        projectColumn.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<IncomeJoinPartner, LocalDateTime> createdColumn = new TableColumn<>("Created");
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
        createdColumn.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<IncomeJoinPartner, LocalDateTime> approvedColumn = new TableColumn<>("Approved");
        approvedColumn.setCellValueFactory(new PropertyValueFactory<>("approved"));
        approvedColumn.setMinWidth(Region.USE_PREF_SIZE);
        
        TableColumn<IncomeJoinPartner, String> nameCol = new TableColumn<>("Partner name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(Region.USE_PREF_SIZE);

        TableColumn<IncomeJoinPartner, String> contactCol = new TableColumn<>("Partner contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactCol.setMinWidth(Region.USE_PREF_SIZE);
        
        table.getColumns().addAll(idCol,partnerCol, amountCol, projectColumn, createdColumn, approvedColumn,
                                  nameCol, contactCol);
    }
    
    public void clearTable() {
    table.getColumns().clear();
    table.setItems(null);
}

}
