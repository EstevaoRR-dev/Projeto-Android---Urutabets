package br.com.testesbottomnav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TesteDeFuncionalidadeRemoverDepois extends AppCompatActivity {

    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_de_funcionalidade_remover_depois);
        Intent intent = getIntent();

        view = findViewById(R.id.string_teste);

        view.setText(intent.getStringExtra("titulo"));
    }
}