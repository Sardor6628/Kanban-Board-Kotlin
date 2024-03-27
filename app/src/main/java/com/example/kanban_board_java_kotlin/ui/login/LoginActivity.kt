package com.example.kanban_board_java_kotlin.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.kanban_board_java_kotlin.R
import com.example.kanban_board_java_kotlin.app.AppConstant
import com.example.kanban_board_java_kotlin.app.BaseActivity
import com.example.kanban_board_java_kotlin.app.KanbanBoardApp
import com.example.kanban_board_java_kotlin.databinding.ActivityLoginBinding
import com.example.kanban_board_java_kotlin.ui.home.HomeActivity
import com.example.kanban_board_java_kotlin.utils.formatSecondToString
import com.example.kanban_board_java_kotlin.utils.formatString
import com.example.kanban_board_java_kotlin.utils.resource.Status
import com.example.kanban_board_java_kotlin.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    private var googleSignInClient: GoogleSignInClient? = null

    private var googleSignInAccount: GoogleSignInAccount? = null

    private var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleObserver()
        initGoogleSignInClient()
        addTextChangedWatcher()
        initView()
        updateUI()
        setClick()
    }

    private fun addTextChangedWatcher() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!isValidEmail(p0)) {
                    binding.etEmail.error = "Enter a valid email"
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length < 6) {
                    binding.etPassword.error = "Enter min. 6 characters"
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun initView() {
        status = intent.getIntExtra(AppConstant.LOGIN_KEY, 0)
    }

    private fun updateUI() {
        if (status == 0) {
            binding.tvTitle.text = getString(R.string.sign_in)
            binding.btnSignIn.text = getString(R.string.sign_in)
            binding.btnSignUpNow.text = getString(R.string.sign_up_now)
        } else {
            binding.tvTitle.text = getString(R.string.sign_up)
            binding.btnSignIn.text = getString(R.string.sign_up)
            binding.btnSignUpNow.text = getString(R.string.sign_in_hear)
        }
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun signInGoogle() {
        if (googleSignInClient != null) {
            val signInIntent: Intent = googleSignInClient!!.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val singInData = result.data
                if (singInData != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(singInData)
                    try {
                        task.getResult(ApiException::class.java)?.let {
                            googleSignInAccount = it
                            getGoogleAuthCredential(it)
                        }
                    } catch (e: ApiException) {
                        KanbanBoardApp.getAppInstance().showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }
            }
        }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        viewModel.signInWithGoogle(googleAuthCredential)
    }

    private fun googleObserver() {
        viewModel.googleSignInNetworkState().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideProgress()
                    if (it.data != null) {
                        val user = FirebaseAuth.getInstance().currentUser
                        prefsUtils.setUseId(user?.uid.toString())
                        prefsUtils.setLogin(true)

                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                Status.ERROR -> {
                    KanbanBoardApp.getAppInstance().showToast(it.error.toString())
                    hideProgress()
                }

                Status.LOADING -> {
                    showProgress()
                }

                else -> {
                    KanbanBoardApp.getAppInstance().showToast(it.error.toString())
                    hideProgress()
                }
            }
        }
    }

    private fun setClick() {
        binding.btnSignIn.setOnClickListener(this)
        binding.btnSignUpNow.setOnClickListener(this)
        binding.btnSignInGoogle.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnSignIn -> {
                if (isValidEmail(binding.etEmail.text.toString())) {
                    if (binding.etPassword.text.toString().length >= 6) {
                        if (status == 0) {
                            viewModel.signInWithEmail(
                                binding.etEmail.text.toString(),
                                binding.etPassword.text.toString()
                            )
                        } else {
                            viewModel.signUpWithEmail(
                                binding.etEmail.text.toString(),
                                binding.etPassword.text.toString()
                            )
                        }
                    } else {
                        KanbanBoardApp.getAppInstance().showToast("Please enter password")
                    }
                } else {
                    KanbanBoardApp.getAppInstance().showToast("Please enter email")
                }
            }

            binding.btnSignInGoogle -> {
                signInGoogle()
            }

            binding.btnSignUpNow -> {
                val intent = if (status == 0) {
                    Intent(this, LoginActivity::class.java).apply {
                        putExtra(AppConstant.LOGIN_KEY, 1)
                    }
                } else {
                    Intent(this, LoginActivity::class.java).apply {
                        putExtra(AppConstant.LOGIN_KEY, 0)
                    }
                }
                startActivity(intent)
                finish()
            }
        }
    }

}