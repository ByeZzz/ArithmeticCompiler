package Compile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * 产生式
 */
class Production{
    public Production(String vt, int length) {
        Vt = vt;
        this.length = length;
    }


    /**
     * 产生式左部
     */
    String Vt;
    /**
     * 产生式右部长度
     */
    int length;
}

public class Compiler {

    /**
     * 非终结符
     */
    private static String Vn[]={
            "+","-","*","/","digit","(",")","#"
    };

    /**
     * 终结符
     */
    private static String Vt[]={
           "E","T","F"
    };

    /**
     * 映射非终结符的哈希表
     */
    private static HashMap<String,Integer> VnMap=new HashMap<>(){
        {
            for(int i=0;i<Vn.length;i++){
                put(Vn[i],i);
            }
        }
    };

    /**
     * 映射终结符的哈希表
     */
    private static HashMap<String,Integer> VtMap=new HashMap<>(){
        {
            for(int i=0;i<Vt.length;i++){
                put(Vt[i],i);
            }
        }
    };

    /**
     * action
     */
    private static String[][] action=
            {
                    {"err","err","err","err","S4","S5","err","err"},
                    {"S6","S7","err","err","S4","S5","err","acc"},
                    {"r3","r3","S8","S9","err","err","r3","r3"},
                    {"r6","r6","r6","r6","err","err","r6","r6"},
                    {"r8","r8","r8","r8","err","err","r8","r8"},
                    {"err","err","err","err","S4","S5","err","err"},
                    {"err","err","err","err","S4","S5","err","err"},
                    {"err","err","err","err","S4","S5","err","err"},
                    {"err","err","err","err","S4","S5","err","err"},
                    {"err","err","err","err","S4","S5","err","err"},
                    {"S6","S7","err","err","err","err","S15","err"},
                    {"r1","r1","S8","S9","err","err","r1","r1"},
                    {"r2","r2","S8","S9","err","err","r2","r2"},
                    {"r4","r4","r4","r4","err","err","r4","r4"},
                    {"r5","r5","r5","r5","err","err","r5","r5"},
                    {"r7","r7","r7","r7","err","err","r7","r7"}
            };

    /**
     * goto
     */
    private static String[][] gotoTable=
            {
                    {"1","2","3"},
                    {"err","err","err"},
                    {"err","err","err"},
                    {"err","err","err"},
                    {"err","err","err"},
                    {"10","2","3"},
                    {"err","11","3"},
                    {"err","12","3"},
                    {"err","err","13"},
                    {"err","err","14"},
                    {"err","err","err"},
                    {"err","err","err"},
                    {"err","err","err"},
                    {"err","err","err"},
                    {"err","err","err"},
                    {"err","err","err"},
            };

    /**
     * 产生式
     */
    private  static Production[] productions;

    /*
    1.E->E(1)+T  {E.code=E(1).code||T.code||+}
    2.E->E(1)-T  {E.code=E(1).code||T.code||-}
    3.E->T  {E.code=T.code}
    4.T->T(1)*F  {T.code=T(1).code||F.code||*}
    5.T->T(1)/F  {T.code=T(1).code||F.code||/}
    6.T->F  {T.code=F.code}
    7.F->(E)  {F.code=E.code}
    8.F->i  {F.code=i}
    */
    static {
        productions=new Production[8];
        productions[0]=new Production("E",3);
        productions[1]=new Production("E",3);
        productions[2]=new Production("E",1);
        productions[3]=new Production("T",3);
        productions[4]=new Production("T",3);
        productions[5]=new Production("T",1);
        productions[6]=new Production("F",3);
        productions[7]=new Production("F",1);

    }



    /**
     * 编译总控程序
     * @param sourceOperational 源字符串
     * @return 逆波兰式栈
     */
    public static Stack Compile(String sourceOperational){
        List<WordSymbol> wordList;
        Stack<String> result=null;
        try {
            //词法分析
           wordList=LexicalAnalyzer(sourceOperational);
           //添加结束符
           wordList.add(new WordSymbol("#", WordKind.END));
            //语法分析
           result=SyntaxAnalyzer(wordList);
        } catch (LexicalError e) {
            //捕获词法异常
            e.PrintError();
        } catch (SyntaxError syntaxError) {
            //捕获语法异常
            syntaxError.PrintError();
        }
        return result;
    }

    /**
     * @param sourceOperational 源字符串
     * @return 单词符号集合
     * @throws LexicalError 词法错误
     */
    public static List<WordSymbol> LexicalAnalyzer(String sourceOperational) throws LexicalError{
        List<WordSymbol> wordList=new ArrayList<>();

        //存取当前字符
        char ch;

        for(int i=0;i<sourceOperational.length();i++){

            //拼接单词符号
            String token="";

            //定位到非空白字符
            if((i=getChar(sourceOperational,i))==-1){
                return wordList;
            }
            ch=sourceOperational.charAt(i);

            //如果为数字1-9
            if(ch>48&&ch<58){
                token+=ch;
                //定位到下一个非空白字符
                i++;
                //到达字符串尾部
                if((i=getChar(sourceOperational,i))==-1){
                    wordList.add(new WordSymbol(token, WordKind.DIGIT));
                    return wordList;
                }
                ch=sourceOperational.charAt(i);
                //如果为数字0-9
                while(ch>47&&ch<58){
                    token+=ch;
                    //定位到下一个非空白字符
                    i++;
                    if((i=getChar(sourceOperational,i))==-1){
                        wordList.add(new WordSymbol(token, WordKind.DIGIT));
                        return wordList;
                    }
                    ch=sourceOperational.charAt(i);
                }
                //为小数点
                if(ch=='.'){
                    token+='.';
                    i++;
                    //如果以小数点结尾，抛出错误
                    if((i=getChar(sourceOperational,i))==-1){
                        throw new LexicalError(sourceOperational.length(),ch,1);
                    }
                    ch=sourceOperational.charAt(i);
                    if(ch>57||ch<48){
                        //小数点后不是数字
                        throw new LexicalError(i+1,ch,1);
                    }

                    while(ch>47&&ch<58){
                        token+=ch;
                        //定位到下一个非空白字符
                        i++;
                        if((i=getChar(sourceOperational,i))==-1){
                            wordList.add(new WordSymbol(token, WordKind.DIGIT));
                            return wordList;
                        }
                        ch=sourceOperational.charAt(i);
                    }

                }
                i--;
                wordList.add(new WordSymbol(token, WordKind.DIGIT));
            }
            //数字0
            else if(ch=='0'){
                token+='0';
                i++;
                if((i=getChar(sourceOperational,i))==-1){
                    wordList.add(new WordSymbol(token, WordKind.DIGIT));
                    return wordList;
                }
                ch=sourceOperational.charAt(i);
                //下一个字符为小数点
                if(ch=='.'){
                    token+='.';
                    i++;
                    if((i=getChar(sourceOperational,i))==-1){
                        throw new LexicalError(sourceOperational.length(),ch,1);
                    }
                    ch=sourceOperational.charAt(i);
                    if(ch>57||ch<47){
                        throw new LexicalError(i+1,ch,1);
                    }

                    while(ch>47&&ch<58){
                        token+=ch;
                        //定位到下一个非空白字符
                        i++;
                        if((i=getChar(sourceOperational,i))==-1){
                            wordList.add(new WordSymbol(token, WordKind.DIGIT));
                            return wordList;
                        }
                        ch=sourceOperational.charAt(i);
                    }
                }
                i--;
                wordList.add(new WordSymbol(token, WordKind.DIGIT));
            }
            //f表示负号，数字为负数
            else if(ch=='f'){
                token+="-";
                i++;
                //负号为结束字符
                if((i=getChar(sourceOperational,i))==-1){
                    throw new LexicalError(sourceOperational.length(),ch,2);
                }
                ch=sourceOperational.charAt(i);

                if(ch>48&&ch<58){
                    token+=ch;
                    //定位到下一个非空白字符
                    i++;
                    if((i=getChar(sourceOperational,i))==-1){
                        wordList.add(new WordSymbol(token, WordKind.DIGIT));
                        return wordList;
                    }
                    ch=sourceOperational.charAt(i);
                    //如果为数字0-9
                    while(ch>47&&ch<58){
                        token+=ch;
                        //定位到下一个非空白字符
                        i++;
                        if((i=getChar(sourceOperational,i))==-1){
                            wordList.add(new WordSymbol(token, WordKind.DIGIT));
                            return wordList;
                        }
                        ch=sourceOperational.charAt(i);
                    }
                    if(ch=='.'){
                        token+='.';
                        i++;
                        if((i=getChar(sourceOperational,i))==-1){
                            //Error(sourceOperational,i,1);
                            //return null;
                            throw new LexicalError(sourceOperational.length(),ch,1);
                        }
                        ch=sourceOperational.charAt(i);
                        if(ch>57||ch<47){
                            //Error(sourceOperational,i,1);
                            throw new LexicalError(i+1,ch,1);
                            //return null;
                        }

                        while(ch>47&&ch<58){
                            token+=ch;
                            //定位到下一个非空白字符
                            i++;
                            if((i=getChar(sourceOperational,i))==-1){
                                wordList.add(new WordSymbol(token, WordKind.DIGIT));
                                return wordList;
                            }
                            ch=sourceOperational.charAt(i);
                        }

                    }
                    i--;
                    wordList.add(new WordSymbol(token, WordKind.DIGIT));
                }
                else if(ch=='0'){
                    token+='0';
                    i++;
                    if((i=getChar(sourceOperational,i))==-1){
                        wordList.add(new WordSymbol(token, WordKind.DIGIT));
                        return wordList;
                    }
                    ch=sourceOperational.charAt(i);
                    if(ch=='.'){
                        token+='.';
                        i++;
                        if((i=getChar(sourceOperational,i))==-1){
                            //Error(sourceOperational,i,1);
                            throw new LexicalError(sourceOperational.length(),ch,1);
                            //return null;
                        }
                        ch=sourceOperational.charAt(i);
                        if(ch>57||ch<47){
                            //Error(sourceOperational,i,1);
                            throw new LexicalError(i+1,ch,1);
                            //return null;
                        }

                        while(ch>47&&ch<58){
                            token+=ch;
                            //定位到下一个非空白字符
                            i++;
                            if((i=getChar(sourceOperational,i))==-1){
                                wordList.add(new WordSymbol(token, WordKind.DIGIT));
                                return wordList;
                            }
                            ch=sourceOperational.charAt(i);
                        }
                    }
                    i--;
                    wordList.add(new WordSymbol(token, WordKind.DIGIT));
                }
                //负号后非数字
                else{
                    throw new LexicalError(i+1,ch,2);
                }

            }
            //运算符与括号
            else{
                switch (ch) {
                    case '+' -> wordList.add(new WordSymbol("+", WordKind.OPERATOR));
                    case '-' -> wordList.add(new WordSymbol("-", WordKind.OPERATOR));
                    case '*' -> wordList.add(new WordSymbol("*", WordKind.OPERATOR));
                    case '/' -> wordList.add(new WordSymbol("/", WordKind.OPERATOR));
                    case '(' -> wordList.add(new WordSymbol("(", WordKind.BRACKET));
                    case ')' -> wordList.add(new WordSymbol(")", WordKind.BRACKET));
                    //其他符号
                    default -> {
                        throw new LexicalError(i+1,ch,0);
                    }
                }
            }
        }
        return wordList;
    }

    /**
     * @param wordList 单词符号集合
     * @return 逆波兰式栈
     * @throws SyntaxError 语法错误
     */
    public static Stack<String> SyntaxAnalyzer(List<WordSymbol> wordList) throws SyntaxError {
        Stack<Integer> stateStack=new Stack<>();
        //初始为0状态
        stateStack.push(0);

        //状态字符串
        String strStack="";

        Stack<String> semanticStack=new Stack<>();

        for(int i=0;i<wordList.size();i++){
            WordSymbol word=wordList.get(i);
            int wordIndex=(word.getWordKind()== WordKind.DIGIT)?VnMap.get("digit"):VnMap.get(word.getWordValue());
            int state=stateStack.peek();

            //查询action
            String nextAction=action[state][wordIndex];

//            //打印状态栈
//            System.out.println(stateStack);

            //获取状态字符串
            strStack+=stateStack.toString()+"\n";

            //跳转状态
            if(nextAction.charAt(0)=='S'){
                //获取序号
                int serialNum=Integer.valueOf(nextAction.substring(1));
                stateStack.push(serialNum);
            }
            //归约
            else if(nextAction.charAt(0)=='r'){
                int serialNum=Integer.valueOf(nextAction.substring(1));

                Production production=productions[serialNum-1];

                for(int j=0;j<production.length;j++){
                    stateStack.pop();
                }

                String nextState=gotoTable[stateStack.peek()][VtMap.get(production.Vt)];

                if(nextState.charAt(0)!='e'){
                    stateStack.push(Integer.valueOf(nextState));
                }
                else{
                    System.out.println("goto Error!");
                }

                //语义操作，拼接逆波兰式
                switch (serialNum){
                    case 1->semanticStack.push("+");
                    case 2->semanticStack.push("-");
                    case 4->semanticStack.push("*");
                    case 5->semanticStack.push("/");
                    case 8->semanticStack.push(wordList.get(i-1).getWordValue());
                }
                //当前扫描字符维持不变
                i--;
            }
            //接受
            else if(nextAction.charAt(0)=='a'){
                System.out.println(semanticStack.toString());
                semanticStack.push(strStack);
                return semanticStack;

            }
            //出错
            else if(nextAction.charAt(0)=='e'){
                throw new SyntaxError(state,word.getWordValue(), i+1);
            }
        }
        //将状态变换添加到语法栈中
        semanticStack.push(strStack);
        return semanticStack;
    }

    /**
     * @param sourceOperational 源字符串
     * @param pos 扫描位置
     * @return 下一个非空字符位置或-1
     */
    private static int getChar(String sourceOperational,int pos){

        //到达字符串尾部
        if(pos>=sourceOperational.length()){
            return -1;
        }

        //寻找到下一个非空字符
        while(sourceOperational.charAt(pos)==' '){
            pos++;
            //到达字符串尾部
            if(pos>=sourceOperational.length()){
                return -1;
            }
        }
        return pos;
    }

    /**
     * 计算逆波兰式
     * @param exp 逆波兰式栈
     * @return 计算结果
     */
    public static double Calculate( Stack<String> exp ){
        Stack<Double> stack=new Stack<>();

        for(String i:exp){
            switch (i){
                //遇到运算符
                case "+"->{
                    //弹栈
                    double op2=stack.pop();
                    double op1=stack.pop();
                    //运算结果入栈
                    stack.push(op1+op2);
                }
                case "-"->{
                    double op2=stack.pop();
                    double op1=stack.pop();
                    stack.push(op1-op2);
                }
                case "*"->{
                    double op2=stack.pop();
                    double op1=stack.pop();
                    stack.push(op1*op2);
                }
                case "/"->{
                    double op2=stack.pop();
                    double op1=stack.pop();
                    stack.push(op1 / op2);
                }

                //数字直接入栈
                default -> {
                    stack.push(Double.valueOf(i));
                }
            }
        }

        //运算结果弹栈返回
        return stack.pop();
    }
}

