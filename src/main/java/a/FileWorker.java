package a;

import com.cedarsoftware.util.io.JsonWriter;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

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

    public void Delete() throws Exception {
        System.out.println("Type the path to the key");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        String separator = "\\";
        String[] keys = path.split(Pattern.quote(separator));
        JSONArray ja1 = new JSONArray();
        JSONObject jo1 = new JSONObject();
        for (int i = 0; i < ja.length(); i++) {
            jo1 = ja.getJSONObject(i);
            JSONObject jo2 = ja.getJSONObject(i);
            for (int j = 0; j < keys.length; j++) {
                if (j != keys.length - 1) {
                    try {
                        jo1 = jo1.getJSONObject(keys[j]);
                    } catch (Exception ex) {
                        ja1.put(jo2);
                        break;
                    }
                } else {
                    if (jo1.has(keys[j])) {
                        deleteJson(jo2, keys[j]);
                    }
                    ja1.put(jo2);
                }
            }
        }
        ja = ja1;
        content = ja.toString();
    }

    public void Set() throws Exception {
        System.out.println("Type the path to the key");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        System.out.println("Enter the string for object you want to replace");
        int k = 0;
        String jsonValue = scanner.nextLine();
        JSONObject jb = null;
        try {
            jb = new JSONObject(jsonValue);
        }
        catch(Exception ex)
        {
            System.out.println("The entered string isn't valid json");
            k++;
        }
        String separator = "\\";
        String[] keys = path.split(Pattern.quote(separator));
        JSONArray ja1 = new JSONArray();
        JSONObject jo1 = new JSONObject();
        if(k == 0) {
            for (int i = 0; i < ja.length(); i++) {
                jo1 = ja.getJSONObject(i);
                JSONObject jo2 = ja.getJSONObject(i);
                for (int j = 0; j < keys.length; j++) {
                    if (j != keys.length - 1) {
                        try {
                            jo1 = jo1.getJSONObject(keys[j]);
                        } catch (Exception ex) {
                            ja1.put(jo2);
                            break;
                        }
                    } else {
                        if (jo1.has(keys[j])) {
                            updateJson(jo2, keys[j], jb.toString());
                        }
                        ja1.put(jo2);
                    }
                }
            }
            ja = ja1;
            content = ja.toString();
        }
    }

    public void Create() {
        System.out.println("Type the path to the key");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        int k = 0;
        System.out.println("Enter the string for object you want to create");
        String jsonValue = scanner.nextLine();
        JSONObject jb = null;
        try {
            jb = new JSONObject(jsonValue);
        }
        catch(Exception ex)
        {
            System.out.println("The entered string isn't valid json");
            k++;
        }
        String separator = "\\";
        String[] keys = path.split(Pattern.quote(separator));
        JSONArray ja1 = new JSONArray();
        JSONObject jo1 = new JSONObject();
        boolean checkIfKeyExists = false;
        for(int i=0;i<ja.length();i++)
        {
            boolean checkIfKeyExists1 = checkKey(ja.getJSONObject(i),keys[0]);
                    if(checkIfKeyExists1)
                        checkIfKeyExists = true;
        }
        if(checkIfKeyExists && k == 0) {
            for (int i = 0; i < ja.length(); i++) {
                jo1 = ja.getJSONObject(i);
                JSONObject jo2 = ja.getJSONObject(i);
                for (int j = 0; j < keys.length; j++) {
                    if (j != keys.length - 1) {
                        try {
                            jo1 = jo1.getJSONObject(keys[j]);
                        } catch (Exception ex) {
                            ja1.put(jo2);
                            break;
                        }
                    } else {
                        if (jo1.has(keys[j])) {
                            for (int z = 0; z < keys.length - 1; z++) {
                                jo2 = jo2.getJSONObject(keys[z]);
                            }
                            jo2.put(keys[j],new JSONObject(jsonValue));
                        }
                        ja1.put(jo2);
                    }
                }


         /*   for(int j=0;j<keys.length;j++)
            {
                if(jo1.has(keys[j]) && j < keys.length-1)
                {
                    jo1 = jo1.getJSONObject(keys[j]);
                }
                else if(jo1.has(keys[j]))
                {
                    jo1.remove(keys[j]);
                    flag = true;
                }
            }
            if(flag)
                break;
 */
            }
            ja = ja1;
            content = ja.toString();
        }
        else
        {
            System.out.println("Such element already exists");
        }
    }

    private static JSONObject replaceKeyInJSONObject(JSONObject jsonObject, String jsonKey,
                                                     String jsonValue) {

        for (Object key : jsonObject.keySet()) {
            if (key.equals(jsonKey) && ((jsonObject.get((String) key) instanceof String)||(jsonObject.get((String) key) instanceof Number)||jsonObject.get((String) key) ==null)) {
                jsonObject.put((String) key, jsonValue);
                return jsonObject;
            } else if (jsonObject.get((String) key) instanceof JSONObject) {
                JSONObject modifiedJsonobject = (JSONObject) jsonObject.get((String) key);
                if (modifiedJsonobject != null) {
                    replaceKeyInJSONObject(modifiedJsonobject, jsonKey, jsonValue);
                }
            }

        }
        return jsonObject;
    }

    private static JSONObject updateJson(JSONObject obj, String keyString, String newValue) throws Exception {
        JSONObject json = new JSONObject();
        // get the keys of json object
        Iterator iterator = obj.keys();
        String key = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            // if the key is a string, then update the value
            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                if ((key.equals(keyString))) {
                    // put new value
                    obj.put(key, new JSONObject(newValue));
                    return obj;
                }
            }

            // if it's jsonobject
            if (obj.optJSONObject(key) != null) {
                updateJson(obj.getJSONObject(key), keyString, newValue);
            }

            // if it's jsonarray
            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    updateJson(jArray.getJSONObject(i), keyString, newValue);
                }
            }
        }
        return obj;
    }

    private static JSONObject deleteJson(JSONObject obj, String keyString) throws Exception {
        JSONObject json = new JSONObject();
        // get the keys of json object
        Iterator iterator = obj.keys();
        String key = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            // if the key is a string, then update the value
            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                if ((key.equals(keyString))) {
                    // put new value
                    obj.remove(keyString);
                    return obj;
                }
            }

            // if it's jsonobject
            if (obj.optJSONObject(key) != null) {
                deleteJson(obj.getJSONObject(key), keyString);
            }

            // if it's jsonarray
            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    deleteJson(jArray.getJSONObject(i), keyString);
                }
            }
        }
        return obj;
    }



    private static JSONObject replaceAll(JSONObject json, String key, String newValue) throws JSONException {
        Iterator<?> keys = json.keys();
        while (keys.hasNext()) {
            String k = (String) keys.next();
            if (key.equals(k)) {
                json.put(key, newValue);
            }
            Object value = json.opt(k);
            if (value instanceof JSONObject) {
                replaceAll((JSONObject) value, key, newValue);
            }
        }
        return json;
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
