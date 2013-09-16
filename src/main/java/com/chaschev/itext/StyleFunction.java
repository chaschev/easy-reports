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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import java.util.List;

/**
 * User: achaschev
 * Date: 8/29/13
 * Time: 2:55 PM
 */

//comment, subHeader, normal -> setLeading 11f
public abstract class StyleFunction {
    ITextBuilder b;

    public void apply(ParagraphBuilder pb){

    }

    public void apply(ChunkBuilder c){
//        throw new UnsupportedOperationException("todo: implement " +
//            this.getClass().getSimpleName() + ".apply(ChunkBuilder)");
   }

    public void apply(PhraseBuilder pb){
//        throw new UnsupportedOperationException("todo: implement " +
//            this.getClass().getSimpleName() + ".apply(PhraseBuilder)");
    }

    public void applyGeneral(Element element, boolean applyToChildrenChunks){
        if(applyToChildrenChunks){
            final List<Chunk> chunks = element.getChunks();
            final ChunkBuilder cb = b.reusableChunkBuilder;

            for (Chunk chunk : chunks) {
                apply(cb.withChunk(chunk));
            }
        }

        if (element instanceof Chunk) {
            apply(b.reusableChunkBuilder.with((Chunk) element));
        }else
        if (element instanceof Paragraph) {
            apply(b.reusableParagraphBuilder.with((Paragraph) element));
        }else
        if (element instanceof Phrase) {
            apply(b.reusePhraseBuilder().with((Phrase) element));
        }
    }

    public void applyGeneral(Element element){
        applyGeneral(element, true);
    }

}
