package edu.vwc.mastermind.sequence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.PotentialAssignment;

import edu.vwc.mastermind.sequence.provider.AllCodesProvider;

final class CodeTheoriesHelper {

    @Retention(RetentionPolicy.RUNTIME)
    @ParametersSuppliedBy(CodeRangeSupplier.class)
    public @interface CodeRange {
        int minCodeLength() default 1;
        int maxCodeLength() default 3;
        int minNumberColors() default 1;
        int maxNumberColors() default 3;
    }

    private CodeTheoriesHelper() {}

    public static final class CodeRangeSupplier extends ParameterSupplier {

        @Override
        public List<PotentialAssignment> getValueSources(ParameterSignature sig)
                throws Throwable {
            List<PotentialAssignment> values = new LinkedList<>();
            CodeRange annotation = sig.getAnnotation(CodeRange.class);

            for (int pegs = annotation.minCodeLength();
                    pegs < annotation.maxCodeLength();
                    pegs++) {
                for (int colors = annotation.minNumberColors();
                        colors < annotation.maxNumberColors();
                        colors++) {
                    new AllCodesProvider(colors, pegs).getCodes().stream()
                            .map((Code code) -> PotentialAssignment.forValue(
                                    code.toString(), code))
                            .forEach(values::add);
                }

            }

            Collections.shuffle(values);
            return values;
        }
    }

}
