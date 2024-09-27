package com.syahdi.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.syahdi.storyapp.R
import com.syahdi.storyapp.databinding.ActivityLoginBinding
import com.syahdi.storyapp.ui.main.MainActivity
import com.syahdi.storyapp.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupVariable()
        setupListener()
        beginAnimation()
    }

    private fun setupVariable() {
        progressBar = findViewById(R.id.progressBarLogin)
        progressBar.visibility = View.GONE

        loginViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]
    }

    private fun setupListener() {
        binding.loginButton.isEnabled = false

        binding.emailEditText.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (email == "") {
                    binding.emailEditTextLayout.error = "Masukkan email"
                } else {
                    binding.emailEditTextLayout.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                val isLengthValid = password.length >= 8
                binding.loginButton.isEnabled = isLengthValid
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.loginButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    loginViewModel.authenticateUser(email, password)
                    loginViewModel.message.observe(this) { message ->
                        when (message) {
                            "success" -> {
                                progressBar.visibility = View.GONE
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this, "Login failed, check your credentials", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        binding.registerHereTextView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun beginAnimation() {

        val titleTv = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val messageTv = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTv = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTv = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val registerTv = ObjectAnimator.ofFloat(binding.registerHereTextView, View.ALPHA, 1f).setDuration(500)

        val emailTogether = AnimatorSet().apply {
            playTogether(emailTv, emailEdit)
        }

        val passwordTogether = AnimatorSet().apply {
            playTogether(passwordTv, passwordEdit)
        }

        val buttonTogether = AnimatorSet().apply {
            playTogether(login, registerTv)
        }

        AnimatorSet().apply {
            playSequentially(titleTv, messageTv, emailTogether, passwordTogether, buttonTogether)
            start()
        }
    }

}