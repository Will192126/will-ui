package com.will.hi_ui.demo.banner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import com.bumptech.glide.Glide
import com.will.hi_ui.R
import com.will.ui.banner.WiBanner
import com.will.ui.banner.core.WiBannerMo
import com.will.ui.banner.indicator.IWiIndicator
import com.will.ui.banner.indicator.WiCircleIndicator

class WiBannerActivity : AppCompatActivity() {
    private var urls = arrayOf(
        "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera7.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera8.jpeg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wi_banner)

        val autoPlay = findViewById<Switch>(R.id.auto_play)
        autoPlay.setOnCheckedChangeListener { _, isChecked ->
            initView(WiCircleIndicator(this), isChecked)
        }

        initView(WiCircleIndicator(this), false)
    }

    private fun initView(indicator: IWiIndicator<*>?, autoPlay: Boolean) {
        val banner = findViewById<WiBanner>(R.id.banner)
        val models: MutableList<WiBannerMo> = ArrayList()
        for (i in 0..7) {
            val mo = BannerMo()
            mo.url = urls[i % urls.size]
            models.add(mo)
        }
        banner.setWiIndicator(indicator)
        banner.setAutoPlay(autoPlay)
        banner.setIntervalTime(2000)
        banner.setBannerData(R.layout.banner_item_layout, models)
        banner.setBindAdapter { viewHolder, mo, position ->
            val imageView: ImageView = viewHolder.findViewById(R.id.iv_image)
            Glide.with(this@WiBannerActivity).load(mo.url).into(imageView)
        }
    }
}
