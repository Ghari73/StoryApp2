package com.syahdi.storyapp.ui.customView

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText : AppCompatEditText {
    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//        filters = arrayOf<InputFilter>(LengthFilter(MIN_PASSWORD_LENGTH))

        var isLengthValid = true;
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()

                // Check for password requirements
                isLengthValid = password.length >= 8
                error = if (!isLengthValid) {
                    "Password kurang dari 8 character"
                } else {
                    null;
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    companion object {
//        private const val MIN_PASSWORD_LENGTH = 10
    }
}