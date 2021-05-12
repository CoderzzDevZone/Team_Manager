package com.example.android.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.example.android.manager.databinding.ActivityMainBinding
import com.example.android.manager.fragments.AssignedTaskFragment
import com.example.android.manager.fragments.DiscussionFragment
import com.example.android.manager.fragments.TaskFragment
import com.example.android.manager.fragments.adapter.ViewPagerAdapter

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setUpTabs()
    }
    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DiscussionFragment(),"Discussion")
        adapter.addFragment(TaskFragment(),"Task List")
        adapter.addFragment(AssignedTaskFragment(),"My Commits")
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_task_list)
        binding.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_discussion)
        binding.tabs.getTabAt(2)!!.setIcon(R.drawable.ic_person_task_assigned)

    }
}