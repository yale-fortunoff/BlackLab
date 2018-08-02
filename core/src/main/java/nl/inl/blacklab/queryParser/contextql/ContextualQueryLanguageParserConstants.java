/* Generated By:JavaCC: Do not edit this line. ContextualQueryLanguageParserConstants.java */
package nl.inl.blacklab.queryParser.contextql;

/**
 * Token literal values and constants. Generated by
 * org.javacc.parser.OtherFilesGen#start()
 */
public interface ContextualQueryLanguageParserConstants {

    /** End of File. */
    int EOF = 0;
    /** RegularExpression Id. */
    int SINGLE_LINE_COMMENT = 5;
    /** RegularExpression Id. */
    int MULTI_LINE_COMMENT = 6;
    /** RegularExpression Id. */
    int AND = 7;
    /** RegularExpression Id. */
    int OR = 8;
    /** RegularExpression Id. */
    int NOT = 9;
    /** RegularExpression Id. */
    int PROX = 10;
    /** RegularExpression Id. */
    int IDENTIFIER = 11;
    /** RegularExpression Id. */
    int STRING = 12;

    /** Lexical state. */
    int DEFAULT = 0;

    /** Literal token values. */
    String[] tokenImage = {
            "<EOF>",
            "\" \"",
            "\"\\t\"",
            "\"\\n\"",
            "\"\\r\"",
            "<SINGLE_LINE_COMMENT>",
            "<MULTI_LINE_COMMENT>",
            "\"and\"",
            "\"or\"",
            "\"not\"",
            "\"prox\"",
            "<IDENTIFIER>",
            "<STRING>",
            "\">\"",
            "\"=\"",
            "\"(\"",
            "\")\"",
            "\"<\"",
            "\">=\"",
            "\"<=\"",
            "\"<>\"",
            "\"/\"",
    };

}
