package com.syahdi.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.syahdi.storyapp.R
import com.syahdi.storyapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        progressBar = findViewById(R.id.progressBarRegister)
        progressBar.visibility = View.GONE
    }

    private fun setupViewModel() {
        signUpViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SignUpViewModel::class.java]
    }

    private fun setupAction() {
        binding.signupButton.isEnabled = false

        binding.nameEditText.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString()
                if (name == "") {
                    binding.nameEditTextLayout.error = "Masukkan nama"
                } else {
                    binding.nameEditTextLayout.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.emailEditText.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (email == "") {
                    binding.emailEditTextLayout.error = "Masukkan email"
                } else {
                    binding.emailEditTextLayout.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                val isLengthValid = password.length >= 8

                binding.signupButton.isEnabled = isLengthValid
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.signupButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.nameEditTextLayout.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    progressBar.visibility = View.GONE
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    signUpViewModel.saveUser(name, email, password)
                    progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akunnya sudah jadi nih. Yuk, login dan belajar coding.")
                        setPositiveButton("Lanjut") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTv = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTv = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTv = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTv = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        val nameTogether = AnimatorSet().apply {
            playTogether(nameTv, nameEdit)
        }

        val emailTogether = AnimatorSet().apply {
            playTogether(emailTv, emailEdit)
        }

        val passwordTogether = AnimatorSet().apply {
            playTogether(passwordTv, passwordEdit)
        }

        AnimatorSet().apply {
            playSequentially(titleTv, nameTogether, emailTogether, passwordTogether, signup)
            start()
        }
    }
}