package dev.zodo.vdp.cobranca.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BoletosGerados {
    private List<Boleto> boletos;
    private int total;

    public static BoletosGerados getInstance(List<Boleto> bols) {
        BoletosGerados boletosGerados = new BoletosGerados();
        boletosGerados.boletos = bols;
        boletosGerados.total = bols.size();
        return boletosGerados;
    }

    public void atualizarCodigoBeneficiario(String codigo) {
        boletos.forEach(boleto -> boleto.getCobranca().setCodigoBeneficiario(codigo));
    }

    public Beneficiario getBeneficiario() {
        return boletos.stream().map(Boleto::getBeneficiario)
                .findAny()
                .orElse(null);
    }
}
