/**
 * <p>This is an example usage of the WeightedRandomSet class to define a number of
 * options with attached weights which are taken into account when picking random 
 * elements from the set. The class is using java generics to work with any type of
 * data.</p>
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

WeightedRandomSet<String> languages=new WeightedRandomSet<String>();

languages.add("cpp",10);
languages.add("java",40);
languages.add("javascript",15);
languages.add("obj-c",2);
languages.add("php",10);
languages.add("processing",20);
languages.add("python",5);

HashMap<String,Integer> hist = new HashMap<String,Integer>();
int numIter=1000;

for(int i=0; i<numIter; i++) {
  String lang=languages.getRandom();
  if (hist.get(lang)==null) {
    hist.put(lang,1);
  } else {
    hist.put(lang,hist.get(lang)+1);
  }
}

for(String id : hist.keySet()) {
  println(id+": "+hist.get(id)/(float)numIter);
}
