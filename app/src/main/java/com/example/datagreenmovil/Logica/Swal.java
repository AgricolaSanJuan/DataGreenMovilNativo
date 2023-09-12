package com.example.datagreenmovil.Logica;

import android.content.Context;
import android.os.Handler;

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
    }, (long) delay);
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
    }, (long) delay);
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
    }, (long) delay);
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
    }, (long) delay);
  }
}
