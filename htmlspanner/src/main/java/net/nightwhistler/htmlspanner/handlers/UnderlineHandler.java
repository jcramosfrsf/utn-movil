package net.nightwhistler.htmlspanner.handlers;

import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;

import net.nightwhistler.htmlspanner.SpanStack;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

/**
 * Created by Tomas on 20/07/2016.
 */
public class UnderlineHandler extends TagNodeHandler {

    @Override
    public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end, SpanStack spanStack) {
        spanStack.pushSpan(new UnderlineSpan(), start, end);
    }
}
