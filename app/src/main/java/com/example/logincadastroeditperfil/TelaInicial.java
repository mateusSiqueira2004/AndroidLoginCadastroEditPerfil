package com.example.logincadastroeditperfil;

//feito por Mateus Siqueira Salomão

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja);

    //Declaração da toolbar no lugar da ActionBar
        setSupportActionBar(findViewById(R.id.toolbar));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //Menu que se localiza na toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.TelaDeUsuario) {
            TelaPerfil();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void TelaPerfil(){
        startActivity(new Intent(TelaInicial.this,form_PerfilUsuario.class));
        finish();
    }
}