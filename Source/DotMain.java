import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DotMain extends PApplet {

Game game;
public void setup(){
  
  surface.setAlwaysOnTop(true);  
  //Game(int dificultate);  => dif * 100
  game = new Game(2);
}

public void draw(){
  background(255);
  game.updateGame();

}

public void keyPressed(){
  game.player.keyPressed();
}

public void keyReleased(){
  game.player.keyReleased();
}

class Battle{
  
  //diametru
  float d = 1.2f*(width - 10);
  float minSize = 150;
  float decreasingSpeed =5;
  float speed = 2;
  float arcWidth  = width;
  float arcHeight = height;
  PVector arcCenter = new PVector(width/2, height/2);
  PVector arcVel = new PVector(0, 0);
  PVector arcAcc = new PVector(0, 0);
  
  boolean reachedEnd = false;
  
  public void setupBattle(){
    randomPos();
  }
   
  public void update(){
    fill(0,0,128);
    noStroke();
    //arcul mare
    arc(width/2, height/2, width, height, 0, 2*PI);    
    if(d >= minSize){
      fill(255);
      noStroke();
      //arcul mic
      arc(arcCenter.x, arcCenter.y, d, d, 0, 2*PI);
      d-= decreasingSpeed;
    }
    fill(255);
    noStroke();
    arc(arcCenter.x, arcCenter.y, d, d, 0, 2*PI);
    
    randomVel();
  }
  
  public float getRadius(){
    return d/2;
  }
  
  
  public boolean isEaten(float x, float y){
    if(dist(arcCenter.x, arcCenter.y, x, y) >= getRadius())
      return true;
    else return false;
  }
  
  public void randomPos(){
    arcCenter = new PVector((int)random(width), (int)random(height));
    if(dist(arcCenter.x, arcCenter.y, width/2, height/2) <= width/2){
        return;
    }else randomPos();
  }
  public void randomVel(){
    float angle = random(2*PI);
    if(dist(arcCenter.x, arcCenter.y, width/2, height/2) <= width/2){
      arcAcc = PVector.fromAngle(angle);
      arcVel.add(arcAcc);
      arcVel.limit(speed);
      arcCenter.add(arcVel);
    }else{
      reachedEnd = true;
    }
    
  }
  
  
  
}
class Brain{
  //contine directiile de miscare
  PVector[] directions;
  
  int step = 0;
  
  Brain(int size){
    directions = new PVector[size];
    setDirections();
  }
  
  Brain(){
  //do nothing
  }
  
  public void setDirections(){
    for(int i=0; i<directions.length; i++){
      float randomAngle = random(2*PI);
      //seteaza o noua directie 
      directions[i] = PVector.fromAngle(randomAngle);
    }
  }
}
class Dot{
  Battle battle;
  Brain brain;
  
  PVector pos;
  PVector vel;
  PVector acc;
  
  float accel = 0;
  float velLim = 5;
  int alive, dead;
    
  //true daca este functional
  boolean dotStatus = true;
  
  //numarul de directii
  int instructionNum = 1000;
  int step;
  
  Dot(){
    brain = new Brain(instructionNum);
    //dot-ul porneste din centru, fara viteza sau acceleratie
    pos = new PVector(width/2, height/2);
    vel = new PVector(0, 0);
    acc = new PVector(0, 0);
    alive = color(255, 10, 10);
    dead  = color(0, 0, 0);
  }
  
  public void show(){
    fill(alive); 
    if(!dotStatus) fill(dead);
    ellipse(pos.x, pos.y, 4, 4);  
  }
  
  //speed = viteza de rotatie
  public void showAsBomb(){
    noFill();
    //ellipse(pos.x, pos.y, 4, 4);
    //noFill();
    arc(pos.x, pos.y, 10, 10, 0, 2*PI);
  }
  
  public void update(){
    if(dotStatus){
      move(); 
      step = brain.step;
    }
    checkStop();
  }
  
   public void move(){
    //daca inca sunt directii de urmat
    if(brain.directions.length > brain.step){
      //seteaza urmatoarea directie / acceleratie 
      acc = brain.directions[brain.step];
      brain.step++;
    }
    else dotStatus = false;
    
    //aplica acceleratia
    vel.add(acc);
    //limitam viteza
    vel.limit(velLim);
    //aplica viteza
    pos.add(vel);
  }
  
  public void checkStop(){
    if(pos.x < 2 || pos.y < 2 || pos.x > width-2 || pos.y > height-2){
      dotStatus = false;
    }
  }
}
class Game{
  Battle battle;
  Population population;
  Player player;
  
  long timeStart, timeEnd;
  float collisionBox = 5;
  int dif; //dificultate
  int lastTime = 0;
  
  Game(int difficulty){
    dif = difficulty;
    timeStart = millis();
    battle = new Battle();
    battle.setupBattle();
    
    player = new Player();
    population = new Population(dif * 100);
  }
  
  public void updateGame(){
    if(running()){
      //#1
      population.update();
      player.update(population.battleRadius,
                    population.battle.arcCenter.x,
                    population.battle.arcCenter.y);
      player.show();
      population.show();
      
      //#2
      checkCollision();
      showInfoSet();
      if(player.dotStatus)
        timeEnd = millis();
        lastTime = (int)(timeEnd - timeStart) / 1000;
      }else{
      //postgameInfo();
      setup();
      draw();
      }
  }
  
  public boolean running(){
    if(population.numOfAlive > 0 || player.dotStatus) return true;
    else if(millis() <= timeEnd + 3000) return true;
    else return false;  
  }
  
  public void checkCollision(){
   for(int i=0; i< population.dots.length; i++){
     if(!population.dots[i].dotStatus){
       if(dist(player.pos.x, player.pos.y, population.dots[i].pos.x, population.dots[i].pos.y) < collisionBox){
         player.dotStatus = false;
       }
     }
    }
  }
  
  
  public void showInfoSet(){
    fill(0);
    textSize(13);
    text("Numar boti : " + population.numOfAlive + " / " + population.dots.length, 10, 30);
    if(population.numOfAlive <= 1 || 
       population.dots[population.lastAlive].step == population.dots[population.lastAlive].instructionNum)
       text("Ultimul bot in viata: #" + population.lastAlive, 10, 45);
    postgameInfo();
  }
  
  public void postgameInfo(){
    fill(150);
    textSize(35);
    if(!player.dotStatus){
      if(lastTime < 20)
        text("Ai supravietuit timp de " + lastTime + " secunde.", 150, height/2 - 50);
      else
        text("Ai supravietuit timp de " + lastTime + " de secunde.",150, height/2 - 50);
    }
  }
  
}
class Player extends Dot{
  
  Battle battle; 
  
  //pentru miscari verticale / orizontale
  float speed = 2;
  //pentru miscari oblice
  float scaleSpeed = 0.1f;
  boolean up, down, left, right;
  
  Player(){
    //pozitie initiala
    pos = new PVector(width/2, height/2);
    up = false; down = false; left = false; right = false;
  }
  
  public void show(){
    fill(10, 200, 10);
    ellipse(pos.x, pos.y, 7, 7);
  }
  
  public void update(float radius, float x, float y){
    if(dotStatus){
      move();
      if(dist(x, y, pos.x, pos.y) > radius) dotStatus = false; 
    }
    checkStop();
  }
  
  public void move(){
    //verifica comenzile
    if(up)    pos.y -= speed;
    if(down)  pos.y += speed;
    if(left)  pos.x -= speed;
    if(right) pos.x += speed;
    
    if(up && right){
      pos.y -= scaleSpeed/2;
      pos.x += scaleSpeed/2;
    }
    if(up && left){
      pos.y -= scaleSpeed/2;
      pos.x -= scaleSpeed/2;
    }
    if(down && right){
      pos.y += scaleSpeed/2;
      pos.y += scaleSpeed/2;
    }
    if(down && left){
      pos.y += scaleSpeed/2;
      pos.x -= scaleSpeed/2;
    } 
  }
  
  //call in DotMain
  public void keyPressed(){
    if(key == 'W' || key == 'w')  up = true;
    if(key == 'A' || key == 'a')  left = true;
    if(key == 'S' || key == 's')  down = true;
    if(key == 'D' || key == 'd')  right = true;
    
  }
  //call in DotMain
  public void keyReleased(){
    if(key == 'W' || key == 'w')  up = false;
    if(key == 'A' || key == 'a')  left = false;
    if(key == 'S' || key == 's')  down = false;
    if(key == 'D' || key == 'd')  right = false;
  }

}
class Population{
 
  Dot[] dots;
  Battle battle;
  
  float battleRadius;
  
  //numarul de dot-uri inca in viata
  int numOfAlive = 0;
  int lastAlive;
  
  
  Population(int size){
   battle = new Battle();
   dots = new Dot[size];
   for(int i=0; i<size; i++){
     dots[i] = new Dot();
     dots[i].pos.x = battle.arcCenter.x;
     dots[i].pos.y = battle.arcCenter.y;
   }
  }
  
  public void show(){
    //BOTS
    for(int i=0; i<dots.length; i++){
      dots[i].show();  
    }
  }
  
  //numara toate dot-urile inca in miscare
  public void dotsAlive(){
    numOfAlive = 0;
    for(int i=0; i<dots.length; i++){
      if(dots[i].dotStatus){
        numOfAlive++;
        lastAlive = i;
      }
    }  
  }
  
  public void update(){
    battle.update();
    battleRadius = battle.getRadius();
    //BOTS
    dotsAlive();
    for(int i=0; i<dots.length; i++){
      dots[i].update(); 
      
      if(dots[i].dotStatus){
        float dotRadius = dist(battle.arcCenter.x, battle.arcCenter.y, dots[i].pos.x, dots[i].pos.y);
        
        if(dotRadius >= battleRadius){
          dots[i].dotStatus = false;
          dots[i].showAsBomb();
        }
      }
    }
  }  

  
}
  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DotMain" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
