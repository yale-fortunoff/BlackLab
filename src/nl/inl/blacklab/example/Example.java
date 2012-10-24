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
package nl.inl.blacklab.example;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import nl.inl.blacklab.index.Indexer;
import nl.inl.blacklab.queryParser.corpusql.CorpusQueryLanguageParser;
import nl.inl.blacklab.queryParser.corpusql.ParseException;
import nl.inl.blacklab.search.Hit;
import nl.inl.blacklab.search.Hits;
import nl.inl.blacklab.search.Searcher;
import nl.inl.blacklab.search.TextPattern;
import nl.inl.util.XmlUtil;

/**
 * Simple test program to demonstrate index & search functionality.
 */
public class Example {

	/**
	 * The BlackLab searcher object.
	 */
	static Searcher searcher;

	/**
	 * Some test XML data to index.
	 */
	static String[] testData = {
			"<doc>" + "<w l='the'   p='art' >The</w> " + "<w l='quick' p='adj'>quick</w> "
					+ "<w l='brown' p='adj'>brown</w> " + "<w l='fox'   p='nou'>fox</w> "
					+ "<w l='jump'  p='vrb' >jumps</w> " + "<w l='over'  p='pre' >over</w> "
					+ "<w l='the'   p='art' >the</w> " + "<w l='lazy'  p='adj'>lazy</w> "
					+ "<w l='dog'   p='nou'>dog</w>" + ".</doc>",

			"<doc> " + "<w l='may' p='vrb'>May</w> " + "<w l='the' p='art'>the</w> "
					+ "<w l='force' p='nou'>force</w> " + "<w l='be' p='vrb'>be</w> "
					+ "<w l='with' p='pre'>with</w> " + "<w l='you' p='pro'>you</w>" + ".</doc>", };

	public static void main(String[] args) throws IOException {

		// Get a temporary directory for our test index
		File indexDir = new File(System.getProperty("java.io.tmpdir"), "BlackLabExample");

		// Instantiate the BlackLab indexer, supplying our DocIndexer class
		Indexer indexer = new Indexer(indexDir, true, DocIndexerXmlExample.class);
		try {

			// Index each of our test "documents".
			for (int i = 0; i < testData.length; i++) {
				indexer.index("test" + (i + 1), new StringReader(testData[i]));
			}

		} catch (Exception e) {

			// An error occurred during indexing.
			System.err.println("An error occurred, aborting indexing. Error details follow.");
			e.printStackTrace();

		} finally {

			// Finalize and close the index.
			indexer.close();

		}

		// Create the BlackLab searcher object
		searcher = new Searcher(indexDir);
		try {

			// Find the word "the"
			System.out.println("-----");
			findPattern(parseCorpusQL(" 'the' "));

			// Find prepositions
			System.out.println("-----");
			findPattern(parseCorpusQL(" [pos='pre'] "));

			// Find sequence of words
			System.out.println("-----");
			findPattern(parseCorpusQL(" 'the' []{0,2} 'fo.*' "));

		} catch (ParseException e) {

			// Query parse error
			System.err.println(e.getMessage());

		} finally {

			// Close the searcher object
			searcher.close();

		}
	}

	/**
	 * Parse a Corpus Query Language query
	 *
	 * @param query
	 *            the query to parse
	 * @return the resulting BlackLab text pattern
	 * @throws ParseException
	 */
	private static TextPattern parseCorpusQL(String query) throws ParseException {

		// A bit of cheating here - CorpusQL only allows double-quoting, but
		// that makes our example code look ugly (we have to add backslashes).
		// We may extend CorpusQL to allow single-quoting in the future.
		query = query.replaceAll("'", "\"");

		// Instantiate the CorpusQL parser
		CorpusQueryLanguageParser parser = new CorpusQueryLanguageParser(new StringReader(query));

		// Parse the query
		return parser.query();
	}

	/**
	 * Find a text pattern in the contents field and display the matches.
	 *
	 * @param tp
	 *            the text pattern to search for
	 */
	static void findPattern(TextPattern tp) {
		// Execute the search
		Hits hits = searcher.find("contents", tp);

		// Display the concordances
		displayConcordances(hits);
	}

	/**
	 * Display a list of hits.
	 *
	 * @param hits
	 *            the hits to display
	 */
	static void displayConcordances(Hits hits) {
		// Find concordances (pieces of the original content surrounding the match),
		// so we can display them.
		hits.findConcordances();

		// Loop over the hits and display.
		for (Hit hit : hits) {
			// Strip out XML tags for display.
			String left = XmlUtil.xmlToPlainText(hit.conc[0]);
			String hitText = XmlUtil.xmlToPlainText(hit.conc[1]);
			String right = XmlUtil.xmlToPlainText(hit.conc[2]);

			System.out.printf("[%05d:%06d] %45s[%s]%s\n", hit.doc, hit.start, left, hitText, right);
		}
	}

}