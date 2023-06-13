package br.com.testesbottomnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import br.com.testesbottomnav.model.ApostaModelDb;
import br.com.testesbottomnav.util.UsuarioLogado;

public class AreaApostasNba extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private Item partida;
    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner3;
    private Button button;
    private TextInputEditText valor;
    private TextView nomeTimeA, nomeTimeB, primeiroTempoTimeA, primeiroTempoTimeB, segundoTempoTimeA, segundoTempoTimeB;
    private ImageView logoTimeA, logoTimeB;
    private RadioButton rdbTimeA, rdbTimeB;
    private RadioGroup radioGroup;
    private EditText placarTimeA, placarTimeB, primeiroTempPlacarTimeA, primeiroTempPlacarTimeB, segundoTempPlacarTimeA,
            segundoTempPlacarTimeB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_apostas_nba);

        spinner = findViewById(R.id.spinner_colored_nba);
        spinner2 = findViewById(R.id.spinner_colored_2_nba);
        spinner3 = findViewById(R.id.spinner_colored_3_nba);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_options_4,
                R.layout.color_spinner_layout
        );

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_options_5,
                R.layout.color_spinner_layout
        );

        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_options_6,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);

        dataInitialize();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Double.parseDouble(UsuarioLogado.userLogado.getSaldo()) > Double.parseDouble(valor.getText().toString())){
                    db = FirebaseFirestore.getInstance();
                    CollectionReference apostaRef = db.collection("apostas");

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    ApostaModelDb modelDb = new ApostaModelDb(userId, partida.imagemClubeUm, partida.imagemClubeDois,
                            getApostasFormatado(), "aberto", Double.parseDouble(valor.getText().toString()), 0.00);


                    apostaRef.add(modelDb)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AreaApostasNba.this, "Aposta Registrada com sucesso", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AreaApostasNba.this, "Erro ao cadastrar: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            });
                }else {
                    Toast.makeText(AreaApostasNba.this, "Saldo insuficiente para essa aposta", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void dataInitialize() {

        Intent intent = getIntent();

        if(intent != null){
            nomeTimeA = findViewById(R.id.nome_time_a);
            nomeTimeB = findViewById(R.id.nome_time_b);
            logoTimeA = findViewById(R.id.logo_time_a_nba);
            logoTimeB = findViewById(R.id.logo_time_b_nba);
            rdbTimeA = findViewById(R.id.radioButton_nba_time_a);
            rdbTimeB = findViewById(R.id.radioButton2_nba_time_b);
            radioGroup = findViewById(R.id.radioGroup_nba);
            primeiroTempoTimeA = findViewById(R.id.aposta_1_tempo_time_a_nba);
            primeiroTempoTimeB = findViewById(R.id.aposta_1_tempo_time_b_nba);
            segundoTempoTimeA = findViewById(R.id.aposta_2_tempo_time_a_nba);
            segundoTempoTimeB = findViewById(R.id.aposta_2_tempo_time_b_nba);
            placarTimeA = findViewById(R.id.editText_nba_placar_time_a);
            placarTimeB = findViewById(R.id.editText2_nba_placar_time_b);
            primeiroTempPlacarTimeA = findViewById(R.id.editText3_nba_prim_temp_placar_time_a);
            primeiroTempPlacarTimeB = findViewById(R.id.editText4_nba_prim_temp_placar_time_b);
            segundoTempPlacarTimeA = findViewById(R.id.editText_2_tempo_time_a_nba);
            segundoTempPlacarTimeB = findViewById(R.id.editText_2_tempo_time_b_nba);
            button = findViewById(R.id.button_terminar_apostas_nba);
            valor = findViewById(R.id.edittext_valor_apostado_nba);

            partida = (Item) intent.getSerializableExtra("partida");

            nomeTimeA.setText(partida.getNomeClubeUm());
            nomeTimeB.setText(partida.getNomeClubeDois());
            rdbTimeA.setText(partida.getNomeClubeUm());
            rdbTimeB.setText(partida.getNomeClubeDois());
            primeiroTempoTimeA.setText(partida.getNomeClubeUm());
            primeiroTempoTimeB.setText(partida.getNomeClubeDois());
            segundoTempoTimeA.setText(partida.getNomeClubeUm());
            segundoTempoTimeB.setText(partida.getNomeClubeDois());

            Picasso.with(this)
                    .load(partida.getImagemClubeUm())
                    .fit()
                    .centerCrop()
                    .into(logoTimeA);
            Picasso.with(this)
                    .load(partida.getImagemClubeDois())
                    .fit()
                    .centerCrop()
                    .into(logoTimeB);

            InputFilter inputFilter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {

                    try {
                        int placar = Integer.parseInt(spanned.toString() + charSequence.toString());

                        if (placar >=0 && placar <= 199){
                            return null;

                        }
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                    return "";

                }
            };

            InputFilter inputFilter2 = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {

                    try {
                        int placar = Integer.parseInt(spanned.toString() + charSequence.toString());

                        if (placar >=0 && placar <= 99){
                            return null;

                        }
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                    return "";

                }
            };

            placarTimeA.setFilters(new InputFilter[]{inputFilter});
            placarTimeB.setFilters(new InputFilter[]{inputFilter});
            primeiroTempPlacarTimeA.setFilters(new InputFilter[]{inputFilter2});
            primeiroTempPlacarTimeB.setFilters(new InputFilter[]{inputFilter2});
            segundoTempPlacarTimeA.setFilters(new InputFilter[]{inputFilter2});
            segundoTempPlacarTimeB.setFilters(new InputFilter[]{inputFilter2});

        }else{
            Toast.makeText(this, "Erro ao carregar as informações de basquete!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getApostasFormatado(){

        String apostas = "Apostas:\n\n";


        RadioButton vencedor = findViewById(radioGroup.getCheckedRadioButtonId());
        if (vencedor != null){

            apostas+= "O vencedor será: " + vencedor.getText().toString() + "\n";
        }

        if (!placarTimeA.getText().toString().equals("")){
            apostas+= nomeTimeA.getText().toString() + ": " + placarTimeA.getText().toString() + " Pontos\n";
        }
        if (!placarTimeB.getText().toString().equals("")){
            apostas+= nomeTimeB.getText().toString() + ": " + placarTimeB.getText().toString() + " Pontos\n";
        }
        if(!spinner.getSelectedItem().toString().equals("3 pts")){
            apostas+= "Quandidade de sestas de 3pt: " + spinner.getSelectedItem().toString() + "\n";
        }
        if(!spinner2.getSelectedItem().toString().equals("Ejections")) {
            apostas += "Número de Ejections: " + spinner2.getSelectedItem().toString() + "\n";
        }
        if (!spinner3.getSelectedItem().toString().equals("Lance Livre")){
            apostas+= "Número de Lance Livres: " + spinner3.getSelectedItem().toString() + "\n";
        }
        if (!primeiroTempPlacarTimeA.getText().toString().equals("") && !primeiroTempPlacarTimeB.getText().toString().equals("")){
            apostas+= "Placar do primeiro tempo: " + primeiroTempPlacarTimeA.getText().toString()
                    + " x " + primeiroTempPlacarTimeB.getText().toString() + "\n";
        }
        if(!segundoTempPlacarTimeA.getText().toString().equals("") && !segundoTempPlacarTimeB.getText().toString().equals("")){
            apostas+= "Placar do segundo tempo: " + segundoTempPlacarTimeA.getText().toString()
                    + " x " + segundoTempPlacarTimeB.getText().toString() + "\n";
        }
        return apostas;
    }
}