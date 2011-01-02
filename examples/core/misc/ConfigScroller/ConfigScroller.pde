/**
 * <p>This demo uses an external configuration file to allow core parameters
 * to be edited by 3rd parties, without having to do any code changes. The class
 * TypedProperties has several accessor methods to retrieve data in different
 * primitive types (incl. arrays). When a config setting is missing, a default
 * value can be defined for it. The file format used for the config files is
 * the standard Java Properties format and can be edited with any plain text editor.
 *
 * http://download.oracle.com/javase/1.4.2/docs/api/java/util/Properties.html
 * http://download.oracle.com/javase/tutorial/essential/environment/properties.html
 * </p>
 */

/* 
 * Copyright (c) 2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
import toxi.util.datatypes.*;

TypedProperties config;

float offset;

public void init() {
  super.init();
  config=new TypedProperties();
  config.load(sketchPath("app.properties"));
}

void setup() {
  size(config.getInt("app.width",1280),config.getInt("app.height",720));
  textFont(createFont(
    config.getProperty("font.name","SansSerif"),
    config.getInt("font.size",72)
  ));
}

void draw() {
  background(config.getHexInt("color.bg",0xffffff));
  fill(config.getHexInt("color.text",0xff0000)|0xff<<24);
  String msg=config.getProperty("msg.text","hello world");
  float pos=-offset;
  float w=textWidth(msg)+100;
  while(pos<width) {
    text(msg,pos,height/2);
    pos+=w;
  }
  offset+=config.getFloat("msg.speed",2);
  if (offset>w) {
    offset-=w;
  }
}

