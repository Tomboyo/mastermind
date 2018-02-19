package edu.vwc.mastermind.sequence.provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

/**
 * Filter out codes which are isomporphs of one another. Two codes are
 * considered isomorphic if they have the same number of distinct colors and
 * are proportion their pegs among those distinct colors, without respect to
 * order, equivalently.
 *
 * By the above definition, [1, 1, 2, 2] is an isomorphism of [2, 2, 3, 3]
 * and [3, 3, 2, 2], but not [1, 2, 3, 3] or [1, 2, 2, 2].
 *
 * The above applies only to a universe of codes of uniform length.
 */
public final class WithoutIsomorphsCodesProvider implements CodesProvider {

    private final CodesProvider delegate;
    private final int colors;

    public WithoutIsomorphsCodesProvider(
            CodesProvider delegate,
            int colors) {
        this.delegate = delegate;
        this.colors = colors;
    }

    @Override
    public Set<Code> getCodes() {
        Set<Code> source = delegate.getCodes();
        Set<Code> result = new HashSet<>();
        Set<Integer> mask = new HashSet<>();
        source.forEach(code -> {
            int identity = identity(code.toArray(), colors);
            if (!mask.contains(identity)) {
                result.add(code);
                mask.add(identity);
            }
        });

        return result;
    }

    private int identity(int[] code, int colors) {
        int[] bins = new int[colors];
        int currentBin = 0;
        bins[0] = 1;

        Arrays.sort(code);

        for (int i = 1; i < code.length; i++) {
            if (code[i] != code[i - 1])
                currentBin += 1;
            bins[currentBin] += 1;
        }

        Arrays.sort(bins);

        int identity = 0;
        int factor = 1;
        for (int i = 0; i < bins.length; i++) {
            identity += bins[i] * factor;
            factor *= colors;
        }

        return identity;
    }

}
