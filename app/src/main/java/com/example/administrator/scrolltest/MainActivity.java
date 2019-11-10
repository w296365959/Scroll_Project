package com.example.administrator.scrolltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private PortfolioStockScrollTabAdapter mAdapter;
    private List<String> mRvList;
    private LinearLayoutManager mLayoutManager;
    private SyncHScrollView mHeadSyncScrollView;
    private RecyclerView mRv;
    private View mRvHeadLl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        findViewById(R.id.title2).setOnClickListener(this);
        mRv = findViewById(R.id.rv_view);
        mRvHeadLl = findViewById(R.id.rv_head_ll);
        mHeadSyncScrollView = findViewById(R.id.hsv_rv_title_right);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(mLayoutManager);

        mRvList = new ArrayList<>();
        for (int i = 0; i <70; i++) {
            mRvList.add("i"+i);
        }
        mAdapter = new PortfolioStockScrollTabAdapter(this, mRvList, mHeadSyncScrollView);

        mRvHeadLl.setOnTouchListener(new TitleTouchListener());//头的滑动分发给头内部的sv去滑动
       // mRv.setOnTouchListener(new RvTouchListener());//把rv的滑动给 头的sv去分发
        //改变缓存改类型item最大数，解决刷新item时由于scrollto位置移动过，而出现闪屏现象
        mRv.getRecycledViewPool().setMaxRecycledViews(0, 20);

        mRv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title2:
                startActivity(new Intent(this,HeaderActivity.class));
                break;
        }

    }

    final class TitleTouchListener implements View.OnTouchListener {
        public boolean onTouch(View arg0, MotionEvent event) {

            // 滑动后 分发联动
            mHeadSyncScrollView.onTouchEvent(event);
            return false;
        }
    }

    private float startX;
    private float startY;

    final class RvTouchListener implements View.OnTouchListener {
        boolean isIntercept = false;

        public boolean onTouch(View arg0, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    isIntercept = false;
                    mHeadSyncScrollView.onTouchEvent(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float difY = Math.abs(event.getRawY() - startY);
                    float difX = Math.abs(event.getRawX() - startX);
                    if (difY > 20 && !isIntercept) {
                        return isIntercept;
                    }
                    if (difX > 20 || isIntercept) {
                        // 滑动后 分发联动
                        isIntercept = true;
                        mHeadSyncScrollView.onTouchEvent(event);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isIntercept = false;
                    mHeadSyncScrollView.onTouchEvent(event);
                    break;
                default:
                    isIntercept = false;
                    mHeadSyncScrollView.onTouchEvent(event);
                    break;
            }
            return false;
        }
    }
}
