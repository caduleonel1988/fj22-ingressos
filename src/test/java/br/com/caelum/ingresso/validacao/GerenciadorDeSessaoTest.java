package br.com.caelum.ingresso.validacao;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;

public class GerenciadorDeSessaoTest {

	private Filme akira;
	private Sala sala2D;
	private Sessao sessaoDasDez;
	private Sessao sessaoDasTreze;
	private Sessao sessaoDasDezoito;

	@Before
	public void preparaSessoes() {
		this.akira = new Filme("Akira", Duration.ofMinutes(120), "SCI-FI");
		this.sala2D = new Sala("Sala 2D");

		this.sessaoDasDez = new Sessao(LocalTime.parse("10:00:00"), sala2D, akira);
		this.sessaoDasTreze = new Sessao(LocalTime.parse("13:00:00"), sala2D, akira);
		this.sessaoDasDezoito = new Sessao(LocalTime.parse("18:00:00"), sala2D, akira);
	}

	@Test
	public void garanteQuePermitirUmaInsercaoEmUmaListaDeSessoesVazia() {
		
		List<Sessao> sessoes = Collections.emptyList();
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);
		Assert.assertTrue(gerenciador.cabe(sessaoDasTreze));
	}
	
	@Test
	public void garanteQueNaoPermiteSessaoNoMesmoHorario() {

		List<Sessao> sessoes = Arrays.asList(sessaoDasDez);
		GerenciadorDeSessao gerenciadorDeSessao = new GerenciadorDeSessao(sessoes);
		Assert.assertFalse(gerenciadorDeSessao.cabe(sessaoDasDez));
	}

	@Test
	public void garanteQueNaoPermiteSessoesTerminandoDentroDoHorarioDeOutraSessaoExistente() {

		List<Sessao> sessoes = Arrays.asList(sessaoDasDez);
		Sessao sessao = new Sessao(sessaoDasDez.getHorario().minusHours(1), sala2D, akira);
		GerenciadorDeSessao gerenciadorDeSessao = new GerenciadorDeSessao(sessoes);
		Assert.assertFalse(gerenciadorDeSessao.cabe(sessao));
	}

	@Test
	public void garanteQueNaoPermiteSessoesIniciandoDentroDoHorarioDeOutraSessaoExistente() {

		List<Sessao> sessoes = Arrays.asList(sessaoDasDez);
		Sessao sessao = new Sessao(sessaoDasDez.getHorario().plusHours(1), sala2D, akira);
		GerenciadorDeSessao gerenciadorDeSessao = new GerenciadorDeSessao(sessoes);
		Assert.assertFalse(gerenciadorDeSessao.cabe(sessao));
	}

	@Test
	public void garanteQueDevePermitirUmaInsercaoEntreDoisFilmes() {
		
		List<Sessao> sessoes = Arrays.asList(sessaoDasDez, sessaoDasDezoito);
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);
		Assert.assertTrue(gerenciador.cabe(sessaoDasTreze));
	}

}