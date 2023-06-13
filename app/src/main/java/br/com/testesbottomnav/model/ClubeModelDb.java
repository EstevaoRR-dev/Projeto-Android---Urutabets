package br.com.testesbottomnav.model;

public class ClubeModelDb {

    private String nome;
    private String imageUrl;
    private int posicao;

    public ClubeModelDb(){
        //Construtor vazio necessário
    }

    public  ClubeModelDb (String nome, String imageUrl, int posicao){
        if (nome.trim().equals("")){
            nome = "Sem nome";
        }

        this.nome = nome;
        this.imageUrl = imageUrl;
        this.posicao = posicao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
