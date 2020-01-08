package br.com.caelum.ingresso.validacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.caelum.ingresso.model.Sessao;

public class GerenciadorDeSessao {
	
	private List<Sessao> sessoesDaSala;

	public GerenciadorDeSessao(List<Sessao> sessoesDaSala) {
		this.sessoesDaSala = sessoesDaSala;
	}
	
	public boolean horarioIsValido(Sessao sessaoExistente, Sessao sessaoAtual) {
		
		LocalDate hoje = LocalDate.now();
		
		LocalDateTime horarioSessaoExistente = sessaoExistente.getHorario().atDate(hoje);
		LocalDateTime horarioAtual = sessaoAtual.getHorario().atDate(hoje);
		
		boolean ehAntes = horarioAtual.isBefore(horarioSessaoExistente);
		
		if (ehAntes) {
			return horarioAtual
						.plus(sessaoAtual.getFilme().getDuracao())
						.isBefore(horarioSessaoExistente);
		} else {
			return horarioSessaoExistente
						.plus(sessaoExistente.getFilme().getDuracao())
						.isBefore(horarioAtual);
		}
	}
	
	public boolean cabe(Sessao sessao) {
		
		return sessoesDaSala
				.stream()
				.map(sessaoExistente -> horarioIsValido(sessaoExistente, sessao))
				.reduce(Boolean::logicalAnd)
				.orElse(true);
	}
	
	

}
