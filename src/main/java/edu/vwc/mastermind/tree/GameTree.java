package edu.vwc.mastermind.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

public class GameTree {
	
	public final Code guess;
	public final Map<Response, List<Code>> answerGroups;
	
	private List<GameTree> children;
	
	public GameTree(Code guess, Map<Response, List<Code>> answerGroups) {
		this.guess = guess;
		this.answerGroups = answerGroups;
		this.children = new LinkedList<GameTree>();
	}
	
	public void addChild(GameTree child) {
		children.add(child);
	}

	public GameTreeVisitor<?> accept(GameTreeVisitor<?> visitor) {
		visitor.visit(this);
		
		for (GameTree child : children) {
			visitor.visit(child);
		}
		
		return visitor;
	}
}
