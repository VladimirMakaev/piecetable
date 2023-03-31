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
package com.mammb.code.editor3.syntax.type;

import com.mammb.code.editor3.syntax.Lexer;
import com.mammb.code.editor3.syntax.LexerSource;
import com.mammb.code.editor3.syntax.Token;
import com.mammb.code.editor3.syntax.TokenType;

/**
 * PassThrough Lexer.
 * @author Naotsugu Kobayashi
 */
public class PassThroughLexer implements Lexer {

    /** The input string. */
    private LexerSource source;

    /**
     * Constructor.
     * @param source the {@link LexerSource}
     */
    private PassThroughLexer(LexerSource source) {
        this.source = source;
    }


    /**
     * Create a new lexer.
     * @param source the {@link LexerSource}
     * @return a lexer
     */
    public static Lexer of(LexerSource source) {
        return new PassThroughLexer(source);
    }


    @Override
    public Token nextToken() {
        return new Token(TokenType.ANY.ordinal(), 0, source.length());
    }

}
