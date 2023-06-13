package br.com.testesbottomnav.util;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoBd {

    private static FirebaseAuth auth;

    public static FirebaseAuth firebaseAutencicacao(){
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

}
