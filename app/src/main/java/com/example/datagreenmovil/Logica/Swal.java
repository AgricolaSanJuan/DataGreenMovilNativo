package com.example.datagreenmovil.Logica;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.datagreenmovil.Entidades.Tareo;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Scanner.ui.ScannerFragment;
import com.example.datagreenmovil.databinding.DialogDetalleTareoBinding;
import com.example.datagreenmovil.ui.TareosMain.Dialogs.DialogDetalleTareo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Swal {
  public static void error(Context ctx, String title, String body, Number delay) {
    SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE);
    sd
        .setTitleText(title)
        .setContentText(body);
    sd.show();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        sd.hide();
      }
    }, delay.longValue());
  }

  public static void success(Context ctx, String title, String body, Number delay) {
    SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE);
    sd
        .setTitleText(title)
        .setContentText(body);
    sd.show();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        sd.hide();
      }
    }, delay.longValue());
  }

  public static void info(Context ctx, String title, String body, Number delay) {
    SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.NORMAL_TYPE);
    sd
        .setTitleText(title)
        .setContentText(body);
    sd.show();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        sd.hide();
      }
    }, delay.longValue());
  }

  public static void warning(Context ctx, String title, String body, Number delay) {
    SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE);
    sd
        .setTitleText(title)
        .setContentText(body);
    sd.show();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        sd.hide();
      }
    }, delay.longValue());
  }

  public static SweetAlertDialog confirm(Context ctx, String title, String body) {
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.NORMAL_TYPE);
    sweetAlertDialog
            .setTitleText(title)
            .setContentText(body)
            .setConfirmText("Si")
            .setCancelText("No")
            .show();
    return sweetAlertDialog;
  }

  public static SweetAlertDialog confirm(Context ctx, String title, View view) {
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.NORMAL_TYPE);
    sweetAlertDialog
            .setTitleText(title)
            .setConfirmText("Si")
            .setCancelText("No")
            .setCustomView(view)
            .show();

    return sweetAlertDialog;
  }

  public static SweetAlertDialog redirectioner(Context ctx, String title, String body){
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
    sweetAlertDialog.getProgressHelper().setBarColor(ctx.getColor(R.color.azulSuave));
    sweetAlertDialog.setCancelable(false);
    sweetAlertDialog
            .setTitleText(title)
            .setContentText(body)
            .show();

    return sweetAlertDialog;
  }

  public interface PasswordValidationCallback {
    void onPasswordValidated(boolean isValid, SweetAlertDialog sweetAlertDialog);
  }

  public static void settingsPassword(Context ctx, PasswordValidationCallback callback) {
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
    View custom = LayoutInflater.from(ctx).inflate(R.layout.dialog_settings_password, null);
    sweetAlertDialog.setCustomView(custom);

    sweetAlertDialog.hideConfirmButton();
    custom.findViewById(R.id.btnAceptar).setOnClickListener(view -> {
      EditText inputPassword = custom.findViewById(R.id.inputPassword);
      String enteredPassword = inputPassword.getText().toString();

      boolean isValid = enteredPassword.equals("1598753");

      if (callback != null) {
        callback.onPasswordValidated(isValid, sweetAlertDialog);
      }

    });

    sweetAlertDialog.show();
  }

  public interface ResultScan{
    void onResultScan(String result, SweetAlertDialog sweetAlertDialog);
  }

  public static void scanDialog(Context ctx, ResultScan resultScan){
    String resultado = "";
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
    View custom = LayoutInflater.from(ctx).inflate(R.layout.dialog_scanner, null);
    sweetAlertDialog.setCustomView(custom);

    // Obtén el FragmentManager desde la Activity (asegúrate de que el contexto sea una Activity)
    if (ctx instanceof AppCompatActivity) {
      AppCompatActivity activity = (AppCompatActivity) ctx;
      FragmentManager fragmentManager = activity.getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      ScannerFragment scannerFragment = new ScannerFragment();

      // Agrega el ScannerFragment al contenedor dentro del SweetAlertDialog
      fragmentTransaction.replace(R.id.fragment_container, scannerFragment);
      fragmentTransaction.commit();
    }

    sweetAlertDialog.hideConfirmButton();

    if (resultScan != null) {
      resultScan.onResultScan(resultado, sweetAlertDialog);
    }

    sweetAlertDialog.show();
  }

  public interface ActionResult{
    void onActionResult(String result, SweetAlertDialog sweetAlertDialog);
  }

  public interface DismissDialog{
    void onDismissDialog(boolean success, String message);
  }

  public static void customDialog(Context ctx, String accion, Tareo tareoActual, ArrayList<Integer> listaTrabajadores, ActionResult actionResult, DismissDialog dismissDialog){
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
    View custom = LayoutInflater.from(ctx).inflate(R.layout.dialog_detalle_tareo, null);

    DialogDetalleTareoBinding binding = DialogDetalleTareoBinding.bind(custom);

    // Llama al método para configurar la vista
    DialogDetalleTareo.configureView(binding, accion, tareoActual, listaTrabajadores, actionResult, sweetAlertDialog, dismissDialog);

    sweetAlertDialog.setCustomView(custom);
    sweetAlertDialog.hideConfirmButton();
    sweetAlertDialog.show();
  }


}
