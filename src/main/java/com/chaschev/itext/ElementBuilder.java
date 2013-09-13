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

/**
 * User: achaschev
 * Date: 8/29/13
 * Time: 4:09 PM
 */
public abstract class ElementBuilder<T extends Element, BUILDER extends ElementBuilder> {
    protected ITextBuilder b;
    protected T element;

    public ElementBuilder(ITextBuilder b) {
        this.b = b;
    }

    public BUILDER with(T element){
        this.element = element;
        return (BUILDER) this;
    }

    public abstract BUILDER withNew();

    public BUILDER applyStyles(String cssStyleString) {
        if (!cssStyleString.contains(" ")) {
            b.styles.apply(element, cssStyleString);
        } else {
            final String[] styles = AbstractPhraseBuilder.SPACE_PATTERN.split(cssStyleString);

            for (String style : styles) {
                b.styles.apply(element, style);
            }
        }
        return (BUILDER)this;
    }
}
