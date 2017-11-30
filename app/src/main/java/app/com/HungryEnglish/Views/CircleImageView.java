package app.com.HungryEnglish.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import app.com.HungryEnglish.Activity.ImageActivity;

/**
 * Created by good on 11/28/2017.
 */

public class CircleImageView extends de.hdodenhof.circleimageview.CircleImageView implements View.OnClickListener {
    private Context context;

    public CircleImageView(Context context) {
        super(context);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent(context, ImageActivity.class);
        in.putExtra("bitmap", getBitmapFromView(v));
        context.startActivity(in);
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}
