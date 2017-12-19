package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.rmi.Naming;
import java.util.ArrayList;

public class Main extends Application {
    public static Image folderCollapseImage = new Image(ClassLoader.getSystemResourceAsStream("folder_collapse.png"));
    public static Image folderExpandImage = new Image(ClassLoader.getSystemResourceAsStream("folderExpand.jpeg"));
    public static Image fileImage = new Image(ClassLoader.getSystemResourceAsStream("file.png"));

    TreeView<File> treeView;
    ComboBox<String> changeViewCombo;
    Button up = new Button();
    Button down = new Button();

    public TreeView buildFileSystemBrowser() {
        String hostName = "computer";
        try{
            hostName = InetAddress.getLocalHost().getHostName();
            //treeViewEjetaClickedAse = hostName;
        }catch (UnknownHostException x) {
        }
        TreeItem<File> root = new TreeItem<>();
        root.setValue(new File(hostName));
        FileTreeItem treeItem = new FileTreeItem();
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for(Path name:rootDirectories){
            TreeItem item = treeItem.createNode(name.toFile());
            root.getChildren().add(item);
        }
        root.setExpanded(true);
        treeView = new TreeView<File>(root);
        return  treeView;
    }
    HBox upperhBox;
    HBox lowerHBox;
    VBox treeBox;
    VBox tableBox;
    VBox listBox;
    VBox motherBox;
    ListView<ListClass> listView = new ListView<>();
    //TilePane tilePane = new TilePane();
    //VBox tileObj = new VBox();
    TableView<TableClass> mainClassTableView = new TableView<>();
    TableClass table = new TableClass();
    ListClass listObj = new ListClass();
    File uri;
    Label addressText = new Label("Current address");
    TextArea address = new TextArea();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        changeViewCombo = new ComboBox<>();
        //GridPane.setConstraints(changeViewCombo,0,0);
        changeViewCombo.setPromptText("SwitchView");
        changeViewCombo.getItems().addAll("TableView","ListView");
        changeViewCombo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (changeViewCombo.getValue().equals("ListView")){
                    //flag = 1;
                    System.out.println(changeViewCombo.getValue());
                    tableBox.getChildren().clear();
                    tableBox.setMinWidth(0);
                    listBox.setMinWidth(500);
                    showListView();

                }else{
                    listBox.getChildren().clear();
                    listBox.setMinWidth(0);
                    showTableView();
                }
            }
        });


        TableColumn<TableClass,ImageView> iconCol = new TableColumn<>("Icon");
        iconCol.setMinWidth(100);
        iconCol.setCellValueFactory(new PropertyValueFactory<>("icon"));

        TableColumn<TableClass,String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(120);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TableClass,String> sizeCol = new TableColumn<>("Size");
        sizeCol.setMinWidth(100);
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<TableClass,String> dateCol = new TableColumn<>("Date Modified");
        dateCol.setMinWidth(140);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        mainClassTableView.getColumns().addAll(iconCol,nameCol,sizeCol,dateCol);

        treeView = buildFileSystemBrowser();

        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> mainClassTableView.setItems(table.generateTableView(new File(newValue.getValue().toURI()))));
        //treeView.getSelectionModel()
          //      .selectedItemProperty()
            //    .addListener((observable, oldValue, newValue) -> System.out.println(new File(newValue.getValue().toURI())));
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> address.setText(newValue.getValue().getPath()));

        //treeView.getSelectionModel()
          //      .selectedItemProperty()
            //    .addListener((observable, oldValue, newValue) -> listView.setItems(listObj.getListItems(new File(address.getText()))));
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<File>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue, TreeItem<File> newValue) {
                        listView.setItems(null);
                        listView.setItems(listObj.getListItems(new File(address.getText())));
                    }
                });

        /*treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<File>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue, TreeItem<File> newValue) {
                        ObservableList<ListClass> list = FXCollections.observableArrayList();
                        list = listObj.getListItems(new File(address.getText()));
                        for(ListClass listClass:list){
                            Label label = new Label(listClass.getName());
                            ImageView imageView = listClass.getImg();
                            tileObj.getChildren().addAll(imageView,label);
                            System.out.println(listClass.getName());
                        }
                    }
                });*/
        mainClassTableView.getSelectionModel()
               .selectedItemProperty()
               .addListener((observable, oldValue, newValue) -> mainClassTableView = newTableView(newValue.getName()));
        mainClassTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> address.setText(newValue.getName().toString()));
        //listView.setOrientation(O);
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> listView = newListView(newValue.getPath()));
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> address.setText(newValue.getPath().toString()));

        treeBox = new VBox();
        treeBox.setPadding(new Insets(10,10,10,10));
        treeBox.setSpacing(10);

        treeBox.getChildren().add(treeView);
        treeBox.autosize();

        //treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> System.out.println( oldValue.getValue()));

        upperhBox = new HBox();
        upperhBox.autosize();
        address.setMaxHeight(15);
        address.setMaxWidth(300);
        up.setGraphic(new ImageView(new Image("up.jpg")));
        down.setGraphic(new ImageView(new Image("down.jpeg")));
        up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(changeViewCombo.getValue().equals("TableView")){
                    mainClassTableView = newTableView(new File(address.getText()).getParentFile().toPath());
                }
                else if(changeViewCombo.getValue().equals("ListView")){
                    listView = newListView(new File(address.getText()).getParentFile().toPath());
                }
                address.setText(new File(address.getText()).getParentFile().getPath());
                //listView = newListView(new File(address.getText()).getParentFile().toPath());

            }
        });

        //up.setOnAction(event -> address.setText(new File(address.getText()).getParentFile().getPath()));
        //up.setOnAction(event -> listView = newListView(new File(address.getText()).getParentFile().toPath()));


        upperhBox.getChildren().addAll(changeViewCombo,addressText,address,up,down);

        lowerHBox = new HBox();
        lowerHBox.autosize();
        tableBox = new VBox();
        tableBox.autosize();
        tableBox.setPadding(new Insets(10,10,10,10));
        tableBox.setSpacing(10);
        tableBox.getChildren().add(mainClassTableView);

        listBox = new VBox();
        listBox.autosize();
        listView.setCellFactory(new Callback<ListView<ListClass>, ListCell<ListClass>>() {
            @Override
            public ListCell<ListClass> call(ListView<ListClass> param) {
                ListCell<ListClass> cell = new ListCell<ListClass>(){
                    @Override
                    protected void updateItem(ListClass myobject,boolean bln){
                        super.updateItem(myobject, bln);
                        if (myobject != null){
                            setGraphic(myobject.getImg());
                            setText(myobject.getName());
                        }
                    }
                };
                return  cell;
            }
        });

        //listBox.getChildren().add(listView);

        lowerHBox.getChildren().addAll(treeBox,tableBox,listBox);
        motherBox = new VBox();
        motherBox.autosize();
        motherBox.getChildren().addAll(upperhBox,lowerHBox);
        primaryStage.setTitle("File explorer JavaFX");
        primaryStage.setScene(new Scene(motherBox, 700, 500));
        primaryStage.show();
    }
    public int countFilesAndFolders(File file){
        int count = 0;
        File[] subFiles = file.listFiles();
        for(File listFiles:subFiles){
            count++;
        }
        return count;
    }

    public TableView<TableClass> newTableView(Path uri){
        tableBox.getChildren().clear();
        System.out.println(uri.toString());
        TableView<TableClass> newTable = new TableView<>();
        TableColumn<TableClass,ImageView> iconCol = new TableColumn<>("Icon");
        iconCol.setMinWidth(100);
        iconCol.setCellValueFactory(new PropertyValueFactory<>("icon"));

        TableColumn<TableClass,String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(120);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TableClass,String> sizeCol = new TableColumn<>("Size");
        sizeCol.setMinWidth(100);
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<TableClass,String> dateCol = new TableColumn<>("Date Modified");
        dateCol.setMinWidth(140);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        newTable.getColumns().addAll(iconCol,nameCol,sizeCol,dateCol);
        newTable.setItems(table.generateTableView(uri.toFile()));
        tableBox.getChildren().add(newTable);
        newTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> mainClassTableView = newTableView(newValue.getName()));
        newTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> address.setText(newValue.getName().toString()));

        return  newTable;
    }
    public ListView<ListClass> newListView(Path uri){
        listBox.getChildren().clear();
        ListView<ListClass> newList = new ListView<>();
        newList.setCellFactory(new Callback<ListView<ListClass>, ListCell<ListClass>>() {
            @Override
            public ListCell<ListClass> call(ListView<ListClass> param) {
                ListCell<ListClass> cell = new ListCell<ListClass>(){
                    @Override
                    protected void updateItem(ListClass myobject,boolean bln){
                        super.updateItem(myobject, bln);
                        if (myobject != null){
                            setGraphic(myobject.getImg());
                            setText(myobject.getName());
                        }
                    }
                };
                return  cell;
            }
        });

        newList.setItems(listObj.getListItems(uri.toFile()));
        listBox.getChildren().add(newList);
        newList.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> listView = newListView(newValue.getPath()));
        newList.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> address.setText(newValue.getPath().toString()));

        return newList;
    }
    public void showTableView(){

        //tableImage.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue.getName()));

        tableBox.setPadding(new Insets(10,10,10,10));
        tableBox.setSpacing(10);
        tableBox.getChildren().addAll(mainClassTableView);

    }
    public void showListView(){

        listBox.setPadding(new Insets(35,10,15,10));
        listBox.setSpacing(10);
        listBox.getChildren().add(listView);
    }
    /*public void showTileView(){
        tilePane.autosize();
        tilePane.getChildren().add(tileObj);

    }*/
    public static void main(String[] args) {
        launch(args);
    }
}
