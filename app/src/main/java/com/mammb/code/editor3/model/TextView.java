/*
 * Copyright 2019-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mammb.code.editor3.model;

import com.mammb.code.editor2.model.LinePoint;
import com.mammb.code.editor3.model.specifics.ContentImpl;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.util.List;

/**
 * TextView.
 * @author Naotsugu Kobayashi
 */
public class TextView {

    private final TextSlice textSlice;

    /** The caret point. */
    private final LinePoint caretPoint = new LinePoint();

    /** max rows. */
    private int maxRows;

    /** text wrap?. */
    private boolean textWrap;

    /** dirty?. */
    private boolean dirty;

    /** The scroll handler. */
    private ScrollHandler scrollHandler;

    /** The caret handler. */
    private CaretHandler caretHandler;


    /**
     * Constructor.
     * @param textSlice the text slice
     */
    public TextView(TextSlice textSlice) {
        this.textSlice = textSlice;
    }


    /**
     * Create text view.
     * @param path the content path
     */
    public TextView(Path path) {
        TextSource source = new TextSource(new ContentImpl(path));
        this.textSlice = new TextSlice(source);
    }

    public List<Text> text() {
        return List.of(new Text(textSlice.string()));
    }


    /**
     * Set the max row number.
     * @param maxRows the max row number
     */
    public void setupMaxRows(int maxRows) {
        if (maxRows > 1 && this.maxRows != maxRows) {
            this.maxRows = maxRows;
            textSlice.refresh();
        }
    }


    /**
     * Gets whether this text is dirty.
     * @return {@code true} if text is dirty.
     */
    public boolean isDirty() { return dirty; }

}
