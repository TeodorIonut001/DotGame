class Battle{
  
  //diametru
  float d = 1.2*(width - 10);
  float minSize = 150;
  float decreasingSpeed =5;
  float speed = 2;
  float arcWidth  = width;
  float arcHeight = height;
  PVector arcCenter = new PVector(width/2, height/2);
  PVector arcVel = new PVector(0, 0);
  PVector arcAcc = new PVector(0, 0);
  
  boolean reachedEnd = false;
  
  void setupBattle(){
    randomPos();
  }
   
  void update(){
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
  
  float getRadius(){
    return d/2;
  }
  
  
  boolean isEaten(float x, float y){
    if(dist(arcCenter.x, arcCenter.y, x, y) >= getRadius())
      return true;
    else return false;
  }
  
  void randomPos(){
    arcCenter = new PVector((int)random(width), (int)random(height));
    if(dist(arcCenter.x, arcCenter.y, width/2, height/2) <= width/2){
        return;
    }else randomPos();
  }
  void randomVel(){
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
