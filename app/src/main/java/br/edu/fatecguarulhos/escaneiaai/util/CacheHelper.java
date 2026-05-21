package br.edu.fatecguarulhos.escaneiaai.util;
import android.content.Context;
import android.content.SharedPreferences;

public class CacheHelper {
    private static final String PREF_PARTICIPANTE = "CacheParticipante";
    private static final String LAST_NOME_INPUT = "last_nome_input";
    private static final String LAST_EMAIL_INPUT = "last_email_input";
    private static final String LAST_RA_INPUT = "last_ra_input";

    public static void saveToCache(Context context, String inputNome, String inputEmail, String inputRa) {
        salvarNome(context, inputNome);
        salvarEmail(context, inputEmail);
        salvarRa(context, inputRa);
    }
    private static void salvarNome(Context context, String input){
        SharedPreferences prefs = context.getSharedPreferences(PREF_PARTICIPANTE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LAST_NOME_INPUT, input);
        editor.apply(); // apply() is asynchronous, commit() is synchronous
    }
    private static void salvarEmail(Context context, String input){
        SharedPreferences prefs = context.getSharedPreferences(PREF_PARTICIPANTE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LAST_EMAIL_INPUT, input);
        editor.apply(); // apply() is asynchronous, commit() is synchronous
    }
    private static void salvarRa(Context context, String input){
        SharedPreferences prefs = context.getSharedPreferences(PREF_PARTICIPANTE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LAST_RA_INPUT, input);
        editor.apply(); // apply() is asynchronous, commit() is synchronous
    }

    public static String[] getFromCache(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_PARTICIPANTE, Context.MODE_PRIVATE);
        String[] dadosCache = new String[3];
        // Returns empty string if no cache exists
        dadosCache[0] = prefs.getString(LAST_NOME_INPUT, "");
        dadosCache[1] = prefs.getString(LAST_EMAIL_INPUT, "");
        dadosCache[2] = prefs.getString(LAST_RA_INPUT, "");
        return dadosCache;
    }
}

