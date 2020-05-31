package ru.qrfication.QRFication.ui

import android.text.InputType
import ru.qrfication.QRFication.R
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes

class PassBtnController {
    companion object {
        const val TEXT_PASS = InputType.TYPE_TEXT_VARIATION_PASSWORD + 1
        const val TEXT_PASS_VISIBLE = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD + 1

        fun togglePasswordVisibility(passLine: ExtendedEditText, passLayout: TextFieldBoxes) {
            val selection = passLine.selectionEnd
            if (passLine.inputType == TEXT_PASS) {
                passLine.inputType = TEXT_PASS_VISIBLE
                passLine.setSelection(selection)
                passLayout.endIconImageButton.setImageResource(R.drawable.ic_eye_off)
            } else {
                passLine.inputType = TEXT_PASS
                passLine.setSelection(selection)
                passLayout.endIconImageButton.setImageResource(R.drawable.ic_eye)
            }
        }
    }
}