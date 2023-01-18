package com.mammb.code.syntax;

import javafx.scene.paint.Paint;

import java.util.List;

public interface Highlighter {

    record PaintText(String text, Paint paint) { }

    List<PaintText> apply(int line, String text);

    void invalidAfter(int line);

    static Highlighter of(String name) {
        return switch (name) {
            case ".java" -> new Java();
            case ".md"   -> new Markdown();
            default      -> new PassThrough();
        };
    }

}
