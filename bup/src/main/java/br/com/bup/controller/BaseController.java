package br.com.bup.controller;

import java.util.ResourceBundle;

import br.com.bup.web.UsuarioSession;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Severity;
import br.com.caelum.vraptor.validator.Validator;

public abstract class BaseController{
	protected final Result result;
	protected final Validator validator;
	protected final UsuarioSession usuarioSession;
	protected final ResourceBundle i18n;
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected BaseController() {
		this(null, null, null, null);
	}
	public BaseController(Result result, Validator validator, UsuarioSession usuarioSession, ResourceBundle i18n) {
		super();
		this.result = result;
		this.validator = validator;
		this.usuarioSession = usuarioSession;
		this.i18n = i18n;
	}
	
	/**
	 * Adiciona uma msg de erro no Validator com chave "error".
	 * 
	 * @param keyMsg
	 *            Key da msg no message.properties.
	 */
	protected void addErrorMsg(String keyMsg, Object... parametros) {
		validator.add(new I18nMessage("error", keyMsg, parametros));
	}
	
	/**
	 * Seta uma msg de sucesso no validator com chave "success".
	 * 
	 * @param keyMsg
	 *            Key da msg no message.properties.
	 * @param parametros P
	 */
	protected void addSuccessMsg(String keyMsg, Object... parametros) {
		validator.add(new I18nMessage("success", keyMsg, Severity.SUCCESS, parametros));
//		MessageFormat.format(bundle.getString(message), i18n(parameters));
//		result.include("success", i18n.getString(keyMsg));
	}
}
