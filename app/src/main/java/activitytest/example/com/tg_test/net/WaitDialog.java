package activitytest.example.com.tg_test.net;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import activitytest.example.com.tg_test.R;

public class WaitDialog {

    private AlertDialog dialog;

    public WaitDialog(Context context) {
        dialog = new AlertDialog.Builder(context, R.style.AlertDialogStyle).create();
        dialog.setCancelable(false);
    }

    public void show() {
        dialog.show();
        dialog.setContentView(R.layout.waiting_dialog);
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}