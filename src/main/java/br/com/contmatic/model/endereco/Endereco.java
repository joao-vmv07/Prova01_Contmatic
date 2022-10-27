package br.com.contmatic.model.endereco;

import static br.com.contmatic.model.util.constantes.EnderecoConstante.BAIRRO_TAMANHO_MAX;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.BAIRRO_TAMANHO_MAX_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.BAIRRO_TAMANHO_MIN;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.BAIRRO_TAMANHO_MIN_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.CEP_NULL_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.CEP_TAMANHO_FIXO;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.CEP_TAMANHO_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.CEP_VAZIO_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.NUMERO_NULL_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.NUMERO_TAMANHO_MAX;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.NUMERO_TAMANHO_MAX_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.NUMERO_TAMANHO_MIN;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.NUMERO_TAMANHO_MIN_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.NUMERO_VAZIO_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.RUA_TAMANHO_MAX;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.RUA_TAMANHO_MAX_MESSAGE;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.RUA_TAMANHO_MIN;
import static br.com.contmatic.model.util.constantes.EnderecoConstante.RUA_TAMANHO_MIN_MESSAGE;
import static br.com.contmatic.model.util.validacao.Validacao.checkNull;
import static br.com.contmatic.model.util.validacao.Validacao.checkTamahhoMaximo;
import static br.com.contmatic.model.util.validacao.Validacao.checkTamahhoMinimo;
import static br.com.contmatic.model.util.validacao.Validacao.checkTamanhoFixo;
import static br.com.contmatic.model.util.validacao.Validacao.checkVazio;

import java.util.Objects;

public class Endereco {

	private String rua;

	private String numero;

	private String bairro;

	private String cep;

	private String pais;

	private String uf;

	private String municipio;

	public Endereco(String cep, String numero) {
		this.setCep(cep);
		this.setNumero(numero); 
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		checkNull(cep, CEP_NULL_MESSAGE);
		checkVazio(cep, CEP_VAZIO_MESSAGE);
		checkTamanhoFixo(cep, CEP_TAMANHO_FIXO, CEP_TAMANHO_MESSAGE);
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		checkTamahhoMinimo(rua, RUA_TAMANHO_MIN, RUA_TAMANHO_MIN_MESSAGE);
		checkTamahhoMaximo(rua, RUA_TAMANHO_MAX, RUA_TAMANHO_MAX_MESSAGE);
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		checkNull(numero, NUMERO_NULL_MESSAGE);
		checkVazio(numero, NUMERO_VAZIO_MESSAGE);
		checkTamahhoMinimo(numero, NUMERO_TAMANHO_MIN, NUMERO_TAMANHO_MIN_MESSAGE);
		checkTamahhoMaximo(numero, NUMERO_TAMANHO_MAX, NUMERO_TAMANHO_MAX_MESSAGE);
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		checkTamahhoMinimo(bairro, BAIRRO_TAMANHO_MIN, BAIRRO_TAMANHO_MIN_MESSAGE);
		checkTamahhoMaximo(bairro, BAIRRO_TAMANHO_MAX, BAIRRO_TAMANHO_MAX_MESSAGE);
		this.bairro = bairro;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
 
	@Override
	public int hashCode() {
		return Objects.hash(cep, numero);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		return Objects.equals(cep, other.cep) && Objects.equals(numero, other.numero);
	} 

	@Override
	public String toString() {
		return "Endereco [rua=" + rua + ", numero=" + numero + ", bairro=" + bairro + ", cep=" + cep + ", pais=" + pais
				+ ", uf=" + uf + ", municipio=" + municipio + "]";
	}

}