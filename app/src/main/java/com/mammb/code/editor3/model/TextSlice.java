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

public class TextSlice {

    /** The origin row number. */
    private int originRow;

    /** The slice row size. */
    private int rowSize;

    /** The text source. */
    private final TextSource source;

    /** The text slice buffer. */
    private final StringBuffer buffer = new StringBuffer();


    public TextSlice(TextSource source) {
        this.source = source;
        this.originRow = 0;
        this.rowSize = 10;
    }

    public String string() {
        return buffer.toString();
    }


    public void refresh() {
        buffer.replace(0, buffer.length(), source.rows(rowSize));
    }

}
