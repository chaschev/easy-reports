/*
 * Copyright (C) 2013 Afoundria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chaschev.itext;

import com.itextpdf.text.Element;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: achaschev
 * Date: 8/29/13
 * Time: 3:08 PM
 */
public class StyleRegister {
    Map<String, Style> styles = new HashMap<String, Style>();
    ITextBuilder b;

    public StyleRegister(ITextBuilder b) {
        this.b = b;

        add("normal", new StyleFunction() {
            public void apply(ChunkBuilder c) {

            }
        });
    }

    public StyleRegister add(String cssStyleString, StyleFunction styleFunction){
        final String[] styles = PhraseBuilder.SPACE_PATTERN.split(cssStyleString);

        styleFunction.b = b;

        for (String name : styles) {
            Style style = getStyle(name);
            style.styleFunctions.add(styleFunction);
        }

        return this;
    }

    private Style getStyle(String name) {
        Style r = styles.get(name);

        if(r == null){
            styles.put(name, r = new Style(name));
        }

        return r;
    }

    public StyleRegister apply(Element el, @Nullable String styleName) {
        if(styleName == null){
            styleName = "normal";
        }

        final Style style = styles.get(styleName);

        if(style.isEmpty()){
            throw new IllegalStateException("could not find style: " + styleName);
        }

        style.apply(el);

        return this;
    }
}
