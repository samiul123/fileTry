package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Lotus Computer on 22-Apr-17.
 */
public class ListClass {
    public static Image folderCollapseImage = new Image(ClassLoader.getSystemResourceAsStream("folder_collapse.png"));
    public static Image folderExpandImage = new Image(ClassLoader.getSystemResourceAsStream("folderExpand.jpeg"));
    public static Image fileImage = new Image(ClassLoader.getSystemResourceAsStream("file.png"));


    private ImageView img;
    private Path path;
    private String name;

    public ListClass(){

    }
    public ListClass(ImageView img, Path path, String name){
        this.img = img;
        this.path = path;

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public ImageView getImg() {
        return img;
    }
    public void setImg(ImageView img) {
        this.img = img;
    }


    public ObservableList<ListClass> getListItems(File file){
        ObservableList<ListClass> list = FXCollections.observableArrayList();
        //File listCurrentDirectory = new File(fileName);
        File[] listCurrentFiles = file.listFiles();
        try{
            if(listCurrentFiles.length > 0){
                for(File subFile:listCurrentFiles){
                    if(file.isDirectory()){
                        list.add(new ListClass(new ImageView(folderCollapseImage),subFile.toPath(),subFile.getName()));
                    }else{
                        list.add(new ListClass(new ImageView(fileImage),subFile.toPath(),subFile.getName()));
                    }
                }
            }
        }catch(NullPointerException e){
            System.out.println("nullTable");
        }

        return list;
    }

}
