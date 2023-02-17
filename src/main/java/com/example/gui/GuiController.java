package com.example.gui;

import Compile.*;
import Compile.Compiler;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class GuiController implements Initializable {

    public JFXTextArea sourceExpression;
    public JFXTextArea resultExpression;
    public JFXTextArea resultValue;
    public JFXTextArea phraseStack;
    public JFXTextArea grammarStack;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sourceExpression.setText("");
        resultExpression.setText("");
        resultValue.setText("");
        phraseStack.setText("");
        grammarStack.setText("");
        sourceExpression.textProperty().addListener((observable, oldText, newText) -> updateText(newText));
    }

    public void updateText(String str) {
        try {
            //获取词法分析结果
            List<WordSymbol> wordList=Compiler.LexicalAnalyzer(str);

            String pStr="";

            for(WordSymbol i:wordList){
              String strTemp="{"+i.getWordKind()+":"+i.getWordValue()+"}"+"\n";
              pStr+=strTemp;
            }

            //显示
            phraseStack.setText(pStr);

            //添加结束符
            wordList.add(new WordSymbol("#", WordKind.END));

            //获取语法分析结果
            Stack<String> resultExp=Compiler.SyntaxAnalyzer(wordList);

            //状态栈
            grammarStack.setText(resultExp.pop());

            //逆波兰式
            resultExpression.setText(resultExp.toString());

            //计算逆波兰式
            resultValue.setText(String.valueOf(Compiler.Calculate(resultExp)));

        } catch (LexicalError e) {
            //捕获到词法错误
            phraseStack.setText("Error："+e.getMsg()+"\n"+"出错字符："+e.getWord()+"\n"+"出错位置："+e.getPos());
            resultValue.setText("Error!");
            resultExpression.setText("Error!");
            grammarStack.setText("Error!");
        }catch (SyntaxError syntaxError) {
            //捕获到语法错误
            grammarStack.setText("Error："+syntaxError.getMsg()+"\n"+"出错单词符号："+syntaxError.getWord()+"\n"+"出错单词符号位置："+syntaxError.getPos());
            resultValue.setText("Error!");
            resultExpression.setText("Error!");
            //phraseStack.setText("Error!");
        }

    }
}