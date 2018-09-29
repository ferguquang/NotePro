package com.ngo.ducquang.notepro.base.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngo.ducquang.notepro.R;

/**
 * Created by ducqu on 7/28/2018.
 */

public class ConfirmDialog extends DialogFragment {
    private OnConfirmDialogAction listener;

    public static ConfirmDialog initialize(String message, OnConfirmDialogAction listener)
    {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setOnConfirmDialogAction(listener);
        Bundle mBundle = new Bundle();
        mBundle.putString("message", message);
        dialog.setArguments(mBundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final String message = getArguments().getString("message");
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Thông báo")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
                        if (listener != null)
                        {
                            listener.onAccept();
                        }

                        if (getDialog() != null)
                        {
                            getDialog().dismiss();
                        }
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
                        if (listener != null)
                        {
                            listener.onCancel();
                        }
                    }
                }).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        setCancelable(false);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setOnConfirmDialogAction(OnConfirmDialogAction listener)
    {
        this.listener = listener;
    }
}
