/**
 * This demo uses the AtomFeed parser to load twitter search results.
 *
 * (c) 2011 Karsten Schmidt // LGPLv2 licensed
 */

import toxi.data.feeds.*;

AtomFeed  feed=AtomFeed.newFromURL("http://search.twitter.com/search.atom?q=toxiclibs");
println(feed.entries.size()+" entries loaded");

for(AtomEntry e : feed) {
  println(String.format("[%s] %s", e.author.name, e.title));
}

