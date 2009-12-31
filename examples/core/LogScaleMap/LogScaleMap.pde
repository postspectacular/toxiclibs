import toxi.math.*;

float expectedMinValue=1;
float expectedMaxValue=800;

ScaleMap linearMap;
ScaleMap logMap;

size(1000,200);
smooth();

// create 2 scale maps
// parameters: in_min, in_max, out_min, out_max
logMap=new ScaleMap(log(expectedMinValue),log(expectedMaxValue),0,width);
linearMap=new ScaleMap(expectedMinValue,expectedMaxValue,0,width);

for(float i=expectedMinValue; i<expectedMaxValue; i+=10) {
  // get log scale value
  float x1=(float)logMap.getMappedValueFor(log(i));
  stroke(0);
  line(x1,0,x1,50);
  // get linear mapped value
  float x2=(float)linearMap.getMappedValueFor(i);
  line(x2,150,x2,200);
  // draw connection line
  stroke(0,50);
  line(x1,50,x2,150);
} 

