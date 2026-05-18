package br.edu.fatecguarulhos.escaneiaai.telas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.R;
import br.edu.fatecguarulhos.escaneiaai.dao.EventoDao;
import br.edu.fatecguarulhos.escaneiaai.interfaces.FirebaseCallback;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;
import br.edu.fatecguarulhos.escaneiaai.util.CaptureActivityPortrait;
import br.edu.fatecguarulhos.escaneiaai.util.QrCodeManager;

public class CameraLeitorCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera_leitor_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        openQrCode();
    }
    public void openQrCode(){
       //QrCodeManager.abrirLeitor(new IntentIntegrator(this));
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Escaneie o QR CODE");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.initiateScan();
    }
    // Resultado da leitura do QR code na tela "HomeFragment"
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // finalizar a tela(garante que a tela em si nunca seja vista)
        finish();
        //String msgQrCode = QrCodeManager.getResultadoLeitor(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String msgQrCode = null;
        if(intentResult != null){
            String output = intentResult.getContents();
            msgQrCode = output;
        }
        if(msgQrCode == null){
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            // dividir o id[0] do tipo de qr code[1]
            String[] msgFatiada = msgQrCode.split("/type=");
            EventoDao eventoConnection = new EventoDao();
            eventoConnection.getEventoById(
                    msgFatiada[0],
                    // garantir q estejam sincronos
                    new FirebaseCallback() {
                        @Override
                        public void onCallbackForAll(List<Evento> lista) { }

                        @Override
                        public void onCallBackByid(Evento e) {
                            String jsonEvento = new Gson().toJson(e);
                            resultLeituraQC(jsonEvento, msgFatiada[1]);
                        }
                    });
        }
    }
    public void resultLeituraQC(String jsonEvento, String tipoQrCode){
        if(tipoQrCode.equals("entrada"))
            formRegistrarCliente(jsonEvento,true);
        else if(tipoQrCode.equals("saida"))
            formRegistrarCliente(jsonEvento,false);
        else
            Toast.makeText(this
                            ,"Leitura inválida tente novamente"
                            , Toast.LENGTH_SHORT)
                    .show();
    }
    private void formRegistrarCliente(String jsonEvento, boolean isEntrada){
        Intent it = new Intent(this, TelaRegistrarParticipante.class);
        it.putExtra("eventoJson",jsonEvento);
        it.putExtra("isEntrada", isEntrada);
        startActivity(it);
    }


}