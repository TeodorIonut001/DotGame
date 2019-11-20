Game game;
void setup(){
  size(800, 800);
  surface.setAlwaysOnTop(true);  
  //Game(int dificultate);  => dif * 100
  game = new Game(2);
}

void draw(){
  background(255);
  game.updateGame();

}

void keyPressed(){
  game.player.keyPressed();
}

void keyReleased(){
  game.player.keyReleased();
}
