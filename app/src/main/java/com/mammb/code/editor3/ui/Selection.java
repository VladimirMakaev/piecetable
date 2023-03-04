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
package com.mammb.code.editor3.ui;

import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

/**
 * Selection.
 * @author Naotsugu Kobayashi
 */
public class Selection extends Path {

    /** The TextFlow. */
    private final TextFlow textFlow;

    /** The selection open offset. */
    private int openOffset = -1;

    /** The selection close offset. */
    private int closeOffset = -1;

    /** The selection on. */
    private boolean on = false;

    /** The selection dragging. */
    private boolean dragging = false;


    /**
     * Constructor.
     * @param textFlow the TextFlow
     */
    public Selection(TextFlow textFlow) {

        this.textFlow = textFlow;

        setStrokeWidth(0);
        setOpacity(0.4);
        setFill(Color.DODGERBLUE);
        setBlendMode(BlendMode.LIGHTEN);
        setManaged(false);

    }


    /**
     * Start selection.
     * @param offset the start position
     */
    public void start(int offset) {
        openOffset = closeOffset = offset;
        on = true;
        dragging = false;
    }


    /**
     * Reset this selection.
     */
    public void reset() {
        openOffset = -1;
        closeOffset = -1;
        on = false;
        dragging = false;
    }


    /**
     * Start drag selection.
     * @param offset the start position
     */
    public void startDragging(int offset) {
        start(offset);
        dragging = true;
    }


    /**
     * Release dragging.
     */
    public void releaseDragging() {
        dragging = false;
    }


    /**
     * Get whether the selection is dragging.
     * @return {@code true}, if the selection is dragging
     */
    public boolean isDragging() {
        return on && dragging;
    }


    /**
     * Shift the current offset.
     * @param delta the number of offsets to shift
     */
    public void shiftOffset(int delta) {
        if (on) {
            openOffset += delta;
            closeOffset += delta;
        }
    }


    /**
     * Get the selection open offset.
     * @return the selection open offset
     */
    public int openOffset() { return openOffset; }


    /**
     * The selection close offset.
     * @return the selection close offset
     */
    public int closeOffset() { return closeOffset; }

}
