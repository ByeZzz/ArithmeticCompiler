package Compile;

/**
 * The type Syntax error.
 */
public class SyntaxError extends Exception {

    private int state;

    /**
     * Gets word.
     *
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets word.
     *
     * @param word the word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Gets pos.
     *
     * @return the pos
     */
    public int getPos() {
        return pos;
    }

    /**
     * Sets pos.
     *
     * @param pos the pos
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets msg.
     *
     * @param msg the msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String word;
    private int pos;
    private String msg;

    /**
     * Instantiates a new Syntax error.
     *
     * @param state LR出错的状态
     * @param word  the word
     * @param pos   the pos
     */
    SyntaxError(int state, String word, int pos) {
        this.state = state;
        this.word = word;
        this.pos = pos;
        switch (state) {
            case 0 -> {
                printMsg("表达式期待操作数或），出现预期之外的符号" + word);
            }
            case 1 -> {
                printMsg("表达式期待+、-，出现预期之外的符号" + word);
            }
            case 2, 3, 4 -> {
                printMsg("表达式期待操作符或），出现预料之外的符号" + word);
            }
            case 5, 6, 7, 8, 9 -> {
                printMsg("表达式期待操作数或（，出现预料之外的符号" + word);
            }
            case 10 -> {
                printMsg("表达式期待+,-或)，出现预料之外的符号" + word);
            }
            case 11, 12, 13, 14, 15 -> {
                printMsg("表达式期待运算符或)，出现预料之外的符号" + word);
            }
            default -> {

            }
        }
    }

    /**
     * Print error.
     */
//打印错误
    public void PrintError() {
        switch (state) {
            case 0 -> {
                printMsg("表达式期待操作数或），出现预期之外的符号" + word);
            }
            case 1 -> {
                printMsg("表达式期待+、-，出现预期之外的符号" + word);
            }
            case 2, 3, 4 -> {
                printMsg("表达式期待操作符或），出现预料之外的符号" + word);
            }
            case 5, 6, 7, 8, 9 -> {
                printMsg("表达式期待操作数或（，出现预料之外的符号" + word);
            }
            case 10 -> {
                printMsg("表达式期待+,-或)，出现预料之外的符号" + word);
            }
            case 11, 12, 13, 14, 15 -> {
                printMsg("表达式期待运算符或)，出现预料之外的符号" + word);
            }
            default -> {

            }
        }
    }

    /**
     * Print msg.
     *
     * @param msg the msg
     */
    public void printMsg(String msg) {
        System.out.println(msg);
        this.msg=msg;
        System.out.println("出错的位置为：" + pos);
        System.out.println("出错的符号为：" + word);
    }
}
