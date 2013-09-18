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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.HyphenationEvent;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
* User: chaschev
* Date: 6/21/13
*/
public abstract class AbstractPhraseBuilder<T extends Phrase, BUILDER extends AbstractPhraseBuilder>  extends ElementBuilder<T, BUILDER> {
    public static final Pattern SPACE_PATTERN = Pattern.compile("\\s+");

    public AbstractPhraseBuilder(ITextBuilder b) {
        super(b);
    }

    public boolean trim() {
        return element.trim();
    }

    public boolean process(ElementListener listener) {
        return element.process(listener);
    }

    public int type() {
        return element.type();
    }

    public List<Chunk> getChunks() {
        return element.getChunks();
    }

    public boolean isContent() {
        return element.isContent();
    }

    public boolean isNestable() {
        return element.isNestable();
    }

    public void add(int index, Element element) {
        this.element.add(index, element);
    }

    public boolean add(String s) {
        return element.add(s);
    }

    public BUILDER add(Element element) {
        this.element.add(element);
        return (BUILDER) this;
    }

    public boolean addAll(Collection<? extends Element> collection) {
        return element.addAll(collection);
    }

    public BUILDER setLeading(float leading) {
        element.setLeading(leading);
        return (BUILDER) this;
    }

    public BUILDER setFont(Font font) {
        element.setFont(font);
        return (BUILDER) this;
    }

    public float getLeading() {
        return element.getLeading();
    }

    public float getTotalLeading() {
        return element.getTotalLeading();
    }

    public boolean hasLeading() {
        return element.hasLeading();
    }

    public Font getFont() {
        return element.getFont();
    }

    public String getContent() {
        return element.getContent();
    }

    public boolean isEmpty() {
        return element.isEmpty();
    }

    public HyphenationEvent getHyphenation() {
        return element.getHyphenation();
    }

    public BUILDER setHyphenation(HyphenationEvent hyphenation) {
        element.setHyphenation(hyphenation);
        return (BUILDER) this;
    }

    public TabSettings getTabSettings() {
        return element.getTabSettings();
    }

    public void setTabSettings(TabSettings tabSettings) {
        element.setTabSettings(tabSettings);
    }

    public T build(){
        return element;
    }
}
