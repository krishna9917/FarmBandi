package com.application.farmbandi.Fragments.OnGoingJobs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.application.farmbandi.Adapters.JobsAdapter
import com.application.farmbandi.Activities.Home.HomeRepository
import com.application.farmbandi.Activities.Home.HomeViewModel
import com.application.farmbandi.Activities.Home.HomeViewModelFactory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.databinding.FragmentOnGoingJobsFragmentsBinding
import com.application.farmbandi.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class OnGoingJobsFragment : Fragment() {
    private val binding by lazy {
        FragmentOnGoingJobsFragmentsBinding.inflate(layoutInflater)
    }

    private val jobsAdapter by lazy {
        JobsAdapter()
    }
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            binding.llRefresh.isRefreshing = false

            if(dataSnapshot.exists())
            {
                val upcomingOrders = ArrayList<Order?>()
                for (orderSnapshot in dataSnapshot.children)
                {
                    val order = orderSnapshot.getValue(Order::class.java)
                    upcomingOrders.add(order)

                }
                jobsAdapter.differ.submitList(upcomingOrders)
            }
            binding.showEmptyLayout = jobsAdapter.itemCount==0
        }
        override fun onCancelled(databaseError: DatabaseError) {
            binding.showEmptyLayout = jobsAdapter.itemCount==0
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initializer()
        return binding.root
    }


    private fun initializer() {
        binding.rvJobs.adapter=jobsAdapter
        binding.llRefresh.isRefreshing = true
        binding.llRefresh.setOnRefreshListener {
            MyApp.MySingleton.getOrderFirebaseDb().child("ongoing_orders").removeEventListener(valueEventListener)
            MyApp.MySingleton.getOrderFirebaseDb().child("ongoing_orders").addValueEventListener(valueEventListener)
        }
        dataObserver()
    }


    private fun dataObserver() {
        MyApp.MySingleton.getOrderFirebaseDb().child("ongoing_orders").addValueEventListener(valueEventListener)
    }


}