package io.github.kexanie.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

public class MathView extends WebView {
    private Chunk chunk;
    private String mText;
    private TypedArray mTypeArray;

    public MathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getSettings().setJavaScriptEnabled(true);
        AndroidTemplates loader = new AndroidTemplates(context);
        chunk = new Theme(loader).makeChunk("mathview");

        mTypeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0
        );

        try {
            setText(mTypeArray.getString(R.styleable.MathView_text));
        } finally {
            mTypeArray.recycle();
        }
    }

    public void setText(String text) {
        mText = text;
        chunk.set("formula", mText);
        this.loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", "about:blank");
    }

    public String getText() {
        return mText;
    }
}
