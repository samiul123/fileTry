package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

/**
 * Created by Samiul Mushfik on 27-Apr-17.
 */
public class TableClass {
    public static Image folderCollapseImage = new Image(ClassLoader.getSystemResourceAsStream("folder_collapse.png"));
    public static Image folderExpandImage = new Image(ClassLoader.getSystemResourceAsStream("folderExpand.jpeg"));
    public static Image fileImage = new Image(ClassLoader.getSystemResourceAsStream("file.png"));


    private ImageView icon;
    private Path name;
    private String fileName;
    private  String size;
    private String date;
    public TableClass(){

    }

    public TableClass(ImageView icon, Path name, String fileName,String size, String date){
        this.icon = icon;
        this.name = name;
        this.fileName = fileName;
        this.size = size;
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Path getName() {
        return name;
    }

    public void setName(Path name) {
        this.name = name;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public ObservableList<TableClass> generateTableView(File file){
        //TableView<TableClass> tableImage = new TableView<>();

        ObservableList<TableClass> tableList = FXCollections.observableArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy hh:mm aaa");

        File[] files = file.listFiles();
        /*for(File fileObj:files){
            System.out.println(fileObj.getName());
        }*/
        if(files == null){
            System.out.println("File list not found");
            return null;
        }
        try{
            if(files.length > 0){
                for(File subFile:files){
                    if(subFile.isDirectory()){
                        tableList.add(new TableClass(new ImageView(folderCollapseImage),subFile.toPath(),subFile.getName(),null,sdf.format(subFile.lastModified())));
                    }else{
                        tableList.add(new TableClass(new ImageView(fileImage),subFile.toPath(),subFile.getName(), bytesIntoHumanReadable(subFile.length()),sdf.format(subFile.lastModified())));
                    }
                }
            }
        }catch(NullPointerException e){
            System.out.println("nullTable");
        }
        //tableImage.setItems(tableList);
        return tableList;
    }
    private String bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }
    }

}
