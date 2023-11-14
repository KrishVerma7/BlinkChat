package com.example.blinkchat.adapters

import PeopleFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.blinkchat.fragments.ChatFragment

class ScreenSlideAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int =2

//swipeable interface creation
    override fun createFragment(position: Int): Fragment = when(position) {
        0-> ChatFragment()  //when position = 0 show ChatFragment()
        else-> PeopleFragment() //when position != 0 show PeopleFragment()
    }

}
