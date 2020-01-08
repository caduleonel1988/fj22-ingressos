package br.com.caelum.ingresso.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.caelum.ingresso.dao.FilmeDao;
import br.com.caelum.ingresso.dao.SalaDao;
import br.com.caelum.ingresso.dao.SessaoDao;
import br.com.caelum.ingresso.model.Sessao;
import br.com.caelum.ingresso.model.form.SessaoForm;
import br.com.caelum.ingresso.validacao.GerenciadorDeSessao;

@Controller
public class SessaoController {

	@Autowired
	private SalaDao salaDao;
	
	@Autowired
	private FilmeDao filmeDao;
	
	@Autowired
	private SessaoDao sessaoDao ;
	
	// method to show the session form
	@GetMapping(value="/admin/sessao")
	public String form(@RequestParam("salaId") Integer salaId, Model model, SessaoForm form) {
		
		model.addAttribute("filmes", filmeDao.findAll());
		model.addAttribute("sala", salaDao.findOne(salaId));
		model.addAttribute("form", form);
		
		return "sessao/sessao";
	}
	
	// method to save session
	@PostMapping(value="/admin/sessao")
	@Transactional
	public String salva(@Valid SessaoForm form, BindingResult result, Model model) {
		
		if (result.hasErrors()) return form(form.getSalaId(), model, form);
		
		Sessao sessao = form.toSessao(salaDao, filmeDao);
		
		List<Sessao> sessoesDaSala = sessaoDao.buscaSessoesDaSala(sessao.getSala());
		
		GerenciadorDeSessao gerenciadorDeSessao = new GerenciadorDeSessao(sessoesDaSala);
		if (gerenciadorDeSessao.cabe(sessao)) {
			sessaoDao.save(sessao);
			return "redirect:/admin/sala/"+ form.getSalaId() + "/sessoes"; 
		}
		
		return form(form.getSalaId(), model, form);
		
	}
}
