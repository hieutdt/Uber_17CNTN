package com.example.cntn_grab.Helpers;

import android.app.AlertDialog;
import android.content.Context;

import com.example.cntn_grab.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class DialogHelper {
    private static DialogHelper instance;
    private DialogHelperListener listener;

    private DialogHelper() {}

    public static DialogHelper getInstance() {
        if (instance == null)
            instance = new DialogHelper();
        return instance;
    }

    public void showSuccessDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);

        builder.setTitle("THÀNH CÔNG");
        builder.setIcon(R.drawable.icon_ok);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
                listener.successDialogOnClick();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showFailedDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);

        builder.setTitle("THẤT BẠI");
        builder.setIcon(R.drawable.icon_close);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                listener.failedDialogOnClick();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showCustomDialog(final String title, final String description, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);

        builder.setTitle(title);
        builder.setMessage(description);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                listener.customDialogOnClick();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setListener(DialogHelperListener listener) {
        this.listener = listener;
    }
}

