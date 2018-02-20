package edu.vwc.mastermind.tree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.vwc.mastermind.Integers;
import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

public final class TreeBuilder {

    private final StringBuilder workingEncodedTree;

    public TreeBuilder() {
        workingEncodedTree = new StringBuilder();
    }

    public TreeBuilder addLine(String encodedInput) {
        if (workingEncodedTree.length() > 0) {
            encodedInput = System.lineSeparator() + encodedInput.trim();
        }
        workingEncodedTree.append(encodedInput);
        return this;
    }

    public Tree build() {
        String[] lines = workingEncodedTree.toString()
                .split(System.lineSeparator());
        if (lines.length == 0)
            raiseMalformedInputException();

        Tree root = null;

        for (String line : lines) {
            if (line.isEmpty())
                raiseMalformedInputException();

            root = root != null
                    ? Tree.union(root, parseLine(line))
                    : parseLine(line);
        }

        return root;
    }

    private Tree parseLine(String line) {
        AdvancingPatternMatcher matcher = new AdvancingPatternMatcher(line);

        Code firstGuess = Code.valueOf(matcher.nextSequence());
        Tree root = new Tree(firstGuess);

        // [guess sequence] -> [response seq.][guess seq.] -> ...
        Tree current = root;
        while (matcher.hasRemainingMatches()) {
            matcher.advancePastArrow();
            int[] response = matcher.nextSequence();
            int[] code = matcher.nextSequence();

            Response nextResponse = Response.valueOf(
                    response[0], response[1], response[2]);
            Code nextGuess = Code.valueOf(code);
            Tree child = new Tree(nextGuess);

            current.add(nextResponse, child);
            current = child;
        }

        return root;
    }

    private void raiseMalformedInputException() {
        throw new IllegalArgumentException("Missing or malformed input");
    }

    private class AdvancingPatternMatcher {
        private final Matcher sequenceMatcher;
        private final Matcher arrowMatcher;
        private int start;

        public AdvancingPatternMatcher(String input) {
            sequenceMatcher = Token.SEQUENCE.matcher(input);
            arrowMatcher = Token.ARROW.matcher(input);
            start = 0;
        }

        public int[] nextSequence() {
            if (!sequenceMatcher.find(start))
                raiseMalformedInputException();
            String matched = sequenceMatcher.group();
            start = sequenceMatcher.end();
            return Integers.fromStringifiedArray(matched);
        }

        public void advancePastArrow() {
            if (!arrowMatcher.find(start))
                raiseMalformedInputException();
            start = arrowMatcher.end();
        }

        public boolean hasRemainingMatches() {
            return arrowMatcher.find(start) || sequenceMatcher.find(start);
        }
    }

    private enum Token {
        ARROW("->"),
        SEQUENCE("\\[[\\d,\\s]+\\]");

        private final Pattern pattern;

        private Token(String regex) {
            pattern = Pattern.compile(regex);
        }

        public Matcher matcher(String input) {
            return pattern.matcher(input);
        }
    }

}
