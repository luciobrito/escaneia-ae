package br.edu.fatecguarulhos.escaneiaai.util;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import br.edu.fatecguarulhos.escaneiaai.components.CardEvento;

public class QrCodeManager {
    public static List<CardEvento> eventoList = new ArrayList<>();
    public static void lerQrCode(IntentIntegrator intentIntegrator){
        //IntentIntegrator intentIntegrator = new IntentIntegrator(v.getActivity());
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scaneie o QR CODE");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.initiateScan();
    }
    public static String getResultadoLeitor(int requestCode, int resultCode, @Nullable Intent data){
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        /*
        if(intentResult != null){
            String contents = intentResult.getContents();
            if(contents != null){
                String output = intentResult.getContents();
                return output;
            }
        }
        return null;
         */
        if(intentResult != null){
            String contents = intentResult.getContents();
            //if(contents != null){
                String output = intentResult.getContents();
                return output;
            //}
        }
        return null;
    }

    public static Bitmap gerarQrCode(String input){
        //String encodedInput = Criptografador.encode(input);
        QRGEncoder qrgEncoder = new QRGEncoder(
                Objects.requireNonNull(input),
                null,
                QRGContents.Type.TEXT,
                500);
        //txtEncodedInput.setText(encodedInput);
        try {
            Bitmap bitmap = qrgEncoder.getBitmap(0);
            //imgQRCode.setImageBitmap(bitmap);
            return bitmap;
        } catch (Exception e){
            //e.printStackTrace();
            return null;
        }
    }




}
