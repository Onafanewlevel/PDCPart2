/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2;

/**
 *
 * @author Onafanewlevel
 */
public class Player {
    private String name;
    private int score;
    private boolean hasLifeline;
    
    public Player(String name){
        this.name = name;
        this.score = 0;
        this.hasLifeline = true;
    }
    
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public int getScore(){return score;}
    public void setScore(int score){this.score = score;}
    public boolean hasLifeline(){return hasLifeline;}
    public void sethasLifeline(boolean hasLifeline){this.hasLifeline = hasLifeline;}
}
