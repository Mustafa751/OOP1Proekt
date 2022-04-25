package a;

import com.cedarsoftware.util.io.JsonWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class FileWorker {
    private String path;
    private String content = null;
    private JSONArray ja = null;

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
        try {
            JSONArray array = new JSONArray(content);
            ja = array;
            System.out.println("The json is valid");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void Print() {
        JSONArray array = new JSONArray(content);
        ja = array;
        String niceFormattedJson = JsonWriter.formatJson(content);
        System.out.println(niceFormattedJson);
    }

    public void Search(String key) {
        boolean flag = false;
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            flag = checkKey(jo,key);
            if(flag)
                break;
        }
        if (!flag)
            System.out.println("No such key");
    }

    public void Delete(String key) {
        JSONArray ja1 = new JSONArray();
        boolean flag = false;
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            for (String key1 : jo.keySet()) {
                if (key.equals(key1)) {
                    flag = true;
                    jo.remove(key);
                    ja1.put(jo);
                    break;
                }
            }
        }
        if (!flag)
            System.out.println("No such key");
        else {
            ja = ja1;
            content = ja.toString();
        }
    }

    private static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private static boolean checkKey(JSONObject object, String searchedKey) {
        boolean exists = object.has(searchedKey);
        if(!exists) {
            Set<String> keys = object.keySet();
            for(String key : keys){
                if ( object.get(key) instanceof JSONObject ) {
                    exists = checkKey((JSONObject)object.get(key), searchedKey);
                }
            }
        }
        else {
            String niceFormattedJson = JsonWriter.formatJson(object.optString(searchedKey));
            System.out.println(niceFormattedJson);
        }
        return exists;
    }
}
