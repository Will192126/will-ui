package com.will.hi_ui.demo.tab

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.will.hi_ui.R
import com.will.library.util.WiDisplayUtil
import com.will.ui.tab.bottom.WiTabBottomInfo
import com.will.ui.tab.bottom.WiTabBottomLayout

class WiTabBottomDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wi_tab_bottom_demo)

        initTabBottom()
    }

    private fun initTabBottom() {
        val tabBottomLayout: WiTabBottomLayout = findViewById(R.id.tab_bottom_layout)
        tabBottomLayout.setTabAlpha(0.85f)
        val bottomInfoList:MutableList<WiTabBottomInfo<*>> = ArrayList()

        val homeInfo = WiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )

        val recommendInfo = WiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#ff656667",
            "#ffd44949"
        )

//        val profileInfo = HiTabBottomInfo(
//            "首页",
//            "fonts/iconfont.ttf",
//            getString(R.string.if_profile),
//            null,
//            "#ff656667",
//            "#ffd44949"
//        )

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire, null)
        val profileInfo = WiTabBottomInfo<String>(
            "首页",
            bitmap,
            bitmap
        )

        val categoryInfo = WiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_category),
            null,
            "#ff656667",
            "#ffd44949"
        )

        val favoriteInfo = WiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#ff656667",
            "#ffd44949"
        )

        bottomInfoList.add(homeInfo)
        bottomInfoList.add(recommendInfo)
        bottomInfoList.add(profileInfo)
        bottomInfoList.add(categoryInfo)
        bottomInfoList.add(favoriteInfo)

        tabBottomLayout.inflateInfo(bottomInfoList)
        tabBottomLayout.addTabSelectedChangeListener {_, _, nextInfo ->
            Toast.makeText(this@WiTabBottomDemoActivity, nextInfo.name, Toast.LENGTH_LONG).show()
        }
        tabBottomLayout.defaultSelected(homeInfo)

        val tabBottom = tabBottomLayout.findTab(profileInfo)
        tabBottom?.apply { resetHeight(WiDisplayUtil.dp2px(66f, resources)) }
    }
}
