package com.novacloud.data.datacleaner.extra;

import org.eobjects.analyzer.result.html.HeadElement;
import org.eobjects.analyzer.result.html.HtmlAnalysisResultWriter;

/**
 * Created by yong on 10/24/14.
 */
public class NovaHtmlAnalysisResultWriter  extends HtmlAnalysisResultWriter{
    public NovaHtmlAnalysisResultWriter() {
        super();
    }
    @Override
    protected HeadElement createBaseHeadElement() {
        return new NovaBaseHeadElement();
    }

}
