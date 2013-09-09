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

import com.chaschev.chutils.util.Exceptions;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User: chaschev
 * Date: 6/21/13
 */
public class ITextBuilder  {
    public static final Logger logger = LoggerFactory.getLogger(ITextBuilder.class);

    public boolean drawBorders;

    protected Predicate<ChunkBuilder> defaultChunkSettings;
    protected Predicate<PhraseBuilder> defaultPhraseSettings;
    protected Predicate<ColumnTextBuilder> defaultColumnTextSettings;

    protected final ChunkBuilder reusableChunkBuilder = new ChunkBuilder(this);
    protected final PhraseBuilder reusablePhraseBuilder = new PhraseBuilder(this);
    protected final ColumnTextBuilder reusableColumnTextBuilder = new ColumnTextBuilder(this);
    protected final RectangleBuilder reusableRectangleBuilder = new RectangleBuilder();

    protected Document document;
    private final OutputStream os;
    protected PdfWriter writer;
    protected CanvasBuilder canvas;

    protected StyleRegister styles = new StyleRegister(this);

    public ITextBuilder(Document document, PdfWriter writer) {
        this.document = document;
        this.writer = writer;
        canvas = new CanvasBuilder(writer.getDirectContent());
        os = null;
    }

    public ITextBuilder(Document document) {
        this(document, new ByteArrayOutputStream(65536));
    }

    public ITextBuilder(Document document, OutputStream os) {
        try {
            this.document = document;
            this.os = os;

            writer = PdfWriter.getInstance(document, os);

            document.open();

            canvas = new CanvasBuilder(writer.getDirectContent());
        } catch (DocumentException e) {
            throw Exceptions.runtime(e);
        }
    }

    public TableBuilder newTableBuilder(float... relativeWidths) {
        return
            new TableBuilder(this, relativeWidths)
                .splitRows(TableSplit.SPLIT_ALL);
    }

    public ITextBuilder close() {
        document.close();
        return this;
    }

    public byte[] getBytes() {
        if (os instanceof ByteArrayOutputStream) {
            return ((ByteArrayOutputStream) os).toByteArray();
        }

        throw new IllegalStateException("os must be of type ByteArrayOutputStream, but is of type " + (os == null ? "null" : os.getClass().getSimpleName()));
    }

    public ITextBuilder saveToFile(File file) {
        final File parent = file.getParentFile();

        if(!parent.exists()){
            parent.mkdirs();
        }

        try {
            FileUtils.writeByteArrayToFile(file, getBytes());
            return this;
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }

    public ITextBuilder drawBorders() {
        drawBorders = true;
        return this;
    }

    public enum TableSplit{
        DEFAULT,SPLIT_ALL
    }

    public PhraseBuilder phrase() {
        return phrase(true);
    }

    /**
     * Creates text with normal style.
     */
    public PhraseBuilder phrase(String text){
        return phrase(false).newPhrase(text, "normal");
    }

    public PhraseBuilder phrase(String text, String cssStyleString){
        return phrase(false).newPhrase(text,cssStyleString);
    }

    public ChunkBuilder chunk(String text){
        return chunk(false, text, null).newChunk(text, (String)null);
    }

    public ChunkBuilder chunk(String text, String cssStyleString){
        return chunk(false, text, null).newChunk(text, cssStyleString);
    }

    public PhraseBuilder phrase(boolean reuse){
        final PhraseBuilder phraseBuilder =
            reuse ? reusablePhraseBuilder.newPhrase() : new PhraseBuilder(this).newPhrase();

        if(defaultPhraseSettings != null){
            defaultPhraseSettings.apply(phraseBuilder);
        }

        return phraseBuilder;
    }

    public PhraseBuilder reusePhraseBuilder(){
        return phrase();
    }

    public ChunkBuilder chunk(boolean reuse, String text, Font font){
        final ChunkBuilder chunkBuilder =
            reuse ? reusableChunkBuilder.newChunk(text, font) : new ChunkBuilder(this).newChunk(text, font);

        return applyDefaultSettings(chunkBuilder);
    }

    public ChunkBuilder applyDefaultSettings(ChunkBuilder chunkBuilder) {
        if(defaultChunkSettings != null){
            defaultChunkSettings.apply(chunkBuilder);
        }

        return chunkBuilder;
    }

    public ColumnTextBuilder reuseColumnTextBuilder(){
        Preconditions.checkNotNull(canvas);

        return applyDefaultSettings(reusableColumnTextBuilder.newColumnText(canvas.canvas));
    }

    public ColumnTextBuilder newColumnTextBuilder(){
        Preconditions.checkNotNull(canvas);

        return applyDefaultSettings(new ColumnTextBuilder(this).newColumnText(canvas.canvas));
    }

    public ColumnTextBuilder applyDefaultSettings(ColumnTextBuilder ctb) {
        if(defaultColumnTextSettings != null){
            defaultColumnTextSettings.apply(ctb);
        }

        return ctb;
    }

    public RectangleBuilder reuseRectangleBuilder(Rectangle r){
        return reusableRectangleBuilder.reuse(r);
    }

    public RectangleBuilder newRectangleBuilder(Rectangle r){
        return new RectangleBuilder().reuse(r);
    }

    public RectangleBuilder newCopyRectangleBuilder(Rectangle r){
        return new RectangleBuilder().reuse(new Rectangle(r));
    }

    public ITextBuilder setDefaultPhraseSettings(Predicate<PhraseBuilder> defaultPhraseSettings) {
        this.defaultPhraseSettings = defaultPhraseSettings;

        return this;
    }

    public void setDefaultColumnTextSettings(Predicate<ColumnTextBuilder> defaultColumnTextSettings) {
        this.defaultColumnTextSettings = defaultColumnTextSettings;
    }

    public Phrase newPhrase(String text, Font font){
        final Chunk element = new Chunk(text, font);
//        element.setHyphenation(new HyphenationAuto("en", "US", 3, 3));
        return phrase().add(element).build();
    }

    public Document getDocument() {
        return document;
    }

    public PdfContentByte getCanvas() {
        return canvas.canvas;
    }

    public CanvasBuilder getCanvasBuilder(){
        return canvas;
    }

    public PdfWriter getWriter() {
        return writer;
    }

    public StyleRegister styles(){
        return styles;
    }
}
