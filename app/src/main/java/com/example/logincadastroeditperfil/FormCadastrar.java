package com.example.logincadastroeditperfil;

//feito por Mateus Siqueira Salomão

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCadastrar extends AppCompatActivity {
    private EditText editNome, editEmail, editSenha,editConfirmaSenha;
    private Button btnCadastrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso", "Senhas não conferem"};
    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastrar);
        iniciarComponentes();

        btnCadastrar.setOnClickListener(view -> {
            if(editNome.getText().toString().isEmpty() || editEmail.getText().toString().isEmpty() || editSenha.getText().toString().isEmpty()){
                Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.GRAY);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            }else {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(btnCadastrar.getWindowToken(), 0);
                CadastrarUsuario(view);
            }
        });
    }

    private void CadastrarUsuario(View view){
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        String confirmSenha;
        confirmSenha = editConfirmaSenha.getText().toString();
        int tt = senha.compareTo(confirmSenha);
        if(tt == 0){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {

                if(task.isSuccessful()){
                    SalvarUsuario();

                    Snackbar snackbar = Snackbar.make(view,mensagens[1],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.GRAY);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                    TelaInicial();
                }else{
                    String erro;
                    try {
                        throw Objects.requireNonNull(task.getException());
                    }catch(FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha com no mínimo 6 caracteres";
                    }catch(FirebaseAuthUserCollisionException e){
                        erro = "Essa conta já foi cadastrada";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";
                    }
                    catch (Exception e){
                        erro = "Erro ao cadastrar Usuário";
                    }
                        Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.GRAY);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.show();
                }

        });}else{
            Snackbar snackbar = Snackbar.make(view,mensagens[2],Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.GRAY);
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
        }

    }

    private void SalvarUsuario(){
        String nome = editNome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);

        usuarioId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference documentReference = db.collection("usuarios").document(usuarioId);

        documentReference.set(usuarios).addOnSuccessListener(unused -> Log.d("db","Sucesso ao salvar os dados"))
        .addOnFailureListener(e -> Log.d("db_error", "Erro ao salvar os dados"+ e));
    }

    private void TelaInicial(){
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), TelaInicial.class));
            finish();
        },3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void iniciarComponentes(){
        editNome = findViewById(R.id.editTextNomeCadastro);
        editEmail = findViewById(R.id.editTextEmailCadastro);
        editSenha = findViewById(R.id.editTextPasswordCadastro);
        editConfirmaSenha = findViewById(R.id.editTextPasswordCadastro2);
        btnCadastrar = findViewById(R.id.btnTelaCadastrar);
        progressBar = findViewById(R.id.progessBarCadastro);
    }
}