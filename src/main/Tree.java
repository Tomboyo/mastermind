package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;

// TODO: Research Future thread?
class Tree {
  int length;
  Key[] allKeys;
  Response correctResponse;  
  Node gameTree;
  
  Tree (int colors, int pegs) {
    length = -1;
    gameTree = null;
    
    // Generate the Correct Response Code
    int[] correctResponseCode = new int[pegs];
    Arrays.fill(correctResponseCode, 2);
    correctResponse = new Response(correctResponseCode);
    
    allKeys = generateAllKeys(colors, pegs);
  }
  
  void compute(boolean log) {      
    Branch rootBranch = new Branch(this, allKeys, new boolean[allKeys.length]);
    this.length = -1;
    
    if (log) {
      gameTree = new Node();
      length = rootBranch.compute(gameTree);
    } else {
      length = rootBranch.compute();
    }
  }
  
  /*
   * -1 indicates that compute() has not been called.
   */
  int length() {
    return this.length;
  }
  
  Node getGameTree() {
    return this.gameTree;
  }
  
  private static Key[] generateAllKeys(int colors, int pegs) {
    int numCodes = (int)Math.pow(colors,  pegs);
    Key[] keys = new Key[numCodes];
    int codesIndex = 0;

    // Tracker holds the code we are generating. We treat its 'pegs' like base-colors numbers.
    int[] tracker = new int[pegs];
    int max_digit = colors - 1;
    
    for (int i = 0; i < numCodes; i++) {
      // Increment the 0th digit
      tracker[0] ++;
      
      // Perform carry operation. Stop if the tracker holds its maximum value
      int carry_index = 0;
      while (tracker[carry_index] > max_digit) {
        tracker[carry_index] = 0;
        carry_index ++;
        
        // Unsigned overflow (carry)
        if (carry_index == pegs) {
          break;
        }
        
        tracker[carry_index] ++;
      }
      
      // Tracker now represents the next code.
      keys[codesIndex++] = new Key(tracker);
    }
    
    return keys;
  }

  private class Branch {
    private Tree root;
    ArrayList<CompositeGuessDataContainer> compositeData;  // Holds guess/response data used in computation
    boolean[] codeMask; // Codes that can not be used as guesses by this branch
    
    Branch(Tree r, Key[] possibleAnswers, boolean[] mask) {
      this.codeMask = mask;
      this.root = r;
      
      //compositeData = new CompositeGuessDataContainer[Branch.allCodes.length];
      compositeData = new ArrayList<CompositeGuessDataContainer>(possibleAnswers.length);
      
      // Make every guess, even if the code you're using couldn't be the answer.
      // TODO: visitor generator gives pruned codes from AllCodes
      for (int i = 0; i < root.allKeys.length; i++) {
        if (codeMask[i]) continue;
        
        Key guess = root.allKeys[i];
      
        Hashtable<Response, ArrayList<Key>> responseGroups = new Hashtable<Response, ArrayList<Key>>();
        
        // What response would this guess get from any of the possible answers?
        for (Key master : possibleAnswers) {
          Response responseCode = guess.getFeedbackFromKey(master);
          
          //System.err.println(Arrays.toString(responseCode)+": "+guess+"<-->"+master);
          //Log the master key used based on the response garnered by the guess
          if (responseGroups.containsKey(responseCode)) {
            responseGroups.get(responseCode).add(master);
          } else {
            ArrayList<Key> list = new ArrayList<Key>();
            list.add(master);
            responseGroups.put(responseCode, list);
          }
        }
        
        // Save this guess' results in a composite data object
        compositeData.add(new CompositeGuessDataContainer(guess, i, responseGroups));
      }
      
      //TODO: call prune()
    }
  
    // Compute (from the non-first level of branches)
    private int compute(Node runningLog) {
      // This value is actually arbitrary. The algorithm will overwrite with the first branch's results.
      int shortestGame = Integer.MAX_VALUE;
      Node currentLog[] = null;
      
      for (CompositeGuessDataContainer composite : compositeData) {
        // Based on a guess we made, here are how the possible master keys are grouped by the guess' response code
        // For each response group, we need to see how long it takes to win
        // Then we compare the results of *this* guess to every other guess we've made. Which one is fastest?
      
        Hashtable<Response, ArrayList<Key>> responseGroups = composite.getResponseGroups();
        int maximumTurnsToWin = 1;
        
        Node[] tentativeLog = new Node[responseGroups.size()];
        int logIndex = 0;
        
        for (Entry<Response, ArrayList<Key>> responseGroup : responseGroups.entrySet()) {
          tentativeLog[logIndex++] = new Node(composite.getGuess(), responseGroup.getKey());
          if (responseGroup.getKey().equals(correctResponse)) {
            // This response group contains the answer, so our guess was right. This answer is found in 1 guess.
            // We defaulted maximum to 1 turn, so just noop.
            continue;
          }
          
          // We have response and a given number of master keys that could still be answers
          // We must start new Branches to determine how long it takes to win for each response group
          
          ArrayList<Key> list = responseGroup.getValue();
          Key[] codes = new Key[list.size()];
          codes = responseGroup.getValue().toArray(codes);
          
          // Short-circuit obvious one-step branches if we don't need a log.
          if (runningLog == null) {
            if (codes.length == 1) {
              if (2 > maximumTurnsToWin) {
                maximumTurnsToWin = 2;
              }
              continue;
            }
          }
          
          boolean[] newMask = Arrays.copyOf(codeMask, codeMask.length);
          newMask[composite.getCodeIndex()] = true;
          
          Branch branchForThisGroup = new Branch(this.root, codes, newMask);
          int turns = 1 + branchForThisGroup.compute(tentativeLog[logIndex - 1]);
          if (turns > maximumTurnsToWin) {
            maximumTurnsToWin = turns;
          } else {
            //tentativeLog = null;
          }
        }
        
        // So it takes a maximum of maximumTurnsToWin to complete this branch and get any answer, whatever it is.
        // Is that faster than any other guess' results, so far? Track it if it's the first one, too, so we have a base value.
        if (maximumTurnsToWin < shortestGame || currentLog == null) {
          shortestGame = maximumTurnsToWin;
          currentLog = tentativeLog;
        }
      }
      
      if (runningLog != null) {
        runningLog.setChildren(currentLog);
      }
      
      return shortestGame;
    }

    // Compute (from the first level of branches)
    int compute() {
      return compute(null);
    }
  }
  
  /*
   * Record of moves made in a game evaluation.
   */
  private class Node {
    Key guess;
    Response response;
    Node[] children;
    
    public Node() {}
    
    public Node(Key guess, Response response) {
      this.response = response;
      this.guess = guess;
    }
    
    public void displayGameLog() {
      if (children == null || children.length == 0) {
        System.out.println(this + ".");
        return;
      }
      
      String me = this.toString();
      for (Node child : children) {
        System.out.print((me.length() > 0 ? me + " -> " : ""));        
        child.displayGameLog();
      }
    }
    
    public void setChildren(Node[] children) {
      this.children = children;
    }
    
    @Override
    public String toString() {
      return (guess == null
          ? ""
          : guess.toString() + (response == null
            ? ""
            : response.toString()));
    }
  }
  

  public static void main(String[] args) {
    Tree root = new Tree(3, 2);
    root.compute(true);
    
    System.out.println(root.length() + "\n");
    
    root.getGameTree().displayGameLog();
  }
}

