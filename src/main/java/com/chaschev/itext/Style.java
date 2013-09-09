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

import java.util.List;

/**
 * User: achaschev
 * Date: 8/29/13
 * Time: 2:55 PM
 */
public abstract class Style<T extends Element> {
    public String name;

    ITextBuilder b;

    protected Style(String name) {
        this.name = name;
    }

    public void applyToChunk(ChunkBuilder c){
        throw new UnsupportedOperationException("todo: implement " +
            this.getClass().getSimpleName() + ".applyToChunk(c)");
    }

    public void apply(T element){
        apply(element, true);
    }

    public void apply(T element, boolean applyToChildrenChunks){
        final ChunkBuilder cb = new ChunkBuilder(b);

        if (element instanceof Chunk) {
            Chunk chunk = (Chunk) element;
            b.applyDefaultSettings(cb.withChunk(chunk));

            applyToChunk(cb);
        }else
        if(applyToChildrenChunks){
            final List<Chunk> chunks = element.getChunks();

            for (Chunk chunk : chunks) {
                b
                    .applyDefaultSettings(cb.withChunk(chunk));

                applyToChunk(cb);
            }
        }
    }
}
