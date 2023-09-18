package com.example.logincadastroeditperfil;

//feito por Mateus Siqueira Salomão

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private TextView text_tela_cadastro;
    private EditText editEmail, editSenha;
    private Button btnEntrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Preencha todos os campos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IniciarComponentes();

        btnEntrar.setOnClickListener(view -> {
            if(editEmail.getText().toString().isEmpty() || editSenha.getText().toString().isEmpty()){
                Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.GRAY);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            }else{
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        btnEntrar.getWindowToken(), 0);
                AutenticarUsuario(view);
            }
        });
        text_tela_cadastro.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),FormCadastrar.class));
            finish();
        });
    }
    private void AutenticarUsuario(View view){
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                progressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(this::TelaPrincipal,3000);
            }else{
                String erro;
                try {
                    throw Objects.requireNonNull(task.getException());
                }catch (Exception e){
                    erro = "erro ao Logar usuario";
                }
                Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.GRAY);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void TelaPrincipal(){
        startActivity(new Intent(getApplicationContext(), TelaInicial.class));
        finish();
    }

    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.texviewCadastrar);
        editEmail = findViewById(R.id.editTextEmailLogar);
        editSenha = findViewById(R.id.editTextPasswordLogar);
        btnEntrar = findViewById(R.id.btnEntrarLogin);
        progressBar = findViewById(R.id.progessBarLogin);
    }

}