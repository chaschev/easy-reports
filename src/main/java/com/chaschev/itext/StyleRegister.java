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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.itextpdf.text.Element;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * User: achaschev
 * Date: 8/29/13
 * Time: 3:08 PM
 */
public class StyleRegister {
    Multimap<String, Style> styles = HashMultimap.create();
    ITextBuilder b;

    public StyleRegister(ITextBuilder b) {
        this.b = b;

        add(new Style("normal") {
            @Override
            public void applyToChunk(ChunkBuilder c) {

            }
        });
    }

    public StyleRegister add(Style style){
        styles.put(style.name, style);
        style.b = b;
        return this;
    }

    public StyleRegister apply(Element el, @Nullable java.lang.String style) {
        if(style == null){
            style = "normal";
        }

        final Collection<Style> stylesCollection = styles.get(style);

        if(stylesCollection.isEmpty()){
            throw new IllegalStateException("could not find style: " + style);
        }

        for (Style s : stylesCollection) {
            s.apply(el);
        }

        return this;
    }
}
