package com.application.farmbandi.Activities.PasswordLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.application.farmbandi.Activities.AccountStatus.AccountStatusActivity
import com.application.farmbandi.Activities.Home.HomeActivity
import com.application.farmbandi.Activities.Register.RegisterActivity.Companion.emailValidator
import com.application.farmbandi.Activities.Register.RegisterActivity.Companion.passwordValidator
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.Utils.UtilsFunction
import com.application.farmbandi.databinding.ActivityLoginWithPasswordBinding

class LoginWithPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy {
        ActivityLoginWithPasswordBinding.inflate(layoutInflater)
    }
    private val repository by lazy {
        PasswordLoginRepository(this)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            PasswordLoginViewModelFactory(repository)
        )[PasswordLoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer()
    }

    private fun initializer() {
        binding.llLoginBtn.btn.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)

        setDataObserver()

    }

    private fun setDataObserver() {

        viewModel.loginResponse.observe(this) {
            binding.isProgress = false
            if (it.status) {
                MyLocalMemory.putString(MyApp.MySingleton.USER_TOKEN, it.access_token)
                MyLocalMemory.putBoolean(
                    MyApp.MySingleton.IS_LOGIN,
                    true
                )
                MyLocalMemory.putObject(MyApp.MySingleton.DRIVER_DETAILS, it.driver)
                if (it.driver?.status == 2)
                {
                        startActivity(Intent(this, HomeActivity::class.java))

                } else {
                    startActivity(Intent(this, AccountStatusActivity::class.java))
                }
                finishAffinity()

            } else {
                UtilsFunction.errorToast(this, it.message)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn -> {
                if (emailValidator(binding.edtEmail) && passwordValidator(binding.edtPassword)) {
                    binding.isProgress = true
                    repository.login(
                        binding.edtEmail.text.toString(),
                        binding.edtPassword.text.toString()
                    )
                } else {
                    UtilsFunction.vibration(this)
                }
            }

            R.id.imgBack -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }


}