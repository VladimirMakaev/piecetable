package com.mammb.code.syntax;

import com.mammb.code.editor.Colors;
import com.mammb.code.trie.Range;
import com.mammb.code.trie.Trie;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class Java implements Highlighter {

    private static final Trie trie = new Trie();
    static {
        Stream.of("""
        abstract,continue,for,new,switch,assert,default,goto,package,synchronized,boolean,do,if,private,
        this,break,double,implements,protected,throw,byte,else,import,public,throws,case,enum,instanceof,
        return,transient,catch,extends,int,short,try,char,final,interface,static,void,class,finally,long,
        strictfp,volatile,const,float,native,super,while,var,record,sealed,with,yield,to,transitive,uses"""
            .split("[,\\s]")).forEach(trie::put);
    }
    private MarkTree marks = new MarkTree();

    @Override
    public List<PaintText> apply(int line, String text) {
        return applyBlockComment(line, text);
    }


    @Override
    public void invalidAfter(int line) { marks.sluice(line); }


    private List<PaintText> applyBlockComment(int line, String text) {

        final String name = "blockComment";

        int start = text.indexOf("/*");
        int end = text.indexOf("*/");
        if (line > marks.highWaterMark()) {
            if (start > -1) marks.addStart(line, start, name, 2);
            if (end > -1) marks.addEnd(line, end, name, 2);
        }

        List<PaintText> list = new ArrayList<>();
        boolean startInScope = marks.isInScope(line, 0, name);
        if (start < 0 && end < 0 && startInScope) {
            list.add(new PaintText(text, Colors.blockCommentColor));
            return list;
        }

        boolean prevInScope = startInScope;
        int offset = 0;
        for (int i = 0; i < text.length(); i++) {
            boolean scope = marks.isInScope(line, i, name);
            if (prevInScope != scope) {
                if (text.substring(i).isBlank()) {
                    i = text.length(); // optimize
                }
                String str = text.substring(offset, i);
                if (prevInScope) {
                    list.add(new PaintText(str, Colors.blockCommentColor));
                } else {
                    list.addAll(applyLineComment(str));
                }
                offset = i;
            }
            prevInScope = scope;
        }

        if (offset < text.length()) {
            if (prevInScope) {
                list.add(new PaintText(text.substring(offset), Colors.blockCommentColor));
            } else {
                list.addAll(applyLineComment(text.substring(offset)));
            }
        }
        return list;
    }


    private List<PaintText> applyLineComment(String text) {
        List<PaintText> list = new ArrayList<>();
        int lineCommentIndex = text.indexOf("//");
        if (lineCommentIndex >= 0) {
            list.addAll(applyStringLiteral(text.substring(0, lineCommentIndex)));
            list.add(new PaintText(text.substring(lineCommentIndex), Colors.lineCommentColor));
        } else {
            list.addAll(applyStringLiteral(text));
        }
        return list;
    }

    private List<PaintText> applyStringLiteral(String text) {
        List<PaintText> list = new ArrayList<>();
        int start = text.indexOf('"');
        if (start > -1 && start != text.length() - 1) {
            int end = start;
            while(true) {
                if (end + 1 > text.length() - 1) {
                    end = -1;
                    break;
                }
                end = text.indexOf('"', end + 1);
                if (end < 0) break;
                if ('\\' != text.charAt(end - 1))  break;
            }
            if (start < end) {
                if (start > 0) {
                    list.addAll(applyKeyword(text.substring(0, start)));
                }
                list.add(new PaintText(text.substring(start, end + 1), Colors.blockCommentColor));
                if (end < text.length() - 1) {
                    list.addAll(applyStringLiteral(text.substring(end + 1)));
                }
                return list;
            }
        }
        return applyKeyword(text);
    }

    private List<PaintText> applyKeyword(String text) {
        List<PaintText> list = new ArrayList<>();
        int offset = 0;
        for (Range range : trie.matchWords(text)) {
            if (offset != range.start()) {
                list.add(new PaintText(text.substring(offset, range.start()), Colors.fgColor));
            }
            list.add(new PaintText(text.substring(range.start(), range.endExclusive()), Colors.kwColor));
            offset = range.endExclusive();
        }
        if (offset < text.length()) {
            list.add(new PaintText(text.substring(offset), Colors.fgColor));
        }
        return list;
    }

}
