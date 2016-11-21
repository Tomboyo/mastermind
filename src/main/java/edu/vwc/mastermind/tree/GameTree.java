package edu.vwc.mastermind.tree;

public class GameTree {

	public GameTreeVisitor<?> accept(GameTreeVisitor<?> visitor) {
		visitor.visit(this);
		
		// Then visit each child here
		
		return visitor;
	}
}
