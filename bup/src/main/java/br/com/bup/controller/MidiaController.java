package br.com.bup.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.bup.annotation.ApenasAdministrador;
import br.com.bup.annotation.OpenTransaction;
import br.com.bup.dao.MidiaDAO;
import br.com.bup.domain.Midia;
import br.com.bup.domain.ModalidadePagamento;
import br.com.bup.web.UsuarioSession;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;

@Controller
public class MidiaController {
	private final static Logger LOGGER = LoggerFactory.getLogger(MidiaController.class);
	
	private final Result result;
	private final Validator validator;
	private final MidiaDAO midiaDAO;
	private final UsuarioSession usuarioSession;
	private final ResourceBundle i18n;
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected MidiaController() {
		this(null, null, null, null, null);
	}
	
	@Inject
	public MidiaController(Result result, Validator validator, MidiaDAO midiaDAO, UsuarioSession usuarioSession,
			ResourceBundle i18n) {
		this.result = result;
		this.validator = validator;
		this.midiaDAO = midiaDAO;
		this.usuarioSession = usuarioSession;
		this.i18n = i18n;
	}
	
	@ApenasAdministrador
	public void formulario() {
		LOGGER.debug("carregando formulario de midia");
		// simples formulario... futuramente receendo id para editar... ou
		// nao...
	}
	@OpenTransaction
	@Path("/midia/editar/{id}")
	public void editar(Long id) {
		LOGGER.debug("carregando formulario de midia com id: " + id);
		Midia midia = midiaDAO.buscarPorId(id);
		
		result.include("midia", midia);
		formulario();
	}
	@OpenTransaction
	public void atualizar(@NotNull Midia midia) {
		validator.onErrorRedirectTo(this).formulario(); // caso seja null...
		
		LOGGER.debug("atualizando modalidade de pagamento: " + midia);
		
		// validacoes...
		validator.validate(midia);
		validator.onErrorRedirectTo(this).editar(midia.getId());
		
		// recupera os dados q nao estao no formulario
		midia = atualizarEntidadeDoFormulario(midia);
		
		// atualiza
		midia = midiaDAO.salvar(midia);
		
		result.include("success", "midia atualizada com sucesso.");
		result.redirectTo(IndexController.class).index();
	}
	
	/**
	 * Retorna uma entidade atualizada com o banco e a passada pro metodo,
	 * mantendo os atributos do formulario da entidade passada.
	 * 
	 * @param modalidadePagamento
	 * @return Entidade atualizada.
	 */
	private Midia atualizarEntidadeDoFormulario(Midia midia) {
		Midia midiaAtualizada = midiaDAO.buscarPorId(midia.getId());
		
		//TODO testar o BeanBuild... sl oq
		midiaAtualizada.setTipo(midia.getTipo());
		
		return midiaAtualizada;
	}
	@OpenTransaction
	@ApenasAdministrador
	public void criar(@NotNull Midia midia) {
		validator.onErrorRedirectTo(this).formulario(); // caso seja null...
		LOGGER.debug("criando midia: " + midia);
		
		
		
		// validacoes...
		validarCriar(midia);
		validator.onErrorRedirectTo(this).formulario();
		
		// salva
		midia = midiaDAO.salvar(midia);
		
		result.include("success", "Midia criado com sucesso.");
		result.redirectTo(IndexController.class).index();
	}
	
	private void validarCriar(Midia midia) {
		validator.validate(midia);
		
		// TODO validar inclusao repetida
	}
	
	@OpenTransaction
	@ApenasAdministrador
	public List<Midia> listar() {
		LOGGER.debug("Listando as midias ");
		
		return midiaDAO.buscarTodos();
	}
	
	@Path("/midia/apagar/{id}")
	@OpenTransaction
	@ApenasAdministrador
	public void apagar(Long id) {
		midiaDAO.apagarPorId(id);
		
		result.include("success", i18n.getString("msg.success.apagar"));
		result.redirectTo(this).listar();
	}
}
