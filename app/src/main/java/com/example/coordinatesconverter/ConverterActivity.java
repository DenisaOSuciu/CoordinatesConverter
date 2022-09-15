package com.example.coordinatesconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Pattern;

public class ConverterActivity extends AppCompatActivity {
    TextView output;
    EditText input;
    Button convert;
    Button export;
    Button openMap;
    String lat;
    String lng;

    private static final java.text.DecimalFormat decimalFormat = new DecimalFormat(".####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        Objects.requireNonNull(getSupportActionBar()).hide();

        output=(TextView) findViewById(R.id.output);
        input=(EditText) findViewById(R.id.coordinatesInput);
        convert=(Button) findViewById(R.id.button);
        export= (Button) findViewById(R.id.export);
        openMap=(Button) findViewById(R.id.mapBtn);

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input.getText().toString().equals("") ){
                    output.setText("Type a value first");
                }
             /*   else if(!Pattern.matches(" ", input.getText().toString())){
                    output.setText("Your values should be separated by space");}
*/
                else if(!Pattern.matches("^[0-9. ]*$", input.getText().toString())){
                    output.setText("Your value should contain only digits, space or dots");
                }

                else
                {
                    closeKeyboard();
                    String[] parts = input.getText().toString().split(" ");
                     lat = parts[0];
                     lng = parts[1];

                String result ="DMS conversion is: \n" + convertToDMS(Double.parseDouble(lat)) + " N "+
                       "\n" +  convertToDMS(Double.parseDouble(lng)) + " E ";
                output.setText(result); }
            }
        });


        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input.getText().toString().equals("")){
                    output.setText("Type a value first");
                }else {

                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    startActivity(intent);
                }
            }
        });



    }

    public static String convertToDMS(double input) {
        double degree =  Math.floor(input);
        double minutes = ((input - Math.floor(input)) * 60.0);
        double seconds = (minutes - Math.floor(minutes)) * 60.0;
        return ((int)degree)+"° "+((int)minutes)+"' " +decimalFormat.format(seconds) + "\"";


    }

    private void closeKeyboard() {

        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }


    public void export(View view) {
         StringBuilder data =new StringBuilder();
        data.append("Decimal Latitude, Decimal longitude, DMS latitude, DMS longitude");

        if(input.getText().toString().equals("")){
            output.setText("Type a value first");
        }
        else {
            data.append("\n" + lat + " , " + lng + " , "  + convertToDMS(Double.parseDouble(lat)) + " , " + convertToDMS(Double.parseDouble(lng)));


            try {
                FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                out.write((data.toString()).getBytes());
                out.close();

                Context context = getApplicationContext();
                File filelocation = new File(getFilesDir(), "data.csv");
                Uri path = FileProvider.getUriForFile(context, "com.example.coordinatesconverter.fileprovider", filelocation);

                Intent fileIntent = new Intent(Intent.ACTION_SEND);
                fileIntent.setType("text/csv");
                fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                startActivity(Intent.createChooser(fileIntent, "Send mail"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    }