package com.project.financemanager.exceptions;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

public class NoConnectivityException extends Exception{
    public NoConnectivityException(String message) {
//        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        super(message);
    }
}
