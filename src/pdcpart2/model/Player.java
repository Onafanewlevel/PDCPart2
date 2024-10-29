
package pdcpart2.model;

/**
 *
 * Author: Setefano Muller 
 *         Tharuka Rodrigo
 */
public class Player {
    private String name;
    private int score;
    
    public Player(String name){
        this.name = name;
        this.score = 0;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return this.score;
    }
}
