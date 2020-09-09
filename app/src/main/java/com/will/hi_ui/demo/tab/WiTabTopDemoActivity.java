package com.will.hi_ui.demo.tab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.will.hi_ui.R;
import com.will.ui.tab.common.IHiTabLayout;
import com.will.ui.tab.top.WiTabTopInfo;
import com.will.ui.tab.top.WiTabTopLayout;

import java.util.ArrayList;
import java.util.List;

public class WiTabTopDemoActivity extends AppCompatActivity {
    String[] tabsStr = new String[]{
            "热门",
            "服装",
            "数码",
            "鞋子",
            "零食",
            "家电",
            "汽车",
            "百货",
            "家居",
            "装修",
            "运动"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_tab_top_demo);

        initTabTopLayout();
    }

    private void initTabTopLayout() {
        WiTabTopLayout tabTopLayout = findViewById(R.id.tab_top_layout);
        int defaultColor = getResources().getColor(R.color.tabBottomDefaultColor);
        int tintColor = getResources().getColor(R.color.tabBottomTintColor);
        final List<WiTabTopInfo<?>> infoList = new ArrayList<>();
        for (String str : tabsStr) {
            WiTabTopInfo<?> info = new WiTabTopInfo<>(str, defaultColor, tintColor);
            infoList.add(info);
        }
        tabTopLayout.inflateInfo(infoList);
        tabTopLayout.addTabSelectedChangeListener(new IHiTabLayout.OnTabSelectedListener<WiTabTopInfo<?>>() {
            @Override
            public void onTabSelectedChange(int index, @Nullable WiTabTopInfo<?> preInfo, @NonNull WiTabTopInfo<?> nextInfo) {
                Toast.makeText(WiTabTopDemoActivity.this, nextInfo.name, Toast.LENGTH_SHORT).show();
            }
        });
        tabTopLayout.defaultSelected(infoList.get(0));
    }
}
