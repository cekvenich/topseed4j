package de.neuland.jade4j.filter;

import java.util.List;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import de.neuland.jade4j.parser.node.Attr;

public class MarkdownFilter extends CachingFilter {

	Parser parser = Parser.builder().build();
	HtmlRenderer renderer = HtmlRenderer.builder().build();

	@Override
	protected String convert(String source, List<Attr> attributes) {
		return renderer.render(parser.parse(source));
	}

}
