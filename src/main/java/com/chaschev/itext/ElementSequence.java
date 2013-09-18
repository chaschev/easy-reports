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

import java.util.ArrayList;
import java.util.List;

/**
 * User: chaschev
 * Date: 9/8/13
 */
public class ElementSequence {
    final protected List<Element> elements;
    protected float[] heights;
    public float initialContentHeight;

    public ElementSequence() {
         elements = new ArrayList<Element>();
    }

    public ElementSequence(List<Element> elements) {
        this.elements = elements;
    }

    public void add(Element element){
        elements.add(element);
    }

    public List<Element> getElements() {
        return elements;
    }

    public boolean isSpace(int i) {

        return i < elements.size() && elements.get(i) instanceof SpaceElement;
    }

    public void setHeight(int i, float h){
        if(heights == null){
            heights = new float[elements.size()];
        }
        heights[i] = h;
    }

    public float getHeight(int i){
        return heights[i];
    }

    public int size() {
        return elements.size();
    }

    public float getInitialContentHeight() {
        return initialContentHeight;
    }

    public void trim() {
        while(!elements.isEmpty() && elements.get(elements.size() - 1) instanceof SpaceElement){
            elements.remove(elements.size() - 1);
        }
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
