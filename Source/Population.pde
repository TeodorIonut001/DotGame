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
  
  void show(){
    //BOTS
    for(int i=0; i<dots.length; i++){
      dots[i].show();  
    }
  }
  
  //numara toate dot-urile inca in miscare
  void dotsAlive(){
    numOfAlive = 0;
    for(int i=0; i<dots.length; i++){
      if(dots[i].dotStatus){
        numOfAlive++;
        lastAlive = i;
      }
    }  
  }
  
  void update(){
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
