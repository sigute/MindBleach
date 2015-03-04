package com.github.sigute.mindbleach.kittenapi;

import android.content.Context;

import com.github.sigute.mindbleach.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for kittens!
 * Created by sigute on 04/03/2015.
 */
public class KittenFactory
{
    private static final String KITTEN_API_URL = "http://thecatapi.com/api/images/";

    private static KittenFactory instance;

    private Context context;
    private int currentKittenIndex;
    private List<Kitten> kittenList;

    public static synchronized KittenFactory getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new KittenFactory(context);
        }
        return instance;
    }

    private KittenFactory(Context context)
    {
        this.context = context;
        kittenList = new ArrayList<Kitten>();
        addDefaultKittens();
        currentKittenIndex = 0;

        //should probably do this fetch in the background!
        fetchMoreKittens();
    }

    private void addDefaultKittens()
    {
        kittenList.add(new Kitten(context.getResources().getDrawable(R.drawable.default_kitten_1),
                "http://www.public-domain-image.com/public-domain-images-pictures-free-stock-photos/fauna-animals-public-domain-images-pictures/cats-and-kittens-public-domain-images-pictures/exploring-kitten.jpg",
                "test",
                "http://www.public-domain-image.com/fauna-animals-public-domain-images-pictures/cats-and-kittens-public-domain-images-pictures/exploring-kitten.jpg.html",
                "Exploring kitten by Rosendahl"));
    }

    private void fetchMoreKittens()
    {
        //TODO
        //figure out what happens if internet is down
    }

    public Kitten getNextKitten()
    {
        currentKittenIndex++;

        if (currentKittenIndex > kittenList.size())
        {
            fetchMoreKittens();
        }

        //check whether internet might have been down, ie handle scenario when there are no more kittens even after fetch :( maybe start at the start of list?

        //check whether kitten list might be getting to the end, if so, fetch more kittens in background when only a few kittens remain.

        return kittenList.get(currentKittenIndex);
    }

    public Kitten getPreviousKitten()
    {
        if (currentKittenIndex == 0)
        {
            return null;
        }

        currentKittenIndex--;
        return kittenList.get(currentKittenIndex);
    }
}
