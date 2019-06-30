package io.github.kexanie.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

public class MathView extends WebView {
    private String mText;
    private String mConfig;
    private int mEngine;
    private boolean mDarkTextColor = true;
    private boolean mDisableTouchEvent;

    public MathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setBackgroundColor(Color.TRANSPARENT);

        TypedArray typeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0
        );

        try { // the order of execution of setEngine() and setText() matters
            setDisableTouchEvent(typeArray.getBoolean(R.styleable.MathView_disableTouchEvent, true));
            setEngine(typeArray.getInteger(R.styleable.MathView_engine, 0));
            setText(typeArray.getString(R.styleable.MathView_text));
        } finally {
            typeArray.recycle();
        }
    }

    // disable touch event on MathView
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDisableTouchEvent;
    }


    private Chunk getChunk() {
        String TEMPLATE_KATEX = "katex";
        String TEMPLATE_MATHJAX = "mathjax";
        String template = TEMPLATE_KATEX;
        AndroidTemplates loader = new AndroidTemplates(getContext());
        switch (mEngine) {
            case Engine.KATEX:
                template = TEMPLATE_KATEX;
                break;
            case Engine.MATHJAX:
                template = TEMPLATE_MATHJAX;
                break;
        }

        return new Theme(loader).makeChunk(template);
    }

    public void setText(String text) {
        mText = text;
        Chunk chunk = getChunk();

        String TAG_FORMULA = "formula";
        String TAG_CONFIG = "config";
        chunk.set(TAG_FORMULA, mText);
        chunk.set(TAG_CONFIG, mConfig);
        this.loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", "about:blank");
    }

    public String getText() {
        return mText;
    }

    public void setDisableTouchEvent(boolean disableTouchEvent) {
        this.mDisableTouchEvent = disableTouchEvent;
    }

    /**
     * Tweak the configuration of MathJax.
     * The `config` string is a call statement for MathJax.Hub.Config().
     * For example, to enable auto line breaking, you can call:
     * config.("MathJax.Hub.Config({
     *      CommonHTML: { linebreaks: { automatic: true } },
     *      "HTML-CSS": { linebreaks: { automatic: true } },
     *      SVG: { linebreaks: { automatic: true } }
     *  });");
     *
     * This method should be call BEFORE setText() and AFTER setEngine().
     * PLEASE PAY ATTENTION THAT THIS METHOD IS FOR MATHJAX ONLY.
     * @param config 
     */
    public void config(String config) {
        if (mEngine == Engine.MATHJAX) {
            this.mConfig = config;
        }
    }

    /**
     * Set the js engine used for rendering the formulas.
     * @param engine must be one of the constants in class Engine
     *
     * This method should be call BEFORE setText().
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
            default:
                mEngine = Engine.KATEX;
        }
    }

    public static class Engine {
        final public static int KATEX = 0;
        final public static int MATHJAX = 1;
    }
}