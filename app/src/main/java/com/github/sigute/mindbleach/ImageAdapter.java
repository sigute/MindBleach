package com.github.sigute.mindbleach;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.sigute.mindbleach.kittenapi.Kitten;
import com.github.sigute.mindbleach.kittenapi.KittenFactory;

/**
 * Created by spikereborn on 06/03/2015.
 */
public class ImageAdapter extends PagerAdapter
{
    Context context;
    private KittenFactory kittenFactory;
    private Kitten[] kittens;

    ImageAdapter(final Context context)
    {
        this.context = context;

        kittenFactory = KittenFactory.getInstance(context);
        kittens = kittenFactory.getKittens();

        new java.util.Timer().schedule(new java.util.TimerTask()
        {
            @Override
            public void run()
            {
                ((Activity) context).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        kittens = kittenFactory.getKittens();
                        notifyDataSetChanged();
                    }
                });

            }
        }, 30000);
    }

    @Override
    public int getCount()
    {
        return kittens.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ImageView imageView = new ImageView(context);
        int padding = 5; //context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageBitmap(kittens[position].getKittenImage());
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView((ImageView) object);
    }
}