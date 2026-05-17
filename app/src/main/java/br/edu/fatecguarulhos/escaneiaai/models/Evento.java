package br.edu.fatecguarulhos.escaneiaai.models;

import android.graphics.Bitmap;

import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String titulo, id, idCriador;
    private List<Participante> participantes = new ArrayList<>();
    private DateTime dataInicio, dataFim;
    private Bitmap qrCode;

    public Bitmap getQrCode() {
        return qrCode;
    }

    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }

    private String dataInicio, dataFim;
    public Evento(String titulo){

        qrCode = QrCodeManager.gerarQrCode(id);
        this.titulo = titulo;
    }

    public Evento() {
        qrCode = QrCodeManager.gerarQrCode(id);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }
    private void gerarQrCode(){}
    public void setDataFim(DateTime dataFim) {

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getIdCriador() {
        return idCriador;
    }

    public void setIdCriador(String idCriador) {
        this.idCriador = idCriador;
    private String imagemFormatadaParaImpressao(ImpressoraTermica impressora){
        return "[C]<img>"+ PrinterTextParserImg.bitmapToHexadecimalString(impressora.getImpressora(), qrCode) + "</img>\n" ;
    }
    @Override
    public String dadosASeremImpressos(ImpressoraTermica impressora) {
        return TITULO_APP +
                "[L]" + titulo +

                DATA_E_HORA_IMPRESSAO;
    }
}
