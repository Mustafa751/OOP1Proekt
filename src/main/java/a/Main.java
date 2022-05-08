package a;


public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("initial commit");
        FileWorker fw = new FileWorker("writePost.json");
        fw.Open();
        fw.Validate();
  //      fw.Print();
       // fw.Search("menuitem");
       // fw.Set();
       // fw.Print();
       // fw.Create();
      //  fw.Print();
        //fw.Delete();
        fw.Print();
    }
}
