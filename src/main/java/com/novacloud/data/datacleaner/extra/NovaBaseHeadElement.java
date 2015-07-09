package com.novacloud.data.datacleaner.extra;

import org.eobjects.analyzer.result.html.HeadElement;
import org.eobjects.analyzer.result.html.HtmlRenderingContext;

/**
 * Created by yong on 10/24/14.
 */
public class NovaBaseHeadElement implements HeadElement {

    private final String _resourcesDirectory;

    /**
     * Constructs a {@link NovaBaseHeadElement} with the default (hosted) resources
     * directory.
     */
    public NovaBaseHeadElement() {
        //TODO: use config file
        this(Config.StaticResourcesRootURI);
    }

    /**
     * Constructs a {@link NovaBaseHeadElement}.
     *
     * @param resourcesDirectory
     */
    public NovaBaseHeadElement(String resourcesDirectory) {
        _resourcesDirectory = resourcesDirectory;
    }

    @Override
    public String toHtml(HtmlRenderingContext context) {
        return "<script type=\"text/javascript\" src=\"" + _resourcesDirectory + "/analysis-result.js\"></script>\n"
                + "<link rel=\"shortcut icon\" href=\"" + _resourcesDirectory + "/analysis-result-icon.png\" />\n"
                + "<script type=\"text/javascript\">//<![CDATA[\n" + "  var analysisResult = {};\n" + "//]]>\n</script>";
    }
}
