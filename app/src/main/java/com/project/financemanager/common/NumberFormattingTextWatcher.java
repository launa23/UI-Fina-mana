package com.project.financemanager.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberFormattingTextWatcher implements TextWatcher {
    private EditText editText;
    private DecimalFormat decimalFormat;

    public NumberFormattingTextWatcher(EditText editText) {
        this.editText = editText;
        this.decimalFormat = (DecimalFormat) NumberFormat.getInstance();
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String originalText = s.toString();
        if (originalText.isEmpty()) {
            return;
        }
        String cleanText = originalText.replaceAll("[^\\d]", "");

        try {
            String formattedText = decimalFormat.format(Double.parseDouble(cleanText));
            editText.removeTextChangedListener(this);
            editText.setText(formattedText);
            editText.setSelection(formattedText.length());
            editText.addTextChangedListener(this);
        } catch (NumberFormatException e) {
        }
    }
}
