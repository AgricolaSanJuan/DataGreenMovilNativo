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
            mSocket = IO.socket("https://e761-190-116-184-206.ngrok.io");
        } catch (URISyntaxException e) {}
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

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        Log.i("user",data.toString());
                        username = "\n"+data.getJSONObject("message").getString("username");
                        message = data.getJSONObject("message").getString("message");
                    } catch (JSONException e) {
                        return;
                    }
                    // add the message to view
                    // Inicializa el MediaPlayer con el archivo de audio

                    // Reproduce el audio
                    mediaPlayer.start();
                    txtRecepted.setText("\n"+txtRecepted.getText().toString().trim()+"\n" + username+" dice: \n"+ message);
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
