package com.mpd.qms.myapplication;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    ViewPager viewPager;
    vpAdapter viewPagerAdapter;
    Button btnAdd,btnDel;
    ImageView btnPrev,btnNext;

    //put all the viewpagerList in arraylist
    private List<View> viewList = new ArrayList<>();

    //count for counting how many pagers
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create first three viewpager by delay 1000 millisecond.
        int delayTime=1000;

        for(int i=0;i<3;i++){
            loadFirstThreePage(delayTime);
            delayTime = delayTime + delayTime;
        }

        viewPagerAdapter=new vpAdapter(viewList);//create an adapter
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);//Set up an adapter for ViewPage

        //set listener for viewpager swiping event
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {
                buttonOpacity();
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //function button next and prev
        btnPrev=(ImageView)findViewById(R.id.btnPrev);
        btnNext=(ImageView)findViewById(R.id.btnNext);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItem(-1), false);
                buttonOpacity();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItem(+1), false);
                buttonOpacity();
            }
        });

        //addButton onClickListner
        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPage();
                buttonOpacity();

            }
        });

        //addDelete onClickListner
        btnDel=(Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delPage();
                buttonOpacity();
            }
        });

    }

    //function add new viewPager
    public void addPage(){

        LayoutInflater inflater = LayoutInflater.from(this);//get a layout inflater
        View view = inflater.inflate(R.layout.page_1, null); //Call the inflate() method of the LayoutInflater instance to load the layout of the page.

        webView=(WebView)view.findViewById(R.id.webView_1);

        WebSettings ws= webView.getSettings();
        ws.setJavaScriptEnabled(true);//set webview readable javascript
        ws.setDefaultTextEncodingName("utf-8");

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url){
                //when webview load finish run javascript function
                webView.loadUrl("javascript:runfunction()");
            }
        });

        webView.loadUrl("file:///android_asset/webView.html");//local html path


        if(count==0)
        {
            viewList.add((viewPager.getCurrentItem()),view);

        }
        else {
            viewList.add((viewPager.getCurrentItem() + 1), view);

        }

        viewPagerAdapter.notifyDataSetChanged();//new data insert notice UI to update
        count++;

        viewPager.setCurrentItem(getItem(+1), false);//set current viewpager to new added page
        disableButtonDelete();


    }
    public void delPage(){

        int position = viewPager.getCurrentItem();//get Current pager
        viewList.remove(position);
        viewPagerAdapter.notifyDataSetChanged();
        count--;
        viewPager.setCurrentItem(getItem(-1), false);//set current viewpager to previous pager.
        disableButtonDelete();
        buttonOpacity();


    }

    private int getItem(int i) {
        //function get current item int
        return viewPager.getCurrentItem() + i;
    }

    public void disableButtonDelete(){
        //function disable delete button while no pager left and enable delete viewlist not empty
        //when viewlist is empty show No page Added message.
        TextView homeEmpty=(TextView)findViewById(R.id.homeEmptyMessage);
        btnDel=(Button) findViewById(R.id.btnDel);

        if(count==0){

            btnDel.getBackground().setAlpha(64);
            btnDel.setEnabled(false);

        }
        else{

            btnDel.getTextColors().withAlpha(255);
            btnDel.setEnabled(true);
        }

        //show no page added Message.
        if(viewList.size()==0){

            homeEmpty.setVisibility(View.VISIBLE);
        }
        else{

            homeEmpty.setVisibility(View.INVISIBLE);
        }

    }


    public void buttonOpacity() {
        //set button next and previous button opacity to 50% opacity when viewpager reach one of the end
        if (viewPager.getCurrentItem() == 0 || viewPager.getCurrentItem() == 1) {

            btnPrev.setAlpha(0.5f);

        }
        if (viewList.size() == 2) {
            if (viewPager.getCurrentItem() == 0) {
                btnPrev.setAlpha(0.5f);
                btnNext.setAlpha(1.0f);
            }
            if (viewPager.getCurrentItem() == 1) {
                btnPrev.setAlpha(1.0f);
                btnNext.setAlpha(0.5f);
            }
        }
        if (viewPager.getCurrentItem() == (viewList.size() - 1) || (viewList.size() - 1) == -1) {

            btnNext.setAlpha(0.5f);

        }
        if (viewPager.getCurrentItem() > 0 && viewPager.getCurrentItem() < viewList.size() - 1) {

            btnNext.setAlpha(1.0f);
            btnPrev.setAlpha(1.0f);

        }
    }

    private void loadFirstThreePage(int delay) {
        //apply function delay for load first three viewpager.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        addPage();
                    }
                }, delay);
    }
}
