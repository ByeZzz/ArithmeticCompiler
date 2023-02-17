package Compile;

public class LexicalError extends Exception {
    //出错位置
    private int pos;
    //出错符号
    private char word;
    //出错类型
    private int type;
    //出错信息
    private String msg;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public char getWord() {
        return word;
    }

    public void setWord(char word) {
        this.word = word;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LexicalError(int pos, char word, int type) {
        this.pos = pos;
        this.word = word;
        this.type = type;
        if (type == 0) {
            //System.out.println("发现预期之外的符号：" + word);
            msg="发现预期之外的符号：" + word;
        } else if (type == 1) {
            //System.out.println("小数点.期待数字");
            msg="小数点.期待数字";
        } else if (type == 2) {
            //System.out.println("负号f期待数字");
            msg="负号f期待数字";
        }
    }

    //打印错误
    public void PrintError() {

        System.out.println("出错信息："+msg);
        System.out.println("出错为位置为：" + pos);
        System.out.println("出错为符号为：" + word);
    }
}
