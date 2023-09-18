package com.example.logincadastroeditperfil;

//feito por Mateus Siqueira Salomão

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class form_PerfilUsuario extends AppCompatActivity {
    private TextView txtEmail, txtNome;
    private Button btnDeslogar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String usuarioId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    ImageView Userimg;
    int Tranca = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_perfil_usuario);

        setSupportActionBar(findViewById(R.id.toolbarteste));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        InicializandoComponentes();

        btnDeslogar.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        });

        Userimg.setOnClickListener(view -> {
            AlertDialog.Builder selecionaFoto = new AlertDialog.Builder(form_PerfilUsuario.this);
            selecionaFoto.setTitle("Origem da Foto");
            selecionaFoto.setMessage("Por favor, selecione a origem da foto");
            selecionaFoto.setCancelable(true);
            selecionaFoto.setPositiveButton("Galeria", (dialogInterface, i) -> ChamaGaleria());
            selecionaFoto.setNegativeButton("camera", (dialogInterface, i) -> {
                ChamaCamera();
            });
            selecionaFoto.create().show();
        });
    }



@Override
    protected void onStart() {
        super.onStart();
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        DocumentReference documentReference = db.collection("usuarios").document(usuarioId);
        documentReference.addSnapshotListener((value, error) -> {
            if(value != null){
                txtNome.setText(value.getString("nome"));
                txtEmail.setText(email);
            }
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        try {
            File locale = File.createTempFile("tempfile", ".jpg");
            //Toast.makeText(form_PerfilUsuario.this, "TESTE DE ERRO", Toast.LENGTH_LONG).show();
            storageRef.child(usuarioId+".jpg").getFile(locale).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(locale.getAbsolutePath());
                Userimg.setImageBitmap(bitmap);
            }).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(),"erro ao baixar imagem", Toast.LENGTH_SHORT).show();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void InicializandoComponentes(){
        txtEmail = findViewById(R.id.textViewEmailUserTelaPerfil);
        txtNome = findViewById(R.id.textViewNomeUserTelaPerfil);
        btnDeslogar = findViewById(R.id.btnDeslogarPerfil);
        Userimg = findViewById(R.id.imageButtonUser);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @SuppressLint("QueryPermissionsNeeded")
    private void ChamaCamera() {
        Tranca = 1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void ChamaGaleria(){
        Tranca = 2;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent dados) {
        super.onActivityResult(requestCode, resultCode, dados);


        if(Tranca == 1){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
            Bundle extras = dados.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Userimg.setImageBitmap(imageBitmap);

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference userRef = storageRef.child(usuarioId+".jpg");

            Userimg.setDrawingCacheEnabled(true);
            Userimg.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) Userimg.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = userRef.putBytes(data);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
            }).addOnSuccessListener(taskSnapshot -> {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            });

            } catch (Exception ignored) {
            }
        }}
        else if(Tranca == 2){
            if(requestCode == 1){
            try {
                Uri imageUser = dados.getData();

                Bitmap galeriafoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUser);
                Bitmap fotoredimendsionada = Bitmap.createScaledBitmap(galeriafoto, 250, 250, true);
                Userimg.setImageBitmap(fotoredimendsionada);

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference userRef = storageRef.child(usuarioId+".jpg");

                Userimg.setDrawingCacheEnabled(true);
                Userimg.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) Userimg.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = userRef.putBytes(data);
                uploadTask.addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                }).addOnSuccessListener(taskSnapshot -> {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                });

            }catch (Exception ignored){
            }
        }}
    }
}