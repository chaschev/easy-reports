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

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: chaschev
 * Date: 6/21/13
 */
public enum ITextSingleton {
    INSTANCE;

    final Logger logger = LoggerFactory.getLogger(ITextSingleton.class);

    {
        logger.info("initializing fonts");
        try {
            FontFactory.register(getClass().getResource("/reports/fonts/handlee/Handlee-Regular.ttf").getPath(), "Handlee");
            FontFactory.register(getClass().getResource("/reports/fonts/opensanscond/OpenSans-CondBold.ttf").getPath(), "OpenSansCond");
            FontFactory.register(getClass().getResource("/reports/fonts/opensanscond/OpenSans-CondLight.ttf").getPath(), "OpenSansCondLight");
            FontFactory.register(getClass().getResource("/reports/fonts/opensans/Regular.ttf").getPath(), "OpenSans");
            FontFactory.register(getClass().getResource("/reports/fonts/opensans/Bold.ttf").getPath(), "OpenSansBold");
            FontFactory.register(getClass().getResource("/reports/fonts/opensans/BoldItalic.ttf").getPath(), "OpenSansBoldItalic");
        } catch (Exception e) {
            logger.error("", e);

            FontFactory.register("grails-app/conf/reports/fonts/handlee/Handlee-Regular.ttf", "Handlee");
            FontFactory.register("grails-app/conf/reports/fonts/opensanscond/OpenSans-CondBold.ttf", "OpenSansCond");
            FontFactory.register("grails-app/conf/reports/fonts/opensans/Regular.ttf", "OpenSans");
            FontFactory.register("grails-app/conf/reports/fonts/opensans/Bold.ttf", "OpenSansBold");
            FontFactory.register("grails-app/conf/reports/fonts/opensans/BoldItalic.ttf", "OpenSansBoldItalic");
        }
    }

    public ITextBuilder newBuilder(){
        return new ITextBuilder();
    }

    public ITextBuilder newBuilder(Document document, PdfWriter writer){
        return new ITextBuilder(document, writer);
    }

}
