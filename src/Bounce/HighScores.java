/*
 * HighScores.java
 *
 * Created on November 5, 2004, 12:55 PM
 */

package Bounce;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScores {
    private ArrayList<HighScore> scores = new ArrayList<HighScore>();
    
    /** Creates a new instance of HighScores */
    public HighScores() {
    }
    public void saveHighScores(){      
        
        // check to see if the HighScores file exists
        File f = new File("HighScores.txt");
        // if it does not exist try to create it
        if (!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Failed to create the file");
            }
        }
        // save the high scores to file
        try {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            out.writeObject(scores);
            out.close();
            //System.out.println("Saved High Scores To: " + f.getAbsolutePath() );
        } catch (IOException e) {
            System.out.println("Error saving high scores");
        }
    }
        
    public void loadHighScores(){
        // load high scores from disk
        File f = new File("HighScores.txt");
        if (f.exists()){
            try {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
                scores = (ArrayList<HighScore>)in.readObject();
                in.close();
                //System.out.println("Read High Scores From: " + f.getAbsolutePath() );
                
            } catch (IOException e) {
                System.out.println("error reading high scores");
            } catch (ClassNotFoundException cnfe){
                System.out.println("error reading high scores");
            }
        } else {
            scores.add(new HighScore(1000, "Abraham", 1));
            scores.add(new HighScore(900, "Isaac", 1));
            scores.add(new HighScore(800, "Jacob", 1));
            scores.add(new HighScore(700, "Judah", 1));
            scores.add(new HighScore(600, "Perez", 1));
            scores.add(new HighScore(500, "Hezron", 1));
            scores.add(new HighScore(400, "Ram", 1));
            scores.add(new HighScore(300, "Amminadab", 1));
            scores.add(new HighScore(200, "Nahshon", 1));
            scores.add(new HighScore(100, "Salmon", 1));      
        }
    }
    
    public boolean isHighScore(long newScore){
        Collections.sort(scores, Collections.reverseOrder());
        HighScore bottomScore = (HighScore)scores.get(scores.size() - 1);        
        if (newScore > bottomScore.getScore() ){
            return true;            
        } else {
            return false;
        }
    }
    
    public long getHighScore(){
        HighScore tmp = (HighScore)scores.get(0);
        return tmp.getScore();
    }
    
    public void addNewScore(String player, long score, int level){
    	// trim long player names
    	if (player.length() > 15){
    		player = player.substring(0, 15);
    	}
        HighScore tmp = new HighScore(score,player,level);
        scores.add(tmp);
        Collections.sort(scores, Collections.reverseOrder());
        if (scores.size() > 10){
            for (int i = 10; i < scores.size(); i++){
                scores.remove(i);                
            }                        
        }
    }
    
    public int size(){
        return scores.size();
    }
    
    public HighScore getScore(int index){
        return (HighScore)scores.get(index);
    }
}
