package br.com.testesbottomnav.model;

public class ApostaModelDb {

    private String userId;
    private String logoTimeaUrl;
    private String logoTimebUrl;
    private String apostas;
    private String estado;
    private Double valorAposta;
    private Double resultado;

    public ApostaModelDb() {
    }

    public ApostaModelDb(String userId, String logoTimeaUrl, String logoTimebUrl, String apostas, String estado,
                         Double valorAposta, Double resultado) {
        this.userId = userId;
        this.logoTimeaUrl = logoTimeaUrl;
        this.logoTimebUrl = logoTimebUrl;
        this.apostas = apostas;
        this.estado = estado;
        this.valorAposta = valorAposta;
        this.resultado = resultado;
    }

    public String getLogoTimeaUrl() {
        return logoTimeaUrl;
    }

    public void setLogoTimeaUrl(String logoTimeaUrl) {
        this.logoTimeaUrl = logoTimeaUrl;
    }

    public String getLogoTimebUrl() {
        return logoTimebUrl;
    }

    public void setLogoTimebUrl(String logoTimebUrl) {
        this.logoTimebUrl = logoTimebUrl;
    }

    public String getApostas() {
        return apostas;
    }

    public void setApostas(String apostas) {
        this.apostas = apostas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getValorAposta() {
        return valorAposta;
    }

    public void setValorAposta(Double valorAposta) {
        this.valorAposta = valorAposta;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getResultado() {
        return resultado;
    }

    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }
}
