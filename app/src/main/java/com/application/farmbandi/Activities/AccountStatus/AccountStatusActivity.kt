package com.application.farmbandi.Activities.AccountStatus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.application.farmbandi.Activities.Home.HomeActivity
import com.application.farmbandi.Activities.Login.LoginActivity
import com.application.farmbandi.Adapters.DocumentsAdapter
import com.application.farmbandi.AppInterfaces.CallBackListener
import com.application.farmbandi.AppInterfaces.DialogClickListener
import com.application.farmbandi.AppInterfaces.RecyclerViewClickListener
import com.application.farmbandi.PopUpDialogs.ShowAlert
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.databinding.ActivityAccountStatusBinding


class AccountStatusActivity : AppCompatActivity(), RecyclerViewClickListener, View.OnClickListener,
    DialogClickListener {
    private val binding by lazy {
        ActivityAccountStatusBinding.inflate(layoutInflater)
    }

    private val repository by lazy {
        AccountStatusRepository(this)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            AccountStatusViewModelFactory(repository)
        )[AccountStatusViewModel::class.java]
    }

    private val documentsAdapter by lazy {
        DocumentsAdapter(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        intializer()
    }

    private fun intializer() {
        binding.txtLoginAnotherAccount.setOnClickListener(this)
        binding.rvDocuments.adapter = documentsAdapter
        binding.llRefresh.setOnRefreshListener {
            repository.getAccountStatus()
        }

        dataObserver()
    }

    private fun dataObserver() {
        viewModel.accountStatusResponse.observe(this) {
            binding.llRefresh.isRefreshing=false
            if (it.status) {
                binding.data = it
                documentsAdapter.submitDocumentList(it.documents)
                if(it.driver.status==2)
                {
                   binding.imgStatusGif.setImageDrawable(getDrawable(R.drawable.img_doc_verified))
                    Handler(Looper.myLooper()!!).postDelayed({
                        MyLocalMemory.putObject(MyApp.MySingleton.DRIVER_DETAILS, it.driver)
                    },1000)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                }else if(it.driver.status==0)
                {
                    binding.imgStatusGif.setImageDrawable(getDrawable(R.drawable.img_doc_rejected))
                }
            }
        }

    }

    override fun onClick(
        data: Any,
        selectedPosition: Int,
        click_layout_code: Int,
        callBack: RecyclerViewClickListener?
    ) {

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.txtLoginAnotherAccount -> {
                ShowAlert(
                    "Confirm login..!!",
                    "Are you sure, You want to login with another account!",
                    this,
                    this,
                    R.drawable.img_login
                ).show()
            }
        }
    }

    override fun onClick(clickCode: Int, data: Any?, callBack: CallBackListener?) {
        when (clickCode) {
            1022 -> {
                MyLocalMemory.clearMemory()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }
}