package com.blackspider.bloodforlife.views;

import android.graphics.Typeface;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by Mr blackSpider on 8/9/2017.
 */

public class DancingScriptRegularFontTextView extends TextView {

    public DancingScriptRegularFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public DancingScriptRegularFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public DancingScriptRegularFontTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        // Just Change your font name
        Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/dancing_script_regular.otf");
        setTypeface(myTypeface);
    }
}
