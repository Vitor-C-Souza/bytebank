package me.vitorcsouza.bytebank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;

@Entity(name = "conta_tb")
@Getter
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String agencia;
    private Integer numeroConta;
    private Double valor;
    private LocalDate mesReferencia;

    public Conta(String nome, String cpf, String agencia, Integer numeroConta, Double valor, LocalDate mesReferencia) {
        this.nome = nome;
        this.cpf = cpf;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.valor = valor;
        this.mesReferencia = mesReferencia;
    }
}
