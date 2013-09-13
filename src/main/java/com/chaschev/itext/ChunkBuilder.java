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
import com.itextpdf.text.pdf.*;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * User: achaschev
 * Date: 8/27/13
 */
public class ChunkBuilder extends ElementBuilder<Chunk, ChunkBuilder> {
    public ChunkBuilder(ITextBuilder b) {
        super(b);
    }

    @Override
    public ChunkBuilder withNew() {
        element = new Chunk();
        return this;
    }

    public ChunkBuilder withNew(final String content, final Font font){
        element = new Chunk(content, font);
        return this;
    }

    public ChunkBuilder withNew(final String content, @Nullable final java.lang.String cssStyleString){
        element = new Chunk(content);

        b.styles().apply(element, cssStyleString);

        return this;
    }

    public ChunkBuilder withChunk(Chunk chunk){
        this.element = chunk;
        return this;
    }

    public boolean process(ElementListener listener) {
        return element.process(listener);
    }

    public Image getImage() {
        return element.getImage();
    }

    public float getCharacterSpacing() {
        return element.getCharacterSpacing();
    }

    public int type() {
        return element.type();
    }

    public Chunk setLocalDestination(String name) {
        return element.setLocalDestination(name);
    }

    public ChunkBuilder setCharacterSpacing(float charSpace) {
        element.setCharacterSpacing(charSpace);
        return this;
    }

    public Chunk setAnchor(String url) {
        return element.setAnchor(url);
    }

    public Chunk setAnnotation(PdfAnnotation annotation) {
        return element.setAnnotation(annotation);
    }

    public ChunkBuilder setFont(Font font) {
        element.setFont(font);
        return this;
    }

    public Font getFont() {
        return element.getFont();
    }

    public float getTextRise() {
        return element.getTextRise();
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        element.setAccessibleAttribute(key, value);
    }

    public boolean isEmpty() {
        return element.isEmpty();
    }

    public Chunk setSplitCharacter(SplitCharacter splitCharacter) {
        return element.setSplitCharacter(splitCharacter);
    }

    public static Chunk createWhitespace(String content, boolean preserve) {
        return Chunk.createWhitespace(content, preserve);
    }

    public Chunk setWordSpacing(float wordSpace) {
        return element.setWordSpacing(wordSpace);
    }

    public Chunk setAnchor(URL url) {
        return element.setAnchor(url);
    }

    public StringBuffer append(String string) {
        return element.append(string);
    }

    public HashMap<String, Object> getAttributes() {
        return element.getAttributes();
    }

    public static Chunk createWhitespace(String content) {
        return Chunk.createWhitespace(content);
    }

    public void setAttributes(HashMap<String, Object> attributes) {
        element.setAttributes(attributes);
    }

    public Chunk setNewPage() {
        return element.setNewPage();
    }

    public Chunk setUnderline(float thickness, float yPosition) {
        return element.setUnderline(thickness, yPosition);
    }

    public HyphenationEvent getHyphenation() {
        return element.getHyphenation();
    }

    public void setRole(PdfName role) {
        element.setRole(role);
    }

    public Chunk setRemoteGoto(String filename, int page) {
        return element.setRemoteGoto(filename, page);
    }

    public Chunk setLocalGoto(String name) {
        return element.setLocalGoto(name);
    }

    @Deprecated
    public boolean isTabspace() {
        return element.isTabspace();
    }

    public Chunk setTextRise(float rise) {
        return element.setTextRise(rise);
    }

    public Chunk setTextRenderMode(int mode, float strokeWidth, BaseColor strokeColor) {
        return element.setTextRenderMode(mode, strokeWidth, strokeColor);
    }

    public float getHorizontalScaling() {
        return element.getHorizontalScaling();
    }

    public UUID getId() {
        return element.getId();
    }

    public Chunk setUnderline(BaseColor color, float thickness, float thicknessMul, float yPosition, float yPositionMul, int cap) {
        return element.setUnderline(color, thickness, thicknessMul, yPosition, yPositionMul, cap);
    }

    public PdfName getRole() {
        return element.getRole();
    }

    public Chunk setGenericTag(String text) {
        return element.setGenericTag(text);
    }

    public Chunk setRemoteGoto(String filename, String name) {
        return element.setRemoteGoto(filename, name);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return element.getAccessibleAttributes();
    }

    public boolean isWhitespace() {
        return element.isWhitespace();
    }

    public float getWordSpacing() {
        return element.getWordSpacing();
    }

    public Chunk setAction(PdfAction action) {
        return element.setAction(action);
    }

    public boolean hasAttributes() {
        return element.hasAttributes();
    }

    @Deprecated
    public static Chunk createTabspace() {
        return Chunk.createTabspace();
    }

    public Chunk setBackground(BaseColor color) {
        return element.setBackground(color);
    }

    @Deprecated
    public static Chunk createTabspace(float spacing) {
        return Chunk.createTabspace(spacing);
    }

    public Chunk setHorizontalScaling(float scale) {
        return element.setHorizontalScaling(scale);
    }

    public Chunk setBackground(BaseColor color, float extraLeft, float extraBottom, float extraRight, float extraTop) {
        return element.setBackground(color, extraLeft, extraBottom, extraRight, extraTop);
    }

    public String getContent() {
        return element.getContent();
    }

    public Chunk setLineHeight(float lineheight) {
        return element.setLineHeight(lineheight);
    }

    public boolean isContent() {
        return element.isContent();
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        return element.getAccessibleAttribute(key);
    }

    public Chunk setHyphenation(HyphenationEvent hyphenation) {
        return element.setHyphenation(hyphenation);
    }

    public void setId(UUID id) {
        element.setId(id);
    }

    public List<Chunk> getChunks() {
        return element.getChunks();
    }

    public Chunk setSkew(float alpha, float beta) {
        return element.setSkew(alpha, beta);
    }

    public boolean isNestable() {
        return element.isNestable();
    }

    public float getWidthPoint() {
        return element.getWidthPoint();
    }

    public Chunk build() {
        return element;
    }
}
