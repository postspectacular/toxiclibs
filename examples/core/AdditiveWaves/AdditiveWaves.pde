import processing.opengl.*;
import toxi.math.waves.*;

AbstractWave waveX,waveY;

int step=10;
int w=1000;
int w2=w/2;

void setup() {
  size(1024,576,OPENGL);
  noStroke();
  waveX=new FMSquareWave(0, 0.08,1,0,new SineWave(0,0.02,0.5,0));
  waveY=new FMTriangleWave(0, 0.05,1,0,new SineWave(0,0.013,0.2,0));
}

void draw() {
  background(0);
  waveX.update();
  waveY.update();
  waveY.push();
  lights();
  translate(width*0.5,height*0.4,0);
  rotateX(0.8);
  rotateZ(mouseX*0.01);
  float prevY=waveY.update();
  float colPrevY=0;
  for(int y=0; y<w; y+=step) {
    float valueY=waveY.update();
    float colY=valueY*128+128;
    waveX.push();
    beginShape(TRIANGLE_STRIP);
    for(int x=0; x<w; x+=step) {
      float valueX=waveX.update();
      float colX=valueX*128+128;
      fill(colX,0,colPrevY);
      vertex(x-w2,y-step-w2,(valueX+prevY)*50);
      fill(colX,0,colY);
      vertex(x-w2,y-w2,(valueX+valueY)*50);
    }
    endShape();
    waveX.pop();
    prevY=valueY;
    colPrevY=colY;
  }
  waveY.pop();
}

