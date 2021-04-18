package dev.zodo.vdp.cobranca.leitura;

import dev.zodo.vdp.cobranca.model.Boleto;
import dev.zodo.vdp.cobranca.model.BoletosGerados;
import dev.zodo.vdp.cobranca.util.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public abstract class LeituraParser {

    private final Registro registro;
    private final List<Boleto> boletos;
    private String arquivoOrigem;

    protected LeituraParser(Map<String, Object> valoresPadrao) {
        this.registro = new Registro();
        this.registro.definirValoresPadrao(valoresPadrao);
        this.boletos = new ArrayList<>();
    }

    protected LeituraParser() {
        this(Collections.emptyMap());
    }

    public BoletosGerados parseFile(File arquivo, Charset charset) throws IOException {
        arquivoOrigem = arquivo.getName();
        try (Stream<String> lines = Files.lines(arquivo.toPath(), charset)) {
            lines.forEachOrdered(this::parseLine);
        }
        finalizarRegistro();
        return BoletosGerados.getInstance(boletos);
    }

    public void resetRegistro() {
        registro.reset();
    }

    public void finalizarRegistro() {
        if (!registro.isEmpty()) {
            boletos.add(RegistroUtil.registroToBoleto(registro, this::parseCustomValues, arquivoOrigem, boletos.size() + 1));
        }
        resetRegistro();
    }

    public void iniciarRegistro() {
        if (!registro.isEmpty()) {
            finalizarRegistro();
        }
    }

    public void addValue(String key, String value) {
        registro.addValue(key, value);
    }

    public void addValueCumulative(String key, String value) {
        registro.addValueCumulative(key, value);
    }

    public void definirValoresPadrao(Map<String, Object> initialValues) {
        registro.definirValoresPadrao(initialValues);
    }

    public abstract void parseLine(String line);
    public abstract Map<String, Object> parseCustomValues(Map<String, Object> values);

    protected void splitLinesByMaxLength(List<String> lines, String line, int maxLength) {
        String value = line;
        while (value.length() > maxLength) {
            String tmp = value.substring(0, maxLength);
            int max = tmp.lastIndexOf(" ");
            lines.add(value.substring(0, max));
            value = value.substring(max).trim();
        }
        if (Util.isNotBlank(value)) {
            lines.add(value);
        }
    }
}
