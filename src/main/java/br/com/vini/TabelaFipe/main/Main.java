package br.com.vini.TabelaFipe.main;

import br.com.vini.TabelaFipe.models.DadosVeiculo;
import br.com.vini.TabelaFipe.models.ModelosVeiculo;
import br.com.vini.TabelaFipe.models.Veiculo;
import br.com.vini.TabelaFipe.service.ConsumoApi;
import br.com.vini.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        var menu = """
                *** OPÇÕES ***
                - Carro
                - Moto
                - Caminhão
                
                Digite uma das opções para consulta:
                """;
        System.out.println(menu);
        String endereco;

        var opcao = sc.nextLine();
        if (opcao.toLowerCase().contains("car")){
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mo")){
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, DadosVeiculo.class);
        marcas.stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);

        System.out.println("\nDidgite o código da Marca para realizar sua consulta:");
        var codigoMarca = sc.nextLine();

        endereco += "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista =  conversor.obterDados(json, ModelosVeiculo.class);

        System.out.println("\n Modelos dessa marca são: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite o nome do Modelo desejado: ");
        var nomeVeiculo = sc.nextLine();

        List<DadosVeiculo> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\n Modelos Filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("\n Digite o código do modelo escolhido para efetuarmos a avaliação: ");
        var codigoModdelo = sc.nextLine();

        endereco += "/" + codigoModdelo + "/anos";
        json = consumo.obterDados(endereco);
        List<DadosVeiculo> anos = conversor.obterLista(json, DadosVeiculo.class);

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);

        }

        System.out.println("Todos os veículos filtrados com avaliações por ano:");
        veiculos.forEach(System.out::println);




    }
}

