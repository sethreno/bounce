
import java.io.File;
import java.io.IOException;

/**
 *
 * @author  breno
 */
public class FileTest {
    
    /** Creates a new instance of FileTest */
    public FileTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long score = 300;
        long highScore = 200;
        
        File f = new File("testfile.txt");
        if (!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Failed to create the file");
            }
        }
        if (f.exists()){
            System.out.println("The file was created");
        }
        System.out.println("the absolute path is " + f.getAbsolutePath()); 
        // save high scores to file
        /*
        if (score > highScore){
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("c:\\HighScores.txt"));
                out.write(String.valueOf(score));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }
    
}
