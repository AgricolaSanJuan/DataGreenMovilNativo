package com.example.datagreenmovil;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class ASJMessenger extends AppCompatActivity {
    Button btnSend;
    TextView txtRecepted;
    EditText etMessageSend;
    MediaPlayer mediaPlayer;
    Context ctx;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://superb-zigzag-behavior.glitch.me");
            mSocket.connect();
            Log.i("FINO", "DENTRO");
        } catch (URISyntaxException e) {
            Log.e("ERROR SOCKET: ", e.toString());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asjmessenger);
        btnSend = findViewById(R.id.msnBtnSend);
        txtRecepted = findViewById(R.id.msnERReceivedMessage);
        etMessageSend = findViewById(R.id.msnETMessage);
        ctx = this;
        mediaPlayer = MediaPlayer.create(ctx, R.raw.new_message);

        mSocket.on("message", onNewMessage);
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);


        btnSend.setOnClickListener(view -> {
            String username = "\n"+Build.MANUFACTURER+" "+Build.MODEL;
            String message = etMessageSend.getText().toString().trim();

            if(TextUtils.isEmpty(message)){
                return;
            }

            etMessageSend.setText("");
            txtRecepted.setText("\n"+txtRecepted.getText().toString().trim()+"\n" + username+" dice: \n"+ message);
//            mSocket.emit();
            JSONObject messageBody = new JSONObject();
            try {
                messageBody.put("username",username);
                messageBody.put("message",message);
                mSocket.emit("message",messageBody);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Exception e = (Exception) args[0];
            Log.e("SocketIO", "Error de conexión: " + e.getMessage());
            // Aquí puedes mostrar un mensaje al usuario indicando que no se pudo conectar
            // y proporcionar detalles sobre la causa del problema
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (args.length > 0) {
                        Object firstArg = args[0];
                        if (firstArg instanceof JSONObject) {
                            // Si args[0] es un JSONObject, trata los datos como un objeto JSON
                            JSONObject data = (JSONObject) firstArg;
                            try {
                                String username = data.getJSONObject("message").getString("username");
                                String message = data.getJSONObject("message").getString("message");
                                mediaPlayer.start();
                                txtRecepted.setText("\n" + txtRecepted.getText().toString().trim() + "\n" + username + " dice: \n" + message+"\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (firstArg instanceof String) {
                            // Si args[0] es una cadena, trata los datos como una cadena de texto
                            String username = "\n Web User";
                            String message = (String) firstArg;
                            mediaPlayer.start();
                            txtRecepted.setText("\n" + txtRecepted.getText().toString().trim() + "\n" + username + " dice: \n" + message+"\n");
                        }
                    }
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("message", onNewMessage);

    }

}
