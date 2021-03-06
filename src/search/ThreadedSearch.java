// Jacob Sphatt


package search;

import java.util.ArrayList;

public class ThreadedSearch<T> implements Runnable {

  private T target;
  private ArrayList<T> list;
  private int begin;
  private int end;
  private Answer answer;

  public ThreadedSearch() {
  }

  private ThreadedSearch(T target, ArrayList<T> list, int begin, int end, Answer answer) {
    this.target=target;
    this.list=list;
    this.begin=begin;
    this.end=end;
    this.answer=answer;
  }

  /**
  * Searches `list` in parallel using `numThreads` threads.
  *
  * You can assume that the list size is divisible by `numThreads`
  */
  public boolean parSearch(int numThreads, T target, ArrayList<T> list) throws InterruptedException {
   
	  this.answer = new Answer();
	  int searchPartSize = list.size()/numThreads;
	  Runnable[] Bund = new Runnable[numThreads];
	  Thread[] ArrayThread = new Thread[numThreads];
	  
	  
	  /*
    * First construct an instance of the `Answer` inner class. This will
    * be how the threads you're about to create will "communicate". They
    * will all have access to this one shared instance of `Answer`, where
    * they can update the `answer` field inside that instance.
    *
    * Then construct `numThreads` instances of this class (`ThreadedSearch`)
    * using the 5 argument constructor for the class. You'll hand each of
    * them the same `target`, `list`, and `answer`. What will be different
    * about each instance is their `begin` and `end` values, which you'll
    * use to give each instance the "job" of searching a different segment
    * of the list. If, for example, the list has length 100 and you have
    * 4 threads, you would give the four threads the ranges [0, 25), [25, 50),
    * [50, 75), and [75, 100) as their sections to search.
    *
    * You then construct `numThreads`, each of which is given a different
    * instance of this class as its `Runnable`. Then start each of those
    * threads, wait for them to all terminate, and then return the answer
    * in the shared `Answer` instance.
    */

	  // creating threads
	    for (int i = 0; i < numThreads; i++){
	    	
	      int startIndex = (i * searchPartSize);
	      int endIndex = startIndex + searchPartSize;
	      Bund[i] = new ThreadedSearch<T>(target, list, startIndex, endIndex, this.answer);
	      ArrayThread[i] = new Thread(Bund[i]);
	      ArrayThread[i].start();
	      
	    }

	    for (int i = 0; i < numThreads; i++){
	      ArrayThread[i].join();
	    }

	    // returns the correct answer
	    return answer.getAnswer();
	  }

  public void run() {
	  // looking for a mtach
	  for(int i = begin; i < end; i++){
	      if (answer.getAnswer()){ 
	        return; 
	      } else{
	    	  // finding a match when true
	        if(list.get(i).equals(target)){
	          answer.setAnswer(true);
	          return; 
	        }
	      }
	    }
	  }
  }

  private class Answer {
    private boolean answer = false;

    public boolean getAnswer() {
      return answer;
    }

    // This has to be synchronized to ensure that no two threads modify
    // this at the same time, possibly causing race conditions.
    public synchronized void setAnswer(boolean newAnswer) {
      answer = newAnswer;
    }
  }

}
