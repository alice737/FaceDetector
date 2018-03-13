package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Datastore {

    private String directoryPath;
    private List<File> files;

    private Datastore() {
        this.files = new ArrayList<>();
    }

    private static Datastore instance = new Datastore();

    public static Datastore getInstance() {
        return instance;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
