package br.edu.fatecguarulhos.escaneiaai.telas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.util.ImpressoraTermica;
import br.edu.fatecguarulhos.escaneiaai.util.QrCodeManager;

public class TelaQrCode extends AppCompatActivity {
    private TextView txtTituloEvento, txtTipoQC;
    private Button btnQCEntrada, btnQCSaida, btnImprimirQrCode;
    private String idEvento, tituloEvento;
    private ImageView imgQrCode;
    private EventoDao eventoDao;
    private Evento evento;
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
        try{
            Intent it =  getIntent();
            idEvento = it.getStringExtra("id");
            tituloEvento = it.getStringExtra("titulo");
            eventoDao = new EventoDao();
            evento = new Evento();
        } catch (RuntimeException re){
            Toast.makeText(this, re.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inicializarComponentes(){
        txtTituloEvento = findViewById(R.id.txtTituloEvento_telaQrCode);
        txtTipoQC = findViewById(R.id.txtTipoQrCode_telaQrCode);
        imgQrCode = findViewById(R.id.imgvQrCode);
        btnQCEntrada = findViewById(R.id.btnQrCodeEntrada_telaQrCode);
        btnQCSaida = findViewById(R.id.btnQrCodeSaida_telaQrCode);
        btnImprimirQrCode = findViewById(R.id.btnImprimirQrCode_telaQrCode);
        buscarEvento();
    }
    private void configurarComponentes(){
        txtTituloEvento.setText(tituloEvento);
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
        txtTipoQC.setText("QrCode de Saida");
        Bitmap qrCode = QrCodeManager.gerarQrCode(idEvento + "/type=saida");
        imgQrCode.setImageBitmap(qrCode);
    }
    private void getQrCodeEntrada(){
        txtTipoQC.setText("QrCode de Entrada");
        Bitmap qrCode = QrCodeManager.gerarQrCode(idEvento + "/type=entrada");
        imgQrCode.setImageBitmap(qrCode);
    }
    public void imprimirQrCodeEntrada(View view){
        try {

            Toast.makeText(this, "Imprimindo...", Toast.LENGTH_SHORT).show();
            ImpressoraTermica impressora = new ImpressoraTermica(this, this);
            String texto =
                    "[C]<b><font size='big'>Escaneia Ae</font></b>\n \n" +
                            "[C]"+ evento.getTitulo() +"\n" +
                            "[L]Entrada do evento\n"
                            +"[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(impressora.getImpressora(),  QrCodeManager.gerarQrCode(idEvento + "/type=entrada"))+"</img>\n\n"
                            +"[L]Impresso em: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "\n";
            impressora.imprimir(texto);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void imprimirQrCodeSaida(View view){
        try {

            Toast.makeText(this, "Imprimindo...", Toast.LENGTH_SHORT).show();
            ImpressoraTermica impressora = new ImpressoraTermica(this, this);
            String texto =
                    "[C]<b><font size='big'>Escaneia Ae</font></b>\n \n" +
                            "[C]"+ evento.getTitulo() +"\n" +
                            "[L]Saida do evento\n"
                            +"[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(impressora.getImpressora(),  QrCodeManager.gerarQrCode(idEvento + "/type=saida"))+"</img>\n\n"+
                            "[L]Impresso em: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "\n";
            impressora.imprimir(texto);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void voltar(View view){
        finish();
    }
    public void buscarEvento(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("eventos");
        reference.child(idEvento).get().addOnCompleteListener(task ->{
            if (task.isSuccessful() && task.getResult().exists()){
                DataSnapshot snapshot = task.getResult();
                Evento evento = snapshot.getValue(Evento.class);
                this.evento.setTitulo(evento.getTitulo());
            }
        });
    }
}