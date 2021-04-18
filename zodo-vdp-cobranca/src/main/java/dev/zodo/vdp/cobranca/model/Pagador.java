package dev.zodo.vdp.cobranca.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagador {
    private String nome;
    private String cpfCnpj;
    private Endereco endereco;
}
