class Dot{
  Battle battle;
  Brain brain;
  
  PVector pos;
  PVector vel;
  PVector acc;
  
  float accel = 0;
  float velLim = 5;
  color alive, dead;
    
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
  
  void show(){
    fill(alive); 
    if(!dotStatus) fill(dead);
    ellipse(pos.x, pos.y, 4, 4);  
  }
  
  //speed = viteza de rotatie
  void showAsBomb(){
    noFill();
    //ellipse(pos.x, pos.y, 4, 4);
    //noFill();
    arc(pos.x, pos.y, 10, 10, 0, 2*PI);
  }
  
  void update(){
    if(dotStatus){
      move(); 
      step = brain.step;
    }
    checkStop();
  }
  
   void move(){
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
  
  void checkStop(){
    if(pos.x < 2 || pos.y < 2 || pos.x > width-2 || pos.y > height-2){
      dotStatus = false;
    }
  }
}
