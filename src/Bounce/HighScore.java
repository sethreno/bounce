/*
 * HighScore.java
 *
 * Created on August 10, 2004, 3:39 PM
 */

package Bounce;
import java.io.Serializable;

public class HighScore implements Comparable, Serializable {
    private long score;
    private String name;
    private int level;
    
    /** Creates a new instance of HighScore */
    public HighScore(long newScore, String newName, int newLevel) {
        this.score = newScore;
        this.name = newName;
        this.level = newLevel;
    }
    
    public long getScore() { return score; }
    public void setScore(long value) { score = value; }
    
    public String getName() { return name; }
    public void setName(String value) { name = value; }
    
    public int getLevel() { return level; }
    public void setLevel(int value) { level = value; }
    
    public int compareTo(Object o) {
        HighScore hs = (HighScore)o;
        if (this.score < hs.score){
            return -1;
        }
        if (this.score > hs.score){
            return 1;
        }
        return 0;
    }    
}
