package br.edu.fatecguarulhos.escaneiaai.interfaces;

import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Calendar;

import br.edu.fatecguarulhos.escaneiaai.util.ImpressoraTermica;

public interface Imprimivel {
    public static final String TITULO_APP = "[C]<b>Escaneia ae</b>\n";
    public static final String DATA_E_HORA_IMPRESSAO = "[L] Impresso em: " + Calendar.getInstance().getTime() + "\n";
    public String dadosASeremImpressos(ImpressoraTermica impressora);
}
