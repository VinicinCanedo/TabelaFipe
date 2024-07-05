package br.com.vini.TabelaFipe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ModelosVeiculo(List<DadosVeiculo> modelos) {
}
