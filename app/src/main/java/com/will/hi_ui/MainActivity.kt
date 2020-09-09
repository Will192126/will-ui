package com.will.hi_ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.will.hi_ui.demo.banner.WiBannerActivity
import com.will.hi_ui.demo.refresh.WiRefreshActivity
import com.will.hi_ui.demo.tab.WiTabBottomDemoActivity
import com.will.hi_ui.demo.tab.WiTabTopDemoActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvTabBottomDemo = findViewById<TextView>(R.id.tab_bottom_demo)
        tvTabBottomDemo.setOnClickListener(this)
        val tvTabTopDemo = findViewById<TextView>(R.id.tab_top_demo)
        tvTabTopDemo.setOnClickListener(this)
        val tvRefreshDemo = findViewById<TextView>(R.id.refresh_demo)
        tvRefreshDemo.setOnClickListener(this)
        val tvBannerDeom = findViewById<TextView>(R.id.banner_demo)
        tvBannerDeom.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tab_bottom_demo -> {
                startActivity(Intent(this, WiTabBottomDemoActivity::class.java))
            }
            R.id.tab_top_demo -> {
                startActivity(Intent(this, WiTabTopDemoActivity::class.java))
            }
            R.id.refresh_demo -> {
                startActivity(Intent(this, WiRefreshActivity::class.java))
            }
            R.id.banner_demo -> {
                startActivity(Intent(this, WiBannerActivity::class.java))
            }
        }
    }
}
