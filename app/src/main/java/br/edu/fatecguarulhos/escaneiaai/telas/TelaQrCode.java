package br.edu.fatecguarulhos.escaneiaai.telas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.util.ImpressoraTermica;
import br.edu.fatecguarulhos.escaneiaai.util.QrCodeManager;

public class TelaQrCode extends AppCompatActivity {
    private Button btnQCEntrada, btnQCSaida, btnImprimirQrCode;
    private String idEvento;
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
        Intent it =  getIntent();
        idEvento = it.getStringExtra("id");
        eventoDao = new EventoDao();
        evento = new Evento();}

    private void inicializarComponentes(){
        imgQrCode = findViewById(R.id.imgvQrCode);
        btnQCEntrada = findViewById(R.id.btnQrCodeEntrada_telaQrCode);
        btnQCSaida = findViewById(R.id.btnQrCodeSaida_telaQrCode);
        btnImprimirQrCode = findViewById(R.id.btnImprimirQrCode_telaQrCode);
        buscarEvento();

    }
    private void configurarComponentes(){
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
    }
    private void getQrCodeEntrada(){
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