package com.example.logincadastroeditperfil;

//feito por Mateus Siqueira Salomão
//Importante lembrar de dar os créditos a pessoa que fez
//afinal sabemos o quão trabalhoso são essas coisas
//e aqui já é um produto final de uma longa leitura de documentação e apredizagem com professores
//Agradecimento a professoa Aline Francisco do Senac Taboão

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnEntrar, btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnEntrar.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        });
        btnCadastrar.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, FormCadastrar.class));
            finish();
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            TelaPrincipal();
        }
    }
    private void TelaPrincipal(){
        startActivity(new Intent(getApplicationContext(), TelaInicial.class));

    }
}