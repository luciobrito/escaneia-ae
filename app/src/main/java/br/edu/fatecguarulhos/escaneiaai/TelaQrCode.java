package br.edu.fatecguarulhos.escaneiaai;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.edu.fatecguarulhos.escaneiaai.util.QrCodeManager;

public class TelaQrCode extends AppCompatActivity {
    private String idEvento;
    private ImageView imgQrCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_qr_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent it =  getIntent();
        idEvento = it.getStringExtra("id");
        imgQrCode = findViewById(R.id.imgvQrCode);
        getQrCode();
    }
    private void getQrCode(){
        Bitmap qrCode = QrCodeManager.gerarQrCode(idEvento);
        imgQrCode.setImageBitmap(qrCode);
    }
}