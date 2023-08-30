package com.application.farmbandi.Activities.OrderDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.application.farmbandi.R
import com.application.farmbandi.Utils.UtilsFunction
import com.application.farmbandi.databinding.ActivityOrderDetailBinding


class OrderDetailActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy {
        ActivityOrderDetailBinding.inflate(layoutInflater)
    }

    private val repository by lazy {
        OrderDetailRepository(this)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            OrderDetailViewModelFactory(intent.getStringExtra("Job_id").toString(), repository)
        )[OrderDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer()
    }

    private fun initializer() {
        binding.btnAccept.setOnClickListener(this)
        binding.btnReject.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        setObserver()
    }

    private fun setObserver() {
        viewModel.orderDetailResponse.observe(this) {
            if(it.status)
            {
                binding.orderData = it.order_details
            }else
            {
                UtilsFunction.errorToast(this,it.message)
                finish()
            }
        }
        viewModel.updateRequestResponse.observe(this) {
                if(it.status)
                {
                  binding.cvBottom.visibility=View.GONE
                }else
                {
                  UtilsFunction.errorToast(this,it.message)
                }
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgBack -> onBackPressedDispatcher.onBackPressed()
            R.id.btnAccept -> repository.updateRequestStatus(
                intent.getStringExtra("Job_id").toString(), "1"
            )
            R.id.btnReject -> repository.updateRequestStatus(
                intent.getStringExtra("Job_id").toString(), "0"
            )
        }
    }
}