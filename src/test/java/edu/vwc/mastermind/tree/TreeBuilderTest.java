package edu.vwc.mastermind.tree;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void createTree() {
        treeBuilder = new TreeBuilder();
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testNoInput() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Missing or malformed input");
        treeBuilder.build();
    }

    @Test
    public void testEmptyInput() {
        treeBuilder.addLine("");

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Missing or malformed input");
        treeBuilder.build();
    }

    @Test
    public void testTreeWithoutChildren1() {
        treeBuilder.addLine("[1]");
        Tree expected = new Tree(Code.valueOf(1));

        assertThat(treeBuilder.build(), equalTo(expected));
    }

    @Test
    public void testTreeWithChild() {
        treeBuilder.addLine("[1,0]->[0,0,2][2,2]");

        Tree expected = new Tree(Code.valueOf(1, 0));
        expected.add(
                Response.valueOf(0, 0, 2),
                new Tree(Code.valueOf(2, 2)));

        assertThat(treeBuilder.build(), equalTo(expected));
    }

    @Test
    public void testTreeWithMalformedChild() {
        treeBuilder.addLine("[1,0]->[0,0,2]");

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Missing or malformed input");
        treeBuilder.build();
    }

    @Test
    public void testTreeWithMultipleGroupedChildren() {
        treeBuilder = new TreeBuilder();
        treeBuilder.addLine("[0,1]->[0,2,0][1,0]");
        treeBuilder.addLine("[0,1]->[0,0,2][2,2]");
        treeBuilder.addLine("[0,1]->[0,1,1][0,0]->[1,0,1][2,0]");
        treeBuilder.addLine("[0,1]->[0,1,1][0,0]->[0,0,2][1,2]");

        Tree expected = new Tree(Code.valueOf(0, 1));
        expected.add(Response.valueOf(0, 2, 0),
                new Tree(Code.valueOf(1, 0)));
        expected.add(Response.valueOf(0, 0, 2),
                new Tree(Code.valueOf(2, 2)));

        Tree subTree = new Tree(Code.valueOf(0, 0));
        subTree.add(Response.valueOf(1, 0, 1),
                new Tree(Code.valueOf(2, 0)));
        subTree.add(Response.valueOf(0, 0, 2),
                new Tree(Code.valueOf(1, 2)));

        expected.add(Response.valueOf(0, 1, 1), subTree);

        assertThat(treeBuilder.build(), equalTo(expected));
    }

    @Test
    public void testMalformedSingleNodeWithoutChildren() {
        List<String> encodedTrees = Arrays.asList(
                "[]",
                "]", "[",
                ",]", "[,",
                "[1,", ",1]",
                "1,2]", "[1,2");

        encodedTrees.forEach(encodedTree -> {
            treeBuilder.addLine(encodedTree);

            try {
                treeBuilder.build();
                fail(String.format(
                        "Expected input '%s' to raise exception",
                        encodedTree));
            } catch (IllegalArgumentException e) {
                assertEquals("Missing or malformed input", e.getMessage());
            }
        });
    }

}
