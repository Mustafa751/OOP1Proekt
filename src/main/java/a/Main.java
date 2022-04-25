package a;


public class Main {

    public static void main(String[] args) {
        System.out.println("initial commit");
        FileWorker fw = new FileWorker("writePost.json");
        fw.Open();
        fw.Validate();
        fw.Print();
        fw.Search("menuitem");
        //fw.Delete("id");
        //fw.Print();
    }
}
