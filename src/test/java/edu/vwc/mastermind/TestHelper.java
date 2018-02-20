package edu.vwc.mastermind;
import java.util.LinkedHashSet;

import edu.vwc.mastermind.sequence.Code;

public final class TestHelper {

    public static LinkedHashSet<Code> setOfCodes(Code...codes) {
        LinkedHashSet<Code> set = new LinkedHashSet<>();
        for (Code c : codes) {
            set.add(c);
        }
        return set;
    }
}
