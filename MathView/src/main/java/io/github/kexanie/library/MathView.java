package io.github.kexanie.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

public class MathView extends WebView {
    private String mText;
    private int mEngine;

    public MathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setBackgroundColor(Color.TRANSPARENT);

        TypedArray mTypeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0
        );

        try { // the order of execution of setEngine() and setText() matters
            setEngine(mTypeArray.getInteger(R.styleable.MathView_engine, 0));
            setText(mTypeArray.getString(R.styleable.MathView_text));
        } finally {
            mTypeArray.recycle();
        }
    }

    private Chunk getChunk() {
        String TEMPLATE_KATEX = "katex";
        String TEMPLATE_MATHJAX = "mathjax";
        String template = TEMPLATE_KATEX;
        AndroidTemplates loader = new AndroidTemplates(getContext());
        switch (mEngine) {
            case Engine.KATEX: template = TEMPLATE_KATEX; break;
            case Engine.MATHJAX: template = TEMPLATE_MATHJAX; break;
        }

        return new Theme(loader).makeChunk(template);
    }

    public void setText(String text) {
        mText = text;
        Chunk chunk = getChunk();

        String TAG_FORMULA = "formula";
        chunk.set(TAG_FORMULA, mText);
        this.loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", "about:blank");
    }

    public String getText() {
        return mText;
    }

    /**
     * Set the js engine used for rendering the formulas.
     * @param engine must be one of the constants in class Engine
     */
    public void setEngine(int engine) {
        switch (engine) {
            case Engine.KATEX: {
                mEngine = Engine.KATEX;
                break;
            }
            case Engine.MATHJAX: {
                mEngine = Engine.MATHJAX;
                break;
            }
            default: mEngine = Engine.KATEX;
        }
    }

    public static class Engine {
        final public static int KATEX = 0;
        final public static int MATHJAX = 1;
    }
}