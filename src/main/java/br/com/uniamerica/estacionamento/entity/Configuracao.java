package br.com.uniamerica.estacionamento.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import java.math.BigDecimal;
import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;


    @Entity
    @Audited
    @Table(name = "configuracao", schema = "public")
    @AuditTable(value = "configuracao_audit", schema = "audit")
    public class Configuracao extends AbstractEntity{

        @Getter @Setter
        @NotNull(message = "O valor da hora é obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "O valor hora deve ser maior que zero")
        @Column(name = "valor_hora")
        private BigDecimal valorHora;

        @Getter @Setter
        @NotNull(message = "O valor da multa por minuto é obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "O valor hora deve ser maior que zero")
        @Column(name = "valor_minuto_multa")
        private BigDecimal valorMultaMinuto;

        @Getter @Setter
        @Column(name = "inicio_expediente")
        @NotNull(message = "O início do expediente é obrigatório")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH':'mm'")
        private LocalTime inicioExpediente;

        @Getter @Setter
        @Column(name = "fim_expediente")
        @NotNull(message = "O fim do expediente é obrigatório")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH':'mm'")
        private LocalTime fimExpediente;

        @Getter @Setter
        @Column(name = "tempo_para_desconto")
        private LocalTime tempoParaDesconto;

        @Getter @Setter
        @Column(name = "tempo_de_desconto")
        private LocalTime tempoDeDesconto;

        @Getter @Setter
        @Column(name = "gerar_desconto")
        private boolean gerarDesconto;

        @Getter @Setter
        @Column(name = "vagas_carro")
        private int vagasCarro;

        @Getter @Setter
        @Column(name = "vagas_moto")
        private int vagasMoto;

        @Getter @Setter
        @Column(name = "vagas_van")
        private int vagasVan;
    }

