ControlP5 ui;

void initGUI() {
  ui = new ControlP5(this);
  ui.addSlider("isoThreshold",1,12,isoThreshold,20,20,100,14).setLabel("iso threshold");

  ui.addToggle("showPhysics",showPhysics,20,60,14,14).setLabel("show particles");
  ui.addToggle("isWireFrame",isWireFrame,20,100,14,14).setLabel("wireframe");
  ui.addToggle("isClosed",isClosed,20,140,14,14).setLabel("closed mesh");
  ui.addToggle("toggleBoundary",useBoundary,20,180,14,14).setLabel("use boundary");

  ui.addBang("initPhysics",20,240,28,28).setLabel("restart");
}


