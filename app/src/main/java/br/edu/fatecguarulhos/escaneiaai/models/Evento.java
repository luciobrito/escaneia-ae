package br.edu.fatecguarulhos.escaneiaai.models;

import android.graphics.Bitmap;

import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecguarulhos.escaneiaai.interfaces.Imprimivel;
import br.edu.fatecguarulhos.escaneiaai.util.ImpressoraTermica;
import br.edu.fatecguarulhos.escaneiaai.util.QrCodeManager;

public class Evento {
    private String titulo, id, idCriador, senha, local, descricao;
    private String dataInicio, dataFim;
    private List<Participante> participantes = new ArrayList<>();
    //private DateTime dataInicio, dataFim,
    private Bitmap qrCode;

    public Bitmap getQrCode() {
        return qrCode;
    }

    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }

    public Evento(String titulo){

        qrCode = QrCodeManager.gerarQrCode(id);
        this.titulo = titulo;
    }

    public Evento() {
    }

    private String imagemFormatadaParaImpressao(ImpressoraTermica impressora){
        return "[C]<img>"+ PrinterTextParserImg.bitmapToHexadecimalString(impressora.getImpressora(), qrCode) + "</img>\n" ;
    }
    //@Override
    public String dadosASeremImpressos(ImpressoraTermica impressora) {
        return Imprimivel.TITULO_APP +
                "[L]" + titulo +
                Imprimivel.DATA_E_HORA_IMPRESSAO;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCriador() {
        return idCriador;
    }

    public void setIdCriador(String idCriador) {
        this.idCriador = idCriador;
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

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
