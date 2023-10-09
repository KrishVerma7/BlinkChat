package com.example.blinkchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.blinkchat.R
import com.example.blinkchat.adapters.ScreenSlideAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabs:TabLayout = findViewById(R.id.tabs)

        viewPager.adapter = ScreenSlideAdapter(this)
        TabLayoutMediator(tabs,viewPager,
            TabLayoutMediator.TabConfigurationStrategy{ tab:TabLayout.Tab,pos:Int->
                when(pos){
                    0->tab.text = "CHATS"
                    1->tab.text = "PEOPLE"
                }
            }).attach()
    }

}