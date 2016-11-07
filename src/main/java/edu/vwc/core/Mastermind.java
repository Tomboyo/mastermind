package edu.vwc.core;

public class Mastermind {
  private int pegs, colors;
  private boolean verbose;
  
  public Mastermind() {}
  
  public Mastermind(int colors, int pegs) {
    this.colors = colors;
    this.pegs = pegs;
    this.verbose = false;
  }
  
  public Mastermind(int colors, int pegs, boolean verbose) {
    this(colors, pegs);
    this.verbose = verbose;
  }
  
  public void setConfig(int colors, int pegs, boolean verbose) {
    this.colors = colors;
    this.pegs = pegs;
    this.verbose = verbose;
  }
  
  public String getConfig() {
    return String.format("Colors: %d, Pegs: %d, Verbose?: %b", colors, pegs, verbose);
  }
  
  public String getUsage() {
    return "Usage: Mastermind <int colors> <int pegs> [<boolean verbose?=false>]";
  }
  
  public void run() throws Exception {
    if (colors == 0 || pegs == 0) {
      throw new Exception("Mastermind needs to be configured first.");
    }
    
    Tree root = new Tree(colors, pegs);
    root.compute(verbose);
    
    System.out.printf("Maximum turns to win: %d\n", root.length());
    if (verbose) {
      System.out.println("---------- Complete game tree ----------");
      root.getGameTree().displayGameLog();
    }
  }
  
  public static void main(String[] args) {
    Mastermind mastermind = new Mastermind();
    int colors, pegs;
    boolean verbose;
    
    if (args.length < 2) {
      System.out.println(mastermind.getUsage());
      return;
    }
    
    try {
      colors = Integer.parseInt(args[0]);
      pegs = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println(mastermind.getUsage());
      return;
    }
    
    verbose = args.length >= 3 ? Boolean.parseBoolean(args[2]) : false;
    
    mastermind.setConfig(colors, pegs, verbose);
    
    try {
      mastermind.run();
    } catch (Exception e) {
      System.out.printf("An error occurred: %s", e.getMessage());
    }
  }
}
