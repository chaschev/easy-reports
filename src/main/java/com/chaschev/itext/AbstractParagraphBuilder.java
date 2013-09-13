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
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

import java.util.*;

/**
* User: chaschev
* Date: 9/13/13
*/
public abstract class AbstractParagraphBuilder<T extends Paragraph, BUILDER extends AbstractParagraphBuilder> extends AbstractPhraseBuilder<T, BUILDER>{
    public AbstractParagraphBuilder(ITextBuilder b) {
        super(b);
    }

    public Paragraph cloneShallow(boolean spacingBefore) {
        return element.cloneShallow(spacingBefore);
    }

    public float getIndentationLeft() {
        return element.getIndentationLeft();
    }

    public int getAlignment() {
        return element.getAlignment();
    }

    public void setRole(PdfName role) {
        element.setRole(role);
    }

    public UUID getId() {
        return element.getId();
    }

    public float getSpacingBefore() {
        return element.getSpacingBefore();
    }

    public List<Element> breakUp() {
        return element.breakUp();
    }

    public boolean getKeepTogether() {
        return element.getKeepTogether();
    }

    public PdfName getRole() {
        return element.getRole();
    }

    public Element set(int index, Element element) {
        return this.element.set(index, element);
    }

    public void setKeepTogether(boolean keeptogether) {
        element.setKeepTogether(keeptogether);
    }

    public ListIterator<Element> listIterator() {
        return element.listIterator();
    }

    public Object[] toArray() {
        return element.toArray();
    }

    public void clear() {
        element.clear();
    }

    public float getExtraParagraphSpace() {
        return element.getExtraParagraphSpace();
    }

    public static Phrase getInstance(int leading, String string, Font font) {
        return Phrase.getInstance(leading, string, font);
    }

    public <T> T[] toArray(T[] a) {
        return element.toArray(a);
    }

    public boolean retainAll(Collection<?> c) {
        return element.retainAll(c);
    }

    public static Phrase getInstance(int leading, String string) {
        return Phrase.getInstance(leading, string);
    }

    public void setSpacingBefore(float spacing) {
        element.setSpacingBefore(spacing);
    }

    @Deprecated
    public float spacingBefore() {
        return element.spacingBefore();
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        element.setAccessibleAttribute(key, value);
    }

    public float getMultipliedLeading() {
        return element.getMultipliedLeading();
    }

    public static Phrase getInstance(String string) {
        return Phrase.getInstance(string);
    }

    public void trimToSize() {
        element.trimToSize();
    }

    @Deprecated
    public float spacingAfter() {
        return element.spacingAfter();
    }

    public void setIndentationRight(float indentation) {
        element.setIndentationRight(indentation);
    }

    public boolean remove(Object o) {
        return element.remove(o);
    }

    public void setIndentationLeft(float indentation) {
        element.setIndentationLeft(indentation);
    }

    public float getIndentationRight() {
        return element.getIndentationRight();
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        return element.getAccessibleAttribute(key);
    }

    public void setLeading(float fixedLeading, float multipliedLeading) {
        element.setLeading(fixedLeading, multipliedLeading);
    }

    public Iterator<Element> iterator() {
        return element.iterator();
    }

    public void setFirstLineIndent(float firstLineIndent) {
        element.setFirstLineIndent(firstLineIndent);
    }

    public float getSpacingAfter() {
        return element.getSpacingAfter();
    }

    public void setAlignment(int alignment) {
        element.setAlignment(alignment);
    }

    public int indexOf(Object o) {
        return element.indexOf(o);
    }

    public void setExtraParagraphSpace(float extraParagraphSpace) {
        element.setExtraParagraphSpace(extraParagraphSpace);
    }

    public boolean removeAll(Collection<?> c) {
        return element.removeAll(c);
    }

    public Element get(int index) {
        return element.get(index);
    }

    public float getFirstLineIndent() {
        return element.getFirstLineIndent();
    }

    public boolean contains(Object o) {
        return element.contains(o);
    }

    public boolean addAll(int index, Collection<? extends Element> c) {
        return element.addAll(index, c);
    }

    public ListIterator<Element> listIterator(int index) {
        return element.listIterator(index);
    }

    public void setId(UUID id) {
        element.setId(id);
    }

    public void ensureCapacity(int minCapacity) {
        element.ensureCapacity(minCapacity);
    }

    public boolean containsAll(Collection<?> c) {
        return element.containsAll(c);
    }

    public void setSpacingAfter(float spacing) {
        element.setSpacingAfter(spacing);
    }

    public Element remove(int index) {
        return element.remove(index);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return element.getAccessibleAttributes();
    }

    public List<Element> subList(int fromIndex, int toIndex) {
        return element.subList(fromIndex, toIndex);
    }

    public int size() {
        return element.size();
    }

    public int lastIndexOf(Object o) {
        return element.lastIndexOf(o);
    }

    @Override
    public float getTotalLeading() {
        return element.getTotalLeading();
    }

    @Override
    public BUILDER setLeading(float fixedLeading) {
        element.setLeading(fixedLeading);
        return (BUILDER) this;
    }

    public void setMultipliedLeading(float multipliedLeading) {
        element.setMultipliedLeading(multipliedLeading);
    }

    @Override
    public BUILDER add(Element o) {
        element.add(o);
        return (BUILDER) this;
    }

    @Override
    public int type() {
        return element.type();
    }
}
