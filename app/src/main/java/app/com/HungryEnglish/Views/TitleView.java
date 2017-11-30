package app.com.HungryEnglish.Views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import app.com.HungryEnglish.R;

/**
 * Created by Rujul on 10/4/2017.
 */

public class TitleView extends View {
    TextView textView;

    public TitleView(Context context) {
        super(context);
        init(context);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater.from(getContext()));
        View view = inflater.inflate(R.layout.title_tv, null, false);
        textView = (TextView) view.findViewById(R.id.title);
        textView.setText("He;l;l");
    }

}
