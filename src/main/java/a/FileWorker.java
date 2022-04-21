package a;

import java.io.File;

public class FileWorker {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileWorker(String path) {
        this.path = path;
    }

    public String Open() {
        File file = new File(
                "input.json");

    }
}
