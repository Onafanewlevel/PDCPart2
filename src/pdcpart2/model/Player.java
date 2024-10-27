/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2.model;

/**
 *
 * @author setefanomuller
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
