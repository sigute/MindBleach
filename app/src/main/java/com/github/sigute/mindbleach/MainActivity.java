package com.github.sigute.mindbleach;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.github.sigute.mindbleach.kittenapi.KittenFactory;
import com.github.sigute.mindbleach.kittenapi.KittensLoadedEvent;
import com.pnikosis.materialishprogress.ProgressWheel;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity
{
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        if (adapter == null)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    ProgressWheel progressWheel = (ProgressWheel) findViewById(R.id.loading_wheel);
                    progressWheel.setVisibility(View.GONE);

                    TextView loadingTextView = (TextView) findViewById(R.id.loading_text_view);
                    loadingTextView.setVisibility(View.GONE);
                }
            });

            ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            adapter = new ImageAdapter(this);
            viewPager.setAdapter(adapter);
        }

        adapter.addKittens(event.getKittens());
    }
}
