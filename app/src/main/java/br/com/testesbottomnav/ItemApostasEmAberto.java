package br.com.testesbottomnav;

public class ItemApostasEmAberto {

    private String imgTimeA, imgTimeB;
    private String todasAsApostas;
    private Double valor;
    private String estado;
    private Double resultado;

    public ItemApostasEmAberto(String imgTimeA, String imgTimeB, String todasAsApostas, Double valor, String estado, Double resultado) {
        this.imgTimeA = imgTimeA;
        this.imgTimeB = imgTimeB;
        this.todasAsApostas = todasAsApostas;
        this.valor = valor;
        this.estado = estado;
        this.resultado = resultado;
    }

    public String getImgTimeA() {
        return imgTimeA;
    }

    public void setImgTimeA(String imgTimeA) {
        this.imgTimeA = imgTimeA;
    }

    public String getImgTimeB() {
        return imgTimeB;
    }

    public void setImgTimeB(String imgTimeB) {
        this.imgTimeB = imgTimeB;
    }

    public String getTodasAsApostas() {
        return todasAsApostas;
    }

    public void setTodasAsApostas(String todasAsApostas) {
        this.todasAsApostas = todasAsApostas;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getResultado() {
        return resultado;
    }

    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }
}
