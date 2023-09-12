import android.os.Handler;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Swal {
  SweetAlertDialog sd = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);

        sd
            .setTitleText("Oops!")
            .setContentText("Imposible conectar con servidor.");

        sd.show();

  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
    @Override
    public void run() {
      sd.hide();
    }
  }, 1500);
}
