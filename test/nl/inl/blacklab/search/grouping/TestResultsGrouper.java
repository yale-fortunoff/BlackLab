/*******************************************************************************
 * Copyright (c) 2010, 2012 Institute for Dutch Lexicology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nl.inl.blacklab.search.grouping;

import java.util.Map;

import junit.framework.Assert;
import nl.inl.blacklab.search.lucene.SpansStub;

import org.apache.lucene.search.spans.Spans;
import org.junit.Test;

public class TestResultsGrouper {
	int[] doc = { 1, 2, 1, 3, 2, 1 };
	int[] start = { 1, 2, 3, 4, 5, 6 };
	int[] end = { 7, 8, 9, 10, 11, 12 };

	@Test
	public void testGrouper() {
		Spans source = new SpansStub(doc, start, end);
		HitProperty crit = new HitPropertyDocumentId();
		ResultsGrouper grouper = new ResultsGrouper(null, source, crit, null);
		Map<String, RandomAccessGroup> groups = grouper.getGroupMap();

		Assert.assertEquals(3, groups.size());
		RandomAccessGroup group1 = groups.get("000000001");
		Assert.assertEquals("000000001", group1.getIdentity());
		Assert.assertEquals(3, group1.size());
		Assert.assertEquals(1, group1.getHits().get(0).doc);
		Assert.assertEquals(1, group1.getHits().get(1).doc);
		Assert.assertEquals(1, group1.getHits().get(2).doc);
		Assert.assertEquals(2, groups.get("000000002").size());
		Assert.assertEquals(1, groups.get("000000003").size());
	}
}