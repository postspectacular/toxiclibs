class HistogramWord {
  String word;
  int count;
  
  HistogramWord(String w, int c) {
    word=w;
    count=c;
  }

  String toString() {
    return word+": "+count;
  }
}

abstract class HistogramSorter implements Comparator<HistogramWord> {
  
  abstract int compare(HistogramWord a, HistogramWord b);
  
  abstract int getMetric(HistogramWord w);
}

class FrequencyComparator extends HistogramSorter {
  
  int compare(HistogramWord a, HistogramWord b) {
   return b.count-a.count;
  }
  
  public int getMetric(HistogramWord w) {
    return w.count;
  }
}

class WordLengthComparator extends HistogramSorter {
  
  int compare(HistogramWord a, HistogramWord b) {
   return b.word.length()-a.word.length();
  }
  
  public int getMetric(HistogramWord w) {
    return w.word.length();
  }
}

