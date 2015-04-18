package com.github.sigute.mindbleach;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.github.sigute.mindbleach.kittenapi.KittenFactory;
import com.github.sigute.mindbleach.kittenapi.KittensLoadedEvent;

import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity
{
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);

        KittenFactory.getInstance(this).fetchKittens();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(KittensLoadedEvent event)
    {
        adapter.addKittens(event.getKittens());
    }
}
