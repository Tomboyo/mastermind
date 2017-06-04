package edu.vwc.mastermind.sequence.provider;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;

public class AllCodesProviderFactoryTest {

	@Test
	public void testGetInstance() {
		AllCodesProviderFactory factory = new AllCodesProviderFactory(3, 2);
		
		/*
		 * The provider given no blacklist will return all the codes.
		 */
		assertEquals(TestUtil.canonicalSet(), 
				factory.getInstance(Collections.emptySet(),
						Collections.emptySet()).getCodes());
		
		/*
		 * The provider will exclude blacklisted elements from the sets it returns
		 */
		Set<Code> blackList = new LinkedHashSet<Code>();
		blackList.add(Code.valueOf(0, 0));
		Set<Code> filteredSet = TestUtil.canonicalSet();
		filteredSet.removeAll(blackList);
		assertEquals(filteredSet, factory.getInstance(blackList,
				Collections.emptySet()).getCodes());
	}

}
