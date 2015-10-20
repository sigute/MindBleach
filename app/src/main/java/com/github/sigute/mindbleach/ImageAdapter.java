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
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends PagerAdapter
{
    Context context;
    private List<Kitten> kittens;

    ImageAdapter(final Context context)
    {
        this.context = context;
        kittens = new ArrayList<>();
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
        final View view = inflater.inflate(R.layout.kitten_layout, null);

        ImageView kittenImageView = (ImageView) view.findViewById(R.id.kitten_image_view);
        Picasso.with(context).load(kittens.get(position).getImageURL()).fit().centerInside()
                .into(kittenImageView, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        ProgressWheel progressWheel = (ProgressWheel) view
                                .findViewById(R.id.kitten_loading_wheel);
                        progressWheel.setVisibility(View.GONE);

                        TextView loadingTextView = (TextView) view
                                .findViewById(R.id.kitten_loading_text_view);
                        loadingTextView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError()
                    {

                    }
                });

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