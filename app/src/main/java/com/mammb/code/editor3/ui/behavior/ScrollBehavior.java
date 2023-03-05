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
package com.mammb.code.editor3.ui.behavior;

import com.mammb.code.editor3.model.TextModel;
import com.mammb.code.editor3.ui.Pointing;
import com.mammb.code.editor3.ui.RowsPanel;
import com.mammb.code.editor3.ui.TextFlow;
import com.mammb.code.editor3.ui.util.Texts;

import java.util.Objects;

/**
 * ScrollBehavior.
 * @author Naotsugu Kobayashi
 */
public class ScrollBehavior {

    /** The text model. */
    private final TextModel model;

    /** The text flow pane. */
    private final TextFlow textFlow;

    /** The pointing. */
    private final Pointing pointing;

    /** The rows panel. */
    private final RowsPanel rowsPanel;


    /**
     * Constructor.
     * @param textFlow the text flow pane
     * @param pointing the pointing
     * @param model the text model
     * @param rowsPanel the rows panel
     */
    public ScrollBehavior(TextFlow textFlow, Pointing pointing,
            TextModel model, RowsPanel rowsPanel) {
        this.textFlow = Objects.requireNonNull(textFlow);
        this.pointing = Objects.requireNonNull(pointing);
        this.model = Objects.requireNonNull(model);
        this.rowsPanel = Objects.requireNonNull(rowsPanel);
    }


    /**
     * Scroll next (i.e. arrow down).
     */
    public void scrollNext() {
        if (!model.hasNextSlice() &&
            textFlow.translatedLineOffset() >= textFlow.lineSize() - 1) {
            // Do not scroll anymore, when if the end of row is reached
            // and the minimum number of rows has been reached.
            // Retain the last '1' line of display.
            return;
        }
        if (!model.hasNextSlice() ||
            textFlow.lineSize() - model.capacityOfRows() > textFlow.translatedLineOffset() + 1) {
            // If there are enough lines to read (if the text is wrapped),
            // only the Y-axis coordinate transformation is performed.
            textFlow.translateLineNext();
        } else {
            // Read next rows from the backing model.
            scrollNext(textFlow.translatedShiftRow() + 1);
        }
    }


    /**
     * Scroll prev (i.e. arrow up).
     */
    public void scrollPrev() {
        if (textFlow.translatedLineOffset() > 0) {
            // If row scrolling by Y-axis transformation is possible.
            textFlow.translateLinePrev();
        } else {
            // Read previous rows from the backing model.
            int shiftedOffset = scrollPrev(1);
            if (shiftedOffset > 0) {
                // If previous row was wrapped
                int leadingLine = textFlow.lineSize(0);
                for (int i = 1; i < leadingLine; i++) textFlow.translateLineNext();
            }
        }
    }


    /**
     * Scroll down to the next page.
     */
    public void pageDown() {
        int rows = textFlow.rowSize();
        if (rows == textFlow.lineSize()) {
            // if the text is not wrapped
            scrollNext(rows - 1);
        } else {
            for (int i = 1; i < rows; i++) scrollNext();
        }
    }


    /**
     * Scroll up to the previous page.
     */
    public void pageUp() {
        int rows = textFlow.rowSize();
        if (rows == textFlow.lineSize()) {
            // if the text is not wrapping
            scrollPrev(rows - 1);
        } else {
            for (int i = 1; i < rows; i++) scrollPrev();
        }
    }


    /**
     * Scroll next.
     * @param n the number of row
     */
    private void scrollNext(int n) {
        int shiftedOffset = model.scrollNext(n);
        textFlow.clearTranslation();
        if (shiftedOffset > 0) {
            textFlow.setAll(Texts.asText(model.text()));
            pointing.addOffset(-shiftedOffset);
            rowsPanel.draw(model.originRowIndex());
        }
    }


    /**
     * Scroll previous.
     * @param n the number of row
     * @return shifted offset
     */
    private int scrollPrev(int n) {
        int shiftedOffset = model.scrollPrev(n);
        textFlow.clearTranslation();
        if (shiftedOffset > 0) {
            textFlow.setAll(Texts.asText(model.text()));
            pointing.addOffset(shiftedOffset);
            rowsPanel.draw(model.originRowIndex());
        }
        return shiftedOffset;
    }

}
