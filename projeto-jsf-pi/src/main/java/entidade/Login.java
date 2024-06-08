package entidade;

import java.util.Date;

public class Login {

	private String login;
	private String senha;
	private Date dataNascimento;

	public Login() {
	}

	public Login(String login, String senha) {
	   this.login = login;
	   this.senha = senha;
	}
	
	public Login(String login, String senha, Date dataNascimento) {
		this.login = login;
		this.senha = senha;
		this.dataNascimento = dataNascimento;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

}
