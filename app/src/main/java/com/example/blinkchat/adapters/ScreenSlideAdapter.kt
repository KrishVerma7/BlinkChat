package com.example.blinkchat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.blinkchat.fragments.InboxFragment
import com.example.blinkchat.fragments.PeopleFragment

class ScreenSlideAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int =2

//swipeable interface creation
    override fun createFragment(position: Int): Fragment = when(position) {
        0-> InboxFragment()  //when position = 0 show InboxFragment()
        else-> PeopleFragment() //when position != 0 show PeopleFragment()
    }

}
