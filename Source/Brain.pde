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
  
  void setDirections(){
    for(int i=0; i<directions.length; i++){
      float randomAngle = random(2*PI);
      //seteaza o noua directie 
      directions[i] = PVector.fromAngle(randomAngle);
    }
  }
}
