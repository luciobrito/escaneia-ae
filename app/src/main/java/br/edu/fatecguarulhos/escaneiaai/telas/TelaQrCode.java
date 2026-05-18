package br.edu.fatecguarulhos.escaneiaai.telas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.util.QrCodeManager;

public class TelaQrCode extends AppCompatActivity {
    private TextView txtTitulo, txtTipoQrCode;
    private Button btnQCEntrada, btnQCSaida;
    private String idEvento, tituloEvento;
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
        inicializarValores();
        inicializarComponentes();
        configurarComponentes();
    }

    private void inicializarValores() {
        Intent it =  getIntent();
        idEvento = it.getStringExtra("id");
        tituloEvento = it.getStringExtra("titulo");
    }

    private void inicializarComponentes(){
        imgQrCode = findViewById(R.id.imgvQrCode);
        btnQCEntrada = findViewById(R.id.btnQrCodeEntrada_telaQrCode);
        btnQCSaida = findViewById(R.id.btnQrCodeSaida_telaQrCode);
        txtTitulo = findViewById(R.id.txtTituloEvento_telaQrCode);
        txtTipoQrCode = findViewById(R.id.txtTipoQrCode_telaQrCode);
    }
    private void configurarComponentes(){
        txtTitulo.setText(tituloEvento);
        getQrCodeEntrada();
        btnQCEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQrCodeEntrada();
            }
        });
        btnQCSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQrCodeSaida();
            }
        });
    }

    private void getQrCodeSaida(){
        Bitmap qrCode = QrCodeManager.gerarQrCode(idEvento + "/type=saida");
        imgQrCode.setImageBitmap(qrCode);
        txtTipoQrCode.setText("QrCode Saida");
    }
    private void getQrCodeEntrada(){
        Bitmap qrCode = QrCodeManager.gerarQrCode(idEvento + "/type=entrada");
        imgQrCode.setImageBitmap(qrCode);
        txtTipoQrCode.setText("QrCode Entrada");
    }
    public void voltar(View view){
        finish();
    }
}