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
* Date: 9/13/13
*/
public class Style {
    protected final String name;

    List<StyleFunction> styleFunctions = new ArrayList<StyleFunction>();

    public Style(String name) {
        this.name = name;
    }

    public boolean isEmpty() {
        return styleFunctions.isEmpty();
    }

    public void apply(Element el) {
        for (StyleFunction function : styleFunctions) {
            function.applyGeneral(el);
        }
    }
}
