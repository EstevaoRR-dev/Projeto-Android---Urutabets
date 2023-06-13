package br.com.testesbottomnav;

public class ItemNoticia {

    private String imagemNoticia;
    private String tituloNoticia, corpoNoticia;
    private String url;

    //Construtores

    public ItemNoticia() {
    }

    public ItemNoticia(String imagemNoticia, String tituloNoticia, String corpoNoticia, String url) {
        this.imagemNoticia = imagemNoticia;
        this.tituloNoticia = tituloNoticia;
        this.corpoNoticia = corpoNoticia;
        this.url = url;
    }

    //Gets and Sets
    public String getImagemNoticia() {
        return imagemNoticia;
    }

    public void setImagemNoticia(String imagemNoticia) {
        this.imagemNoticia = imagemNoticia;
    }

    public String getTituloNoticia() {
        return tituloNoticia;
    }

    public void setTituloNoticia(String tituloNoticia) {
        this.tituloNoticia = tituloNoticia;
    }

    public String getCorpoNoticia() {
        return corpoNoticia;
    }

    public void setCorpoNoticia(String corpoNoticia) {
        this.corpoNoticia = corpoNoticia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
