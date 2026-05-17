package br.edu.fatecguarulhos.escaneiaai.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;

import br.edu.fatecguarulhos.escaneiaai.interfaces.Imprimivel;
import br.edu.fatecguarulhos.escaneiaai.models.Evento;

public class ImpressoraTermica {
    private Activity activity;
    private Context context;
    private EscPosPrinter impressora;

    public ImpressoraTermica(Activity activity, Context context) throws Exception{
        this.activity = activity;
        this.context = context;
        try {
            impressora = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
        } catch (Exception e) {
            throw e;
        }
        }

    public Activity getActivity() {
        return activity;
    }

    public EscPosPrinter getImpressora() {
        return impressora;
    }

    private void requisitarPermissaoBluetooth(Imprimivel imprimivel) throws EscPosEncodingException, EscPosBarcodeException, EscPosParserException, EscPosConnectionException {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            //Não entendi o terceiro argumento, então simplesmente coloquei um inteiro
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH}, 0);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_CONNECT},2);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 3);
        } else {
            //imprimirComPermissao(imprimivel);
        }
    }
    public void imprimirComPermissao(Imprimivel imprimivel) {

        try {
            EscPosPrinter impressora2 = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
            impressora2.printFormattedText("[C]<b>Teste</b>\n" +
                    "[L]Impressão\n");
        } catch (Exception e) {

        }
        }
    public void imprimir(Imprimivel imprimivel) throws EscPosEncodingException, EscPosBarcodeException, EscPosParserException, EscPosConnectionException {
        requisitarPermissaoBluetooth(imprimivel);
    }
}
