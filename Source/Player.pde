class Player extends Dot{
  
  Battle battle; 
  
  //pentru miscari verticale / orizontale
  float speed = 2;
  //pentru miscari oblice
  float scaleSpeed = 0.1;
  boolean up, down, left, right;
  
  Player(){
    //pozitie initiala
    pos = new PVector(width/2, height/2);
    up = false; down = false; left = false; right = false;
  }
  
  void show(){
    fill(10, 200, 10);
    ellipse(pos.x, pos.y, 7, 7);
  }
  
  void update(float radius, float x, float y){
    if(dotStatus){
      move();
      if(dist(x, y, pos.x, pos.y) > radius) dotStatus = false; 
    }
    checkStop();
  }
  
  void move(){
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
  void keyPressed(){
    if(key == 'W' || key == 'w')  up = true;
    if(key == 'A' || key == 'a')  left = true;
    if(key == 'S' || key == 's')  down = true;
    if(key == 'D' || key == 'd')  right = true;
    
  }
  //call in DotMain
  void keyReleased(){
    if(key == 'W' || key == 'w')  up = false;
    if(key == 'A' || key == 'a')  left = false;
    if(key == 'S' || key == 's')  down = false;
    if(key == 'D' || key == 'd')  right = false;
  }

}
