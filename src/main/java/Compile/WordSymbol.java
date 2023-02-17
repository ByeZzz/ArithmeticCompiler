package Compile;

/**
 * The type Word symbol.
 */
public class WordSymbol {
    private String wordValue;//单词符号的值
    private WordKind wordKind;//单词符号的类型

    /**
     * Instantiates a new Word symbol.
     *
     * @param wordValue the word value
     * @param wordKind  the word kind
     */
    public WordSymbol(String wordValue, WordKind wordKind) {
        this.wordValue = wordValue;
        this.wordKind = wordKind;
    }

    /**
     * Gets word value.
     *
     * @return the word value
     */
    public String getWordValue() {
        return wordValue;
    }

    /**
     * Sets word value.
     *
     * @param wordValue the word value
     */
    public void setWordValue(String wordValue) {
        this.wordValue = wordValue;
    }

    /**
     * Gets word kind.
     *
     * @return the word kind
     */
    public WordKind getWordKind() {
        return wordKind;
    }

    /**
     * Sets word kind.
     *
     * @param wordKind the word kind
     */
    public void setWordKind(WordKind wordKind) {
        this.wordKind = wordKind;
    }
}
