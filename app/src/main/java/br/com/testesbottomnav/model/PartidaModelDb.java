package br.com.testesbottomnav.model;

public class PartidaModelDb {

    private String timeAimageUrl;
    private String timeBimageUrl;
    private String nomeTimeA;
    private String nomeTimeB;
    private String horario;
    private String data;
    private String liga;

    public PartidaModelDb() {
        //construtor necessário
    }

    public PartidaModelDb(String timeAimageUrl, String timeBimageUrl, String nomeTimeA, String nomeTimeB, String horario,
                          String data, String liga) {
        this.timeAimageUrl = timeAimageUrl;
        this.timeBimageUrl = timeBimageUrl;
        this.nomeTimeA = nomeTimeA;
        this.nomeTimeB = nomeTimeB;
        this.horario = horario;
        this.data = data;
        this.liga = liga;
    }

    public String getTimeAimageUrl() {
        return timeAimageUrl;
    }

    public void setTimeAimageUrl(String timeAimageUrl) {
        this.timeAimageUrl = timeAimageUrl;
    }

    public String getTimeBimageUrl() {
        return timeBimageUrl;
    }

    public void setTimeBimageUrl(String timeBimageUrl) {
        this.timeBimageUrl = timeBimageUrl;
    }

    public String getNomeTimeA() {
        return nomeTimeA;
    }

    public void setNomeTimeA(String nomeTimeA) {
        this.nomeTimeA = nomeTimeA;
    }

    public String getNomeTimeB() {
        return nomeTimeB;
    }

    public void setNomeTimeB(String nomeTimeB) {
        this.nomeTimeB = nomeTimeB;
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

    public String getLiga() {
        return liga;
    }

    public void setLiga(String liga) {
        this.liga = liga;
    }
}
