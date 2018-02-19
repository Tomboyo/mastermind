package edu.vwc.mastermind.sequence.provider;

import static edu.vwc.mastermind.TestHelper.setOfCodes;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Set;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;

public class WithoutIsomorphsCodesProviderTest extends EasyMockSupport {

    private CodesProvider testSubject;
    private CodesProvider mockDelegate;

    @Before
    public void setUp() {
        mockDelegate = mock(CodesProvider.class);
        testSubject = new WithoutIsomorphsCodesProvider(mockDelegate, 4);
    }

    @Test
    public void allUniqueInput() {
        expect(mockDelegate.getCodes()).andReturn(setOfCodes(
                Code.valueOf(1, 1, 1, 1), Code.valueOf(1, 1, 1, 2),
                Code.valueOf(1, 1, 2, 2), Code.valueOf(1, 2, 3, 3),
                Code.valueOf(1, 2, 3, 4)));
        replayAll();

        assertThat(testSubject.getCodes().size(), equalTo(5));
        verifyAll();
    }

    @Test
    public void relabeledInput() {
        expect(mockDelegate.getCodes()).andReturn(setOfCodes(
                Code.valueOf(1, 1, 2), Code.valueOf(2, 2, 1)));
        replayAll();

        assertThat(testSubject.getCodes().size(), equalTo(1));
    }

    @Test
    public void reorderedInput() {
        expect(mockDelegate.getCodes()).andReturn(setOfCodes(
                Code.valueOf(1, 1, 2), Code.valueOf(1, 2, 1),
                Code.valueOf(2, 1, 1)));
        replayAll();

        assertThat(testSubject.getCodes().size(), equalTo(1));
    }

}
