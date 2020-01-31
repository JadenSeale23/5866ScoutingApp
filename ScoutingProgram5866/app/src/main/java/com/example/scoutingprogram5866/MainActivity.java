package com.example.scoutingprogram5866;

//imports
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.WriterException;
import java.io.IOException;

//This class handles the Mainactivity GUI and all the components on it
public class MainActivity extends AppCompatActivity {

    Bitmap qr;
    TextView teamName1, teamNum1, varA1, varB1, varC1, varD1, varE1;
    EditText teamNum, teamName, varA, varB, notes;
    CheckBox varC, varD, varE;
    ImageView imageView;
    Button button, button2;
    String in;
    android.support.constraint.ConstraintLayout qrLayout;

    //handles what is done when the class is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureNextButton();
        configureBackButton();

        //gets all the components on the screen
        qrLayout = findViewById(R.id.qrLayout);
        qrLayout.setVisibility(View.GONE);
        teamNum = (EditText) findViewById(R.id.TeamNuminput);
        teamName = (EditText) findViewById(R.id.teamNameinput);
        varA = (EditText) findViewById(R.id.varain);
        varB = (EditText) findViewById(R.id.varbin);
        varC = (CheckBox) findViewById(R.id.checkBox2);
        varD = (CheckBox) findViewById(R.id.checkBox3);
        varE = (CheckBox) findViewById(R.id.checkBox4);
        notes = (EditText) findViewById(R.id.notesin);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setVisibility(View.GONE);
        teamName1 = (TextView) findViewById(R.id.teamName);
        teamNum1 = (TextView) findViewById(R.id.teamNum);
        varA1 = (TextView) findViewById(R.id.vara);
        varB1 = (TextView) findViewById(R.id.varb);
        varC1 = (TextView) findViewById(R.id.varc);
        varD1 = (TextView) findViewById(R.id.vard);
        varE1 = (TextView) findViewById(R.id.vare);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button2.setVisibility(View.GONE);
    }

    //The submit button will go to the next screen and generate a QRCode with the data given
    public void configureNextButton() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                {

                    //try to create a QRCode witht he data supplied
                    try {
                        in = "Team num: " + teamNum.getText().toString() +
                                "\nTeam name: " + teamName.getText().toString() +
                                "\nBalls Scored: " + varA.getText().toString() +
                                "\nStage: " + varB.getText().toString() +
                                "\nRotation Control: " + varC.isChecked() + "" +
                                "\nPosition Control: " + varD.isChecked() + "" +
                                "\nHang: " + varE.isChecked() + "" +
                                "\nNotes: " + notes.getText().toString();
                        QRCodeGenerator qrGen = new QRCodeGenerator(in, 350, 350);
                        qr = qrGen.getQRCodeImage();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //switch to the next screen
                    qrLayout.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(qr);
                    imageView.setVisibility(View.VISIBLE);
                    teamNum.setVisibility(View.GONE);
                    teamName.setVisibility(View.GONE);
                    teamName1.setVisibility(View.GONE);
                    teamNum1.setVisibility(View.GONE);
                    varA.setVisibility(View.GONE);
                    varA1.setVisibility(View.GONE);
                    varB.setVisibility(View.GONE);
                    varB1.setVisibility(View.GONE);
                    varC.setVisibility(View.GONE);
                    varC1.setVisibility(View.GONE);
                    varD.setVisibility(View.GONE);
                    varD1.setVisibility(View.GONE);
                    varE.setVisibility(View.GONE);
                    varE1.setVisibility(View.GONE);
                    notes.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    button2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //The back button will go back to the data logging screen
    public void configureBackButton() {
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                {
                    //go to the data logging screen
                    qrLayout.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    teamNum.setVisibility(View.VISIBLE);
                    teamName.setVisibility(View.VISIBLE);
                    teamNum1.setVisibility(View.VISIBLE);
                    teamName1.setVisibility(View.VISIBLE);
                    varA.setVisibility(View.VISIBLE);
                    varA1.setVisibility(View.VISIBLE);
                    varB.setVisibility(View.VISIBLE);
                    varB1.setVisibility(View.VISIBLE);
                    varC.setVisibility(View.VISIBLE);
                    varC1.setVisibility(View.VISIBLE);
                    varD.setVisibility(View.VISIBLE);
                    varD1.setVisibility(View.VISIBLE);
                    varE.setVisibility(View.VISIBLE);
                    varE1.setVisibility(View.VISIBLE);
                    notes.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.GONE);
                }

            }
        });
    }

}