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
  
  void updateGame(){
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
  
  boolean running(){
    if(population.numOfAlive > 0 || player.dotStatus) return true;
    else if(millis() <= timeEnd + 3000) return true;
    else return false;  
  }
  
  void checkCollision(){
   for(int i=0; i< population.dots.length; i++){
     if(!population.dots[i].dotStatus){
       if(dist(player.pos.x, player.pos.y, population.dots[i].pos.x, population.dots[i].pos.y) < collisionBox){
         player.dotStatus = false;
       }
     }
    }
  }
  
  
  void showInfoSet(){
    fill(0);
    textSize(13);
    text("Numar boti : " + population.numOfAlive + " / " + population.dots.length, 10, 30);
    if(population.numOfAlive <= 1 || 
       population.dots[population.lastAlive].step == population.dots[population.lastAlive].instructionNum)
       text("Ultimul bot in viata: #" + population.lastAlive, 10, 45);
    postgameInfo();
  }
  
  void postgameInfo(){
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
