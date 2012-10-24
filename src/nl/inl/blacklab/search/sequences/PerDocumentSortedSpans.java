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
package nl.inl.blacklab.search.sequences;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import nl.inl.blacklab.search.Hit;

import org.apache.lucene.search.spans.Spans;

/**
 * Sort the given Spans per document, according to the given comparator.
 */
public class PerDocumentSortedSpans extends Spans {
	private Hit currentHit;

	private SpansInBuckets bucketedSpans;

	private boolean eliminateDuplicates;

	private Hit previousHit = null;

	private List<Hit> hitsInBucket;

	private Iterator<Hit> hitIterator = null;

	public PerDocumentSortedSpans(Spans src, Comparator<Hit> comparator, boolean eliminateDuplicates) {
		// Wrap a HitsPerDocument and show it to the client as a normal, sequential Spans.
		bucketedSpans = new SpansInBucketsPerDocumentSorted(src, comparator);

		this.eliminateDuplicates = eliminateDuplicates;
	}

	public PerDocumentSortedSpans(Spans src, Comparator<Hit> comparator) {
		this(src, comparator, false);
	}

	@Override
	public int doc() {
		return bucketedSpans.doc();
	}

	@Override
	public int start() {
		return currentHit.start;
	}

	@Override
	public int end() {
		return currentHit.end;
	}

	@Override
	public boolean next() throws IOException {
		do {
			if (hitIterator == null || !hitIterator.hasNext()) {
				if (!bucketedSpans.next())
					return false;
				previousHit = null;
				hitsInBucket = bucketedSpans.getHits();
				hitIterator = hitsInBucket.iterator();
			}
			previousHit = currentHit;
			currentHit = hitIterator.next();
		} while (eliminateDuplicates && (previousHit != null && currentHit.equals(previousHit)));
		return true;
	}

	@Override
	public boolean skipTo(int target) throws IOException {
		int oldDoc = bucketedSpans.doc();
		if (!bucketedSpans.skipTo(target))
			return false;
		if (oldDoc != bucketedSpans.doc())
			previousHit = null;
		hitsInBucket = bucketedSpans.getHits();
		hitIterator = hitsInBucket.iterator();
		return next();
	}

	@Override
	public Collection<byte[]> getPayload() {
		// not used
		return null;
	}

	@Override
	public boolean isPayloadAvailable() {
		// not used
		return false;
	}

	@Override
	public String toString() {
		return bucketedSpans.toString();
	}

}