import java.io.File;
import java.net.URL;

public class Test{

  private static File[] getResourceFolderFiles (String folder) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    URL url = loader.getResource(folder);
    System.out.println(url);
    String path = url.getPath();
    return new File(path).listFiles();
  }

  public static void main (String[] args) {
    for (File f : getResourceFolderFiles("")) {
//    System.out.println(f.getAbsolutePath());
    
    URL url = Thread.currentThread().getContextClassLoader().getResource("logoutLogo.png");
    System.out.println(url);
   }
    
   
  }
}