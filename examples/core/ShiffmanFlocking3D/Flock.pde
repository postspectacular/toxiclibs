// Flock class
// Does very little, simply manages the ArrayList of all the boids

class Flock {
  ArrayList boids; // An arraylist for all the boids

    Flock() {
    boids = new ArrayList(); // Initialize the arraylist
  }

  void run() {
    for (int i = boids.size()-1 ; i >= 0 ; i--) {
      Boid b = (Boid) boids.get(i);  
      b.run(boids);  // Passing the entire list of boids to each boid individually
    }
  }

  void addBoid(Boid b) {
    boids.add(b);
  }
}

