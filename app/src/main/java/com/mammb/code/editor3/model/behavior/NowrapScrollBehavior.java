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
package com.mammb.code.editor3.model.behavior;

import com.mammb.code.editor3.model.TextSlice;

/**
 * NowrapScrollBehavior.
 * @author Naotsugu Kobayashi
 */
public class NowrapScrollBehavior implements ScrollBehavior {

    /** The textSlice. */
    private final TextSlice textSlice;

    /**
     * Create a {@link NowrapScrollBehavior}.
     * @param textSlice the {@link TextSlice}
     */
    public NowrapScrollBehavior(TextSlice textSlice) {
        this.textSlice = textSlice;
    }


    @Override
    public int next(int delta) {
        return textSlice.shiftRow(delta);
    }


    @Override
    public int prev(int delta) {
        return textSlice.shiftRow(-delta);
    }

}
