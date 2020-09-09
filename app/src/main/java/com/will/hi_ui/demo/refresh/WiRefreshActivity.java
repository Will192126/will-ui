package com.will.hi_ui.demo.refresh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.will.hi_ui.R;
import com.will.ui.refresh.IWiRefresh;
import com.will.ui.refresh.WiRefreshLayout;
import com.will.ui.refresh.WiTextOverView;

public class WiRefreshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_refresh);

        final WiRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
        WiTextOverView overView = new WiTextOverView(this);
        refreshLayout.setRefreshOverView(overView);
        refreshLayout.setRefreshListener(new IWiRefresh.RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.refreshFinished();
                    }
                }, 1000);
            }

            @Override
            public boolean enableRefresh() {
                return true;
            }
        });
        refreshLayout.setDisableRefreshScroll(false);
    }
}
