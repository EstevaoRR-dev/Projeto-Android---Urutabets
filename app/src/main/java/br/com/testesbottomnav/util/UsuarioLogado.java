package br.com.testesbottomnav.util;

import br.com.testesbottomnav.model.Usuario;

public class UsuarioLogado {

    public static Usuario userLogado;

    public static Usuario getUserLogado() {
        return userLogado;
    }

    public static void setUserLogado(String nome, String email, String telefone, String dataNasc, String sobrenome,
                                     String userType, String saldo) {
        userLogado = new Usuario();
        userLogado.setNome(nome);
        userLogado.setEmail(email);
        userLogado.setTelefone(telefone);
        userLogado.setDatanasc(dataNasc);
        userLogado.setSobrenome(sobrenome);
        userLogado.setUserType(userType);
        userLogado.setSaldo(saldo);
    }
}
