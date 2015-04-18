package com.github.sigute.mindbleach.kittenapi;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Factory for kittens!
 * Created by sigute on 04/03/2015.
 */
public class KittenFactory
{
    private static final String KITTEN_API_URL = "http://thecatapi.com/api/images/";

    private static final String ns = null;

    private static KittenFactory instance;

    private Context context;
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
    }

    public void fetchKittens()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                List<Kitten> newKittens = fetchMoreKittens();
                kittenList.addAll(newKittens);
                //TODO check whether kittens are loaded - might be an error!

                EventBus.getDefault().post(new KittensLoadedEvent(newKittens));
            }
        }).start();
    }

    private void addDefaultKittens()
    {
        // kittenList.add(new Kitten(context.getResources().getDrawable(R.drawable.default_kitten_1),
        //         "http://www.public-domain-image.com/public-domain-images-pictures-free-stock-photos/fauna-animals-public-domain-images-pictures/cats-and-kittens-public-domain-images-pictures/exploring-kitten.jpg",
        //         "test",
        //         "http://www.public-domain-image.com/fauna-animals-public-domain-images-pictures/cats-and-kittens-public-domain-images-pictures/exploring-kitten.jpg.html",
        //         "Exploring kitten by Rosendahl"));
    }

    private List<Kitten> fetchMoreKittens()
    {
        //TODO
        //figure out what happens if internet is down

        InputStream catInputStream = null;

        try
        {
            URL url = new URL(KITTEN_API_URL + "get?format=xml&results_per_page=20");
            catInputStream = url.openStream();


            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(catInputStream, null);
            parser.nextTag();

            List<Kitten> newKittens = readResponse(parser);
            //TODO check whether read worked?
            return newKittens;
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (catInputStream != null)
            {
                try
                {
                    catInputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        //TODO should deal with errors and then null won't be needed
        // new event for error?
        return null;
    }

    private List<Kitten> readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        List<Kitten> kittens = new ArrayList<Kitten>();

        parser.require(XmlPullParser.START_TAG, ns, "response");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the image tag
            if (name.equals("data"))
            {
                kittens = readData(parser);
            }
            else
            {
                skip(parser);
            }
        }
        return kittens;
    }

    private List<Kitten> readData(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List<Kitten> kittens = new ArrayList<Kitten>();

        parser.require(XmlPullParser.START_TAG, ns, "data");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the image tag
            if (name.equals("images"))
            {
                kittens = readImages(parser);
            }
            else
            {
                skip(parser);
            }
        }
        return kittens;
    }

    private List<Kitten> readImages(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "images");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the image tag
            if (name.equals("image"))
            {
                entries.add(readKitten(parser));
            }
            else
            {
                skip(parser);
            }
        }
        return entries;
    }

    private Kitten readKitten(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "image");
        String url = null;
        String id = null;
        String source_url = null;
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("url"))
            {
                url = readUrl(parser);
            }
            else if (name.equals("id"))
            {
                id = readId(parser);
            }
            else if (name.equals("source_url"))
            {
                source_url = readSourceUrl(parser);
            }
            else
            {
                skip(parser);
            }
        }

        return new Kitten(url, id, source_url);
    }

    private String readUrl(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "url");
        String url = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "url");
        return url;
    }

    private String readId(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "id");
        return id;
    }

    private String readSourceUrl(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "source_url");
        String source_url = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "source_url");
        return source_url;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT)
        {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        if (parser.getEventType() != XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0)
        {
            switch (parser.next())
            {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public List<Kitten> getKittens()
    {
        return kittenList;
    }
}
