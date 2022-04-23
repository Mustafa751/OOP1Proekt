package a;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileWorker {
    private String path;
    private String content = null;
    private JSONObject jo = null;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileWorker(String path) {
        this.path = path;
    }

    public void Open() {
        try {
            content = readFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Validate() {
        try{
            JSONObject jo = new JSONObject(content);
            System.out.println("The json is valid");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void Print(){
        jo = new JSONObject(content);
        System.out.println(content.toString());
    }

    public void Search(String key){
        boolean flag = false;
        for (String key1: jo.keySet()){
            if(key.equals(key1))
            {
                flag = true;
                System.out.println(jo.get(key));
            }
        }
        if(!flag)
            System.out.println("No such key");
    }

    public void Delete(String key){
        boolean flag = false;
        for (String key1: jo.keySet()){
            if(key.equals(key1))
            {
                flag = true;
                jo.remove(key);
                content = jo.toString();
                break;
            }
        }
        if(!flag)
            System.out.println("No such key");
    }

    private static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }
    private JSONObject remove(String key) {
        jo.remove(key);
        return this.jo;
    }
}
