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
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: chaschev
 * Date: 9/13/13
 */
public class ListBuilder {
    ITextBuilder b;
    List list;

    final Item itemBuilder;

    public ListBuilder(ITextBuilder b) {
        this.b = b;
        itemBuilder = new Item(b, this);
    }

    public Item newItem(){
        return itemBuilder.withNew().applyStyles("normal");
    }

    public Item newItem(String cssStyleString){
        return itemBuilder.withNew().applyStyles(cssStyleString);
    }

    public List get() {
        return list;
    }

    public ListBuilder withNew(){
        list = new List();
        return this;
    }

    public static class Item  extends AbstractParagraphBuilder<ListItem, Item>{
        private final ListBuilder listBuilder;

        public Item(ITextBuilder b, ListBuilder listBuilder) {
            super(b);
            this.listBuilder = listBuilder;
        }

        public Item withNew(){
            element = new ListItem();
            return this;
        }

        public Item setListSymbol(Chunk symbol) {
            element.setListSymbol(symbol);
            return this;
        }

        public Item setIndentationLeft(float indentation, boolean autoindent) {
            element.setIndentationLeft(indentation, autoindent);
            return this;
        }

        public Item adjustListSymbolFont() {
            element.adjustListSymbolFont();
            return this;
        }

        public Chunk getListSymbol() {
            return element.getListSymbol();
        }

        public ListBody getListBody() {
            return element.getListBody();
        }

        public ListLabel getListLabel() {
            return element.getListLabel();
        }

        public ListBuilder addToList() {
            return listBuilder.add(element);
        }
    }

    public boolean process(ElementListener listener) {
        return list.process(listener);
    }

    public boolean isAutoindent() {
        return list.isAutoindent();
    }

    public void setListSymbol(String symbol) {
        list.setListSymbol(symbol);
    }

    public java.util.List<Chunk> getChunks() {
        return list.getChunks();
    }

    public boolean isAlignindent() {
        return list.isAlignindent();
    }

    public ListItem getFirstItem() {
        return list.getFirstItem();
    }

    public ListBuilder add(Element o) {
        list.add(o);
        return this;
    }

    public String getPreSymbol() {
        return list.getPreSymbol();
    }

    public void setFirst(int first) {
        list.setFirst(first);
    }

    public void setLowercase(boolean uppercase) {
        list.setLowercase(uppercase);
    }

    public ListItem getLastItem() {
        return list.getLastItem();
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        return list.getAccessibleAttribute(key);
    }

    public void setPreSymbol(String preSymbol) {
        list.setPreSymbol(preSymbol);
    }

    public boolean isContent() {
        return list.isContent();
    }

    public float getSymbolIndent() {
        return list.getSymbolIndent();
    }

    public int size() {
        return list.size();
    }

    public boolean isLowercase() {
        return list.isLowercase();
    }

    public PdfName getRole() {
        return list.getRole();
    }

    public boolean isLettered() {
        return list.isLettered();
    }

    public void setIndentationRight(float indentation) {
        list.setIndentationRight(indentation);
    }

    public void setRole(PdfName role) {
        list.setRole(role);
    }

    public String getPostSymbol() {
        return list.getPostSymbol();
    }

    public boolean isNestable() {
        return list.isNestable();
    }

    public float getIndentationRight() {
        return list.getIndentationRight();
    }

    public ListBuilder setNumbered(boolean numbered) {
        list.setNumbered(numbered);
        return this;
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return list.getAccessibleAttributes();
    }

    public int getFirst() {
        return list.getFirst();
    }

    public ArrayList<Element> getItems() {
        return list.getItems();
    }

    public ListBuilder setSymbolIndent(float symbolIndent) {
        list.setSymbolIndent(symbolIndent);
        return this;
    }

    public int type() {
        return list.type();
    }

    public void setIndentationLeft(float indentation) {
        list.setIndentationLeft(indentation);
    }

    public ListBuilder setListSymbol(Chunk symbol) {
        list.setListSymbol(symbol);
        return this;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public float getIndentationLeft() {
        return list.getIndentationLeft();
    }

    public void setPostSymbol(String postSymbol) {
        list.setPostSymbol(postSymbol);
    }

    public Chunk getSymbol() {
        return list.getSymbol();
    }

    public ListBuilder setLettered(boolean lettered) {
        list.setLettered(lettered);
        return this;
    }

    public boolean isNumbered() {
        return list.isNumbered();
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        list.setAccessibleAttribute(key, value);
    }

    public void normalizeIndentation() {
        list.normalizeIndentation();
    }

    public void setAlignindent(boolean alignindent) {
        list.setAlignindent(alignindent);
    }

    public boolean add(String s) {
        return list.add(s);
    }

    public float getTotalLeading() {
        return list.getTotalLeading();
    }

    public void setAutoindent(boolean autoindent) {
        list.setAutoindent(autoindent);
    }
}
