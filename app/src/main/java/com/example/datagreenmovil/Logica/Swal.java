package com.example.datagreenmovil.Logica;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.datagreenmovil.DAO.Tareo.TrxTareo.Tareo;
import com.example.datagreenmovil.DAO.Tareo.TrxTareosDetalle.TareoDetalles;
import com.example.datagreenmovil.Logica.Dialogs.FilterDialog;
import com.example.datagreenmovil.R;
import com.example.datagreenmovil.Scanner.ui.ScannerFragment;
import com.example.datagreenmovil.databinding.DialogDetalleTareoBinding;
import com.example.datagreenmovil.databinding.DialogFiltrosBinding;
import com.example.datagreenmovil.ui.TareosMain.Dialogs.DialogDetalleTareo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

//  public interface ActionResult{
//    void onActionResult(String result, SweetAlertDialog sweetAlertDialog);
//  }

  public interface DismissDialog{
    void onDismissDialog(boolean success, String message, SweetAlertDialog sweetAlertDialog);
  }

  public static void customDialog(Context ctx, String accion, List<TareoDetalles> tareoActual, ArrayList<Integer> listaTrabajadores, /*ActionResult actionResult,*/ DismissDialog dismissDialog){
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
    View custom = LayoutInflater.from(ctx).inflate(R.layout.dialog_detalle_tareo, null);

    DialogDetalleTareoBinding binding = DialogDetalleTareoBinding.bind(custom);

    // Llama al método para configurar la vista
    DialogDetalleTareo.configureView(binding, accion, tareoActual, listaTrabajadores, sweetAlertDialog, dismissDialog);

    sweetAlertDialog.setCustomView(custom);
    sweetAlertDialog.hideConfirmButton();
    sweetAlertDialog.show();
  }

//  FUNCIONES PARA EL POPUP DE FILTROS

  public static class DialogResult{
    String mensaje = "", estado = "", anio = "", semana = "", desde = "", hasta = "";
    int idEstado = -1;

    public int getIdEstado() {
      return idEstado;
    }

    public void setIdEstado(int idEstado) {
      this.idEstado = idEstado;
    }

    public String getMensaje() {
      return mensaje.isEmpty() ? "" : mensaje;
    }

    public void setMensaje(String mensaje) {
      this.mensaje = mensaje;
    }

    public String getEstado() {
      return estado.isEmpty() ? "" : estado;
    }

    public void setEstado(String estado) {
      this.estado = estado;
    }

    public String getAnio() {
      return anio.isEmpty() ? "" : anio;
    }

    public void setAnio(String anio) {
      this.anio = anio;
    }

    public String getSemana() {
      return semana.isEmpty() ? "" : semana;
    }

    public void setSemana(String semana) {
      this.semana = semana;
    }

    public String getDesde() {
      return desde.isEmpty() ? "" : desde;
    }

    public void setDesde(String desde) {
      this.desde = desde;
    }

    public String getHasta() {
      return hasta.isEmpty() ? "" : hasta;
    }

    public void setHasta(String hasta) {
      this.hasta = hasta;
    }
  }
  public interface FilterDialogResult{
    void onFilterDialogResult(DialogResult dialogResult, SweetAlertDialog sweetAlertDialog);
  }
  public interface FilterOpenDialog{
    void onOpenFilterDialog(JSONArray filterOpenDialogParams, SweetAlertDialog sweetAlertDialog);
  }
  public interface FilterCloseDialog{
    void onDismissFilterDialog(JSONArray filterCloseDialogParams, SweetAlertDialog sweetAlertDialog);
  }
  public static void filterDialog(Context ctx,
                                  boolean vistaEstados,
                                  boolean vistaFechasPeriodo,
                                  boolean vistaFechasRango,
                                  JSONObject props,
                                  FilterOpenDialog filterOpenDialog,
                                  FilterCloseDialog filterCloseDialog,
                                  FilterDialogResult filterDialogResult
                                  ) throws JSONException {
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
    View custom = LayoutInflater.from(ctx).inflate(R.layout.dialog_filtros, null);

    DialogFiltrosBinding binding = DialogFiltrosBinding.bind(custom);

    // Llama al método para configurar la vista
    FilterDialog.configureView(binding,
            vistaEstados,
            vistaFechasPeriodo,
            vistaFechasRango,
            props,
            filterOpenDialog,
            filterCloseDialog,
            filterDialogResult,
            sweetAlertDialog);

    sweetAlertDialog.setCustomView(custom);
    sweetAlertDialog.hideConfirmButton();
    sweetAlertDialog.show();
  }


}
