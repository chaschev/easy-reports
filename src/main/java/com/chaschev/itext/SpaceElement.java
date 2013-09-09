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
import com.itextpdf.text.ElementListener;

import java.util.Collections;
import java.util.List;

/**
 * User: chaschev
 * Date: 9/8/13
 */
public class SpaceElement implements Element {
    float width;
    float height;

    public SpaceElement(float height) {
        this.height = height;
    }

    @Override
    public boolean process(ElementListener listener) {
        return true;
    }

    @Override
    public int type() {
        throw new UnsupportedOperationException("todo SpaceElement.type");
    }

    @Override
    public boolean isContent() {
        return true;
    }

    @Override
    public boolean isNestable() {
        return false;
    }

    @Override
    public List<Chunk> getChunks() {
        return Collections.emptyList();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    public boolean fits(ColumnTextBuilder ctb, float bottom){
        return ctb.getYLine() - height >= bottom;
    }

    public void add(ColumnTextBuilder ctb, boolean simulate){
        final float newYLine = ctb.getYLine() - height;

        ctb.setYLine(newYLine);
    }
}
