package com.example.parcial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.parcial.model.Particles;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText nameText,
    particleAmount,
    xPos, yPos;

    Button redBtn, greenBtn, blueBtn;
    Button createBtn, deleteBtn;

    BufferedReader reader;
    BufferedWriter writer;

    private Socket socket;

    int r, g, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = findViewById(R.id.nameET);
        particleAmount = findViewById(R.id.amountET);
        xPos = findViewById(R.id.XET);
        yPos = findViewById(R.id.YET);
        redBtn = findViewById(R.id.redButton);
        greenBtn = findViewById(R.id.greenButton);
        blueBtn = findViewById(R.id.blueButton);
        createBtn = findViewById(R.id.createButton);
        deleteBtn = findViewById(R.id.deleteButton);

        initClient();


        createBtn.setOnClickListener(
                (view)->
                {
                    Gson gson = new Gson();

                    String name = nameText.getText().toString();
                    int amount = Integer.parseInt(particleAmount.getText().toString());
                    int x = Integer.parseInt(xPos.getText().toString());
                    int y = Integer.parseInt(yPos.getText().toString());

                    Particles particle = new Particles(x, y, name, amount, r, g, b);

                    String json = gson.toJson(particle);

                    Log.d(">>>" , json);

                    sendMessage("test message");
                }
        );

        redBtn.setOnClickListener(
                (view) ->
                {
                    r = 255;
                    g = 0;
                    b = 0;
                }
        );
        greenBtn.setOnClickListener(
                (view) ->
                {
                    r = 0;
                    g = 255;
                    b = 0;
                }
        );
        blueBtn.setOnClickListener(
                (view) ->
                {
                    r = 0;
                    g = 0;
                    b = 255;
                }
        );


    }

    public void initClient()
    {
        new Thread(
                () ->
                {
                    try {
                        System.out.println("Connecting to server...");
                        socket = new Socket("192.168.1.9", 4000);
                        System.out.println("Established connection to server");

                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        reader = new BufferedReader(isr);

                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

                        while(true)
                        {
                            System.out.println("Awaiting message...");
                            String line = reader.readLine();
                            System.out.println("Received message: " + line);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    public void sendMessage(String msg)
    {
        new Thread(
                () ->
                {
                    try {
                        writer.write(msg + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }).start();
    }

}