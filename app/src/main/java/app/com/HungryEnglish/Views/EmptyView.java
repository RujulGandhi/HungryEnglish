package app.com.HungryEnglish.Views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import app.com.HungryEnglish.R;

/**
 * Created by good on 10/4/2017.
 */

public class EmptyView extends LinearLayout {


    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater.from(getContext()));
        View view = inflater.inflate(R.layout.empty_view, this, true);

    }
}
