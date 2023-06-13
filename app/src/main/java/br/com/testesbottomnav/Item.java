package br.com.testesbottomnav;

import java.io.Serializable;

public class Item implements Serializable {


    String nomeClubeUm, nomeClubeDois, horario, data;
    String imagemClubeUm, imagemClubeDois;

    //Construtor
    public Item(String nomeClubeUm, String nomeClubeDois, String horario, String data,
                String imagemClubeUm, String imagemClubeDois) {
        this.nomeClubeUm = nomeClubeUm;
        this.nomeClubeDois = nomeClubeDois;
        this.horario = horario;
        this.data = data;
        this.imagemClubeUm = imagemClubeUm;
        this.imagemClubeDois = imagemClubeDois;
    }

    //Gets and Sets
    public String getNomeClubeUm() {
        return nomeClubeUm;
    }

    public void setNomeClubeUm(String nomeClubeUm) {
        this.nomeClubeUm = nomeClubeUm;
    }

    public String getNomeClubeDois() {
        return nomeClubeDois;
    }

    public void setNomeClubeDois(String nomeClubeDois) {
        this.nomeClubeDois = nomeClubeDois;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImagemClubeUm() {
        return imagemClubeUm;
    }

    public void setImagemClubeUm(String imagemClubeUm) {
        this.imagemClubeUm = imagemClubeUm;
    }

    public String getImagemClubeDois() {
        return imagemClubeDois;
    }

    public void setImagemClubeDois(String imagemClubeDois) {
        this.imagemClubeDois = imagemClubeDois;
    }

}
