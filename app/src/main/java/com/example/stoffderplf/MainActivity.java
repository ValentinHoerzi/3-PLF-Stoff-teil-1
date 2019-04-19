package com.example.stoffderplf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //<editor-fold defaultstate="collapsed" desc="Step 1 - Variablen Instanzieren">
    Spinner spinnerNormal,spinnerCustom;
    Button button;
    SearchView searchView;
    ListView listViewNormal,listViewCustom;

    /* Liste unseres Items aus dem File:
       Wenn das Programm gerade installiert wurde gibt es noch kein file -> man liest das Assets-File
       Wenn das Programm schon einmal geöffnet wurde gibt es schon ein File -> man liest das existierende File
    */
    List<String> itemsList;
    File itemsFile;

    customLayoutAdapter CustomAdapter;

    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> listViewAdapter;
    //</editor-fold>




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //<editor-fold defaultstate="collapsed" desc="Step 2 - Variablen Instanzieren (Für bessere Übersicht in eigene Methode ausgelagert">
        initUI();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Step 3 - Datei einlesen">

        /*
            openFileInput öffnet eine im System liegende Datei (also eine Datei die bereits geschrieben wurde)
            Die Methode wirft eine Exception wenn diese Datei nicht vorhanden ist.
            In diesem Fall lesen wir dann die Assets Datei und schreiben die Datei neu ins System
         */
        try(BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(itemsFile.getName())))){
            String line = br.readLine();
            while(line != null){
                String[] split = line.split(",");
                itemsList.addAll(Arrays.asList(split));
                line = br.readLine();
            }
        }catch(Exception e){
            try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(itemsFile.getName())))) {
                String line = br.readLine();
                while(line != null){
                    String[] split = line.split(",");
                    itemsList.addAll(Arrays.asList(split));
                    line = br.readLine();
                }
            }catch (Exception p){}
            try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(itemsFile.getName(),MODE_PRIVATE)))){
                for (String item : itemsList) {
                    bw.write(item);
                    bw.newLine();
                    bw.flush();
                }
            }catch (Exception x){}
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Step 4 - Eingelesene Items den Adaptern zuweisen">
        spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,itemsList);
        listViewAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,itemsList);

        CustomAdapter = new customLayoutAdapter(this,R.layout.custom_layout,itemsList);

        listViewNormal.setAdapter(listViewAdapter);
        spinnerNormal.setAdapter(spinnerAdapter);

        spinnerCustom.setAdapter(CustomAdapter);
        listViewCustom.setAdapter(CustomAdapter);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Step 5 - Button einen Clicklistener zuweisen UND Toast">
        button.setOnClickListener(listener -> {
            Random r = new Random();
            String s = itemsList.get(r.nextInt(itemsList.size()));
            button.setText(s); //set the text of the button to a random item in the list
            showToast(s);
        });
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Step 6 - Search View setzen">
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                if(TextUtils.isEmpty(text)){
                    CustomAdapter.filter(" ");
                }else{
                    CustomAdapter.filter(text);
                }
                return false;
            }
        });
        //</editor-fold>
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void initUI() {
        spinnerNormal = findViewById(R.id.spinnerNormal);
        spinnerCustom = findViewById(R.id.spinnerCustom);
        button = findViewById(R.id.button);
        searchView = findViewById(R.id.searchView);
        listViewNormal = findViewById(R.id.listViewNormal);
        listViewCustom = findViewById(R.id.listViewCustom);

        itemsList = new ArrayList<>();
        itemsFile = new File("items.csv");
    }

}
