package com.github.sigute.mindbleach;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sigute.mindbleach.kittenapi.Kitten;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spikereborn on 06/03/2015.
 */
public class ImageAdapter extends PagerAdapter
{
    Context context;
    private List<Kitten> kittens;

    ImageAdapter(final Context context)
    {
        this.context = context;
        kittens = new ArrayList<Kitten>();
    }

    @Override
    public int getCount()
    {
        return kittens.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.kitten_layout, null);

        ImageView kittenImageView = (ImageView) view.findViewById(R.id.kitten_image_view);
        Picasso.with(context).load(kittens.get(position).getImageURL()).fit().centerInside()
                .into(kittenImageView);

        TextView kittenDescription = (TextView) view
                .findViewById(R.id.kitten_description_text_view);
        kittenDescription.setText("This kitten comes from " + kittens.get(position).getSourceURL());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    public void addKittens(final List<Kitten> newKittens)
    {
        ((Activity) context).runOnUiThread(new Runnable()
        {
            public void run()
            {
                kittens.addAll(newKittens);
                notifyDataSetChanged();
            }
        });
    }
}