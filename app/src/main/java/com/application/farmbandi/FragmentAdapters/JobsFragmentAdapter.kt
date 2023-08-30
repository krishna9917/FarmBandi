package com.application.farmbandi.FragmentAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.farmbandi.Fragments.CompletedJob.CompletedJobFragment
import com.application.farmbandi.Fragments.OnGoingJobs.OnGoingJobsFragment
import com.application.farmbandi.Fragments.UpComingJobs.UpcomingJobsFragment

class JobsFragmentAdapter(fa: FragmentActivity): FragmentStateAdapter(fa)
{

    override fun getItemCount(): Int {
       return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            1-> OnGoingJobsFragment()
            2-> CompletedJobFragment()
            else-> UpcomingJobsFragment()
        }
    }

}