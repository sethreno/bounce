package Bounce;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;


public class LevelManager {
	private static final int MAX_TILES = 100;
	
	public static ArrayList<Tile> getTiles(int levelNumber){
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		//load the current level from disk.
		// each level is made up of 60 tiles 6 rows of 10 tiles
		// read chars from the file, a 1 represents a visible tile, a 0 is invisible
		// start the tiles 90 from the left and 100 from the top
		// each tile is 60x20 with a margin of 2
		int x = (int)(BounceGame.WIDTH * .1125); // 90
		int y = (int)(BounceGame.HEIGHT * .1666); // 100;
		int width = (int)(BounceGame.WIDTH * .0775); //62;
		int height = (int)(BounceGame.HEIGHT * .0366); //22;
		int row = 0;
		int tileCount = 0;
		File f = new File("levels/" + levelNumber);
        if (f.exists()){
        	// the file was found so try to load the level
            try {
            	BufferedReader levelFile = new BufferedReader(new FileReader(f));
            	String l;
            	while ((l = levelFile.readLine()) != null && tileCount <= MAX_TILES){
            		for (int i=0; i<l.length(); i++){
            			Tile t = new Tile(i * width + x, row * height + y);
            			if (l.charAt(i) == '1'){
            				t.setVisible(true);
            			}else{
            				t.setVisible(false);
            			}
            			tiles.add(t);
            			tileCount++;
            		}
            		row++;
            	}
            	levelFile.close();                
            } catch (IOException e) {
                System.out.println("error reading high scores");
            }
        }
        
        //if no tiles were loaded create a default level
        if (tiles.size() == 0){
        	for (int i = 0; i < 10; i++){
                for (int j = 0; j < 6; j++){
                    tiles.add( new Tile(i * width + x, j * height + y));                
                }            
              }
        }        
		return tiles;
	}
}
