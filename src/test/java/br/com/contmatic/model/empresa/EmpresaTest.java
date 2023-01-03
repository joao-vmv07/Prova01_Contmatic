package br.com.contmatic.model.empresa;

import static br.com.contimatic.model.util.Validacao.getErros;
import static br.com.contmatic.model.util.constantes.EmpresaConstante.CNPJ_INVALIDO_MESSAGE;
import static br.com.contmatic.model.util.constantes.EmpresaConstante.CNPJ_LETRAS_MASK_MESSAGE;
import static br.com.contmatic.model.util.constantes.EmpresaConstante.CNPJ_TAMANHO_MESSAGE;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.com.contmatic.model.endereco.Endereco;
import br.com.contmatic.model.telefone.Telefone;
import br.com.six2six.fixturefactory.Fixture;

public class EmpresaTest {

	private static Empresa empresaBefore;
	private static LocalDateTime data = LocalDateTime.now();
	
	@BeforeAll
	public static void setUp() {
	    loadTemplates("br.com.contmatic.templeate");
	    empresaBefore = Fixture.from(Empresa.class).gimme("valid");
	}
	
	@Test
	void deve_aceitar_cnpj_valido() {
		assertEquals("17081431000122", empresaBefore.getCnpj());
	}

	@Test
	void nao_deve_aceitar_cnpj_invalido() {
		empresaBefore.setCnpj("17081431000111");
		assertThat(getErros(empresaBefore), hasItem(CNPJ_INVALIDO_MESSAGE));
	}

	@Test
	void nao_deve_aceitar_cnpj_com_menos_de_14_caracteres() {
	    empresaBefore.setCnpj("1708143");
	    assertTrue(getErros(empresaBefore).contains(CNPJ_TAMANHO_MESSAGE));
	}

	@Test
	void nao_deve_aceitar_cnpj_com_mais_de_14_caracteres() {
	    empresaBefore.setCnpj("6245898600010322222");
        assertThat(getErros(empresaBefore), hasItem(CNPJ_TAMANHO_MESSAGE));
	}

	@Test
	void nao_deve_aceitar_cnpj_com_letras() {
	    empresaBefore.setCnpj("1708143100011Az");
        assertThat(getErros(empresaBefore), hasItem(CNPJ_LETRAS_MASK_MESSAGE));
	}
 
	@Test
	void nao_deve_aceitar_cnpj_com_caracteres_especial() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> new Empresa("62%5898600010!"),
				"Esperado IllegalArgumentException ao tentar criar CNPJ de Empresa com caracter especial:");
		assertTrue(thrown.getMessage()
				.contains("O campo CNPJ de Empresa não pode conter pontuação, letras e caracteres especiais."));
	}

	@Test
	void nao_deve_aceitar_cnpj_com_caracteres_iguais() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> new Empresa("11111111111111"),
				"Esperado IllegalArgumentException ao tentar criar CNPJ de Empresa com números iguais:");
		assertTrue(thrown.getMessage().contains("O CNPJ de Empresa informado é inválido."));
	}

	@Test
	void nao_deve_aceitar_cnpj_com_maskara() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> new Empresa("69.236.855/0001-12"),
				"Esperado IllegalArgumentException ao tentar criar CNPJ de Empresa com maskara:");
		assertTrue(thrown.getMessage()
				.contains("O campo CNPJ de Empresa não pode conter pontuação, letras e caracteres especiais."));
	}

	@Test
	void nao_deve_aceitar_cpnj_nulo() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Empresa(null),
				"Esperado IllegalArgumentException ao tentar criar Empresa com CPNJ Null:");
		assertTrue(thrown.getMessage().contains("O campo CNPJ de Emrpesa deve ser preenchido."));
	}

	@Test
	void nao_deve_aceitar_cpnj_vazio_com_espaco() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Empresa(" "),
				"Esperado IllegalArgumentException ao tentar criar Empresa com CNPJ vazio com espaço:");
		assertTrue(thrown.getMessage().contains("O campo CNPJ de Empresa não deve ser vazio"));
	}

	@Test
	void nao_deve_aceitar_cpnj_vazio_sem_espaco() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Empresa(""),
				"Esperado IllegalArgumentException ao tentar criar Empresa com CNPJ vazio sem espaço:");
		assertTrue(thrown.getMessage().contains("O campo CNPJ de Empresa não deve ser vazio"));
	}

	@Test
	void nao_deve_aceitar_cpnj_com_espaco() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> new Empresa("  1708143 100 122"),
				"Esperado IllegalArgumentException ao tentar criar Empresa CNPJ com espaço:");
		assertEquals("O campo CNPJ de Empresa não deve conter espaço.", thrown.getMessage());
	}

//RazaoSocial
	@Test
	void deve_aceitar_razao_social_valido() {
		empresaBefore.setRazaoSocial("NIKE");
		assertEquals("NIKE", empresaBefore.getRazaoSocial());
	}

	@Test
	void nao_deve_aceitar_razao_social_mais_40_caracteres() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setRazaoSocial("TESTE123TESTE123TESTE123TESTE123TESTE123TESTE1231213dadada1"),
				"Esperado IllegalArgumentException ao tentar criar Razao Social de Empresa com mais de 40 caracteres: ");
		assertEquals("O campo Razão Social de Empresa é permitido no maximo 40 caracteres.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_razao_social_menos_3_caracteres() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setRazaoSocial("AB"),
				"Esperado IllegalArgumentException ao tentar criar Razao Social de Empresa com menos de 3 caracteres:");
		assertEquals("O campo Razão Social de Empresa deve conter no mínimo 3 caracteres.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_razao_social_campo_nullo() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setRazaoSocial(null),
				"Esperado IllegalArgumentException ao tentar criar Razao Social de Empresa null:");
		assertEquals("O campo Razão Social de Empresa deve ser preenchido.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_razao_social_campo_vazio() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setRazaoSocial(""),
				"Esperado IllegalArgumentException ao tentar criar Razao Social de Empresa vazio");
		assertEquals("O campo Razão Social de Empresa não deve ser vazio.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_razao_social_campo_vazio_com_espaco() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setRazaoSocial(" "),
				"Esperado IllegalArgumentException ao tentar criar Razao Social de Empresa vazio com espaço: ");
		assertEquals("O campo Razão Social de Empresa não deve ser vazio.", thrown.getMessage());
	}

//NomeFantasia
	@Test
	void deve_aceitar_nome_fantasia_valido() {
		empresaBefore.setNomeFantasia("Adidas");
		assertEquals("Adidas", empresaBefore.getNomeFantasia());
	}

	@Test
	void nao_deve_aceitar_nome_fantasia_vazio() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setNomeFantasia(""),
				"Esperado IllegalArgumentException ao tentar criar NomeFantasia de Empresa vazio:");
		assertEquals("O campo Nome Fantasia de Empresa não deve ser vazio.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_nome_fantasia_null() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setNomeFantasia(null),
				"Esperado IllegalArgumentException ao tentar criar NomeFantasia de Empresa nulo:");
		assertEquals("O campo Nome Fantasia de Empresa deve ser preenchido.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_nome_fantasia_mais_40_caracteres() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setNomeFantasia("TESTE123TESTE123TESTE123TESTE123TESTE123TESTE1231213dadada1"),
				"Esperado IllegalArgumentException ao tentar criar NomeFantasia de Empresa com mais de 40 caracteres:");
		assertEquals("O campo Nome Fantasia de Empresa é permitido no maximo 40 caracteres.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_nome_fantasia_menos_3_caracteres() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setNomeFantasia("AB"),
				"Esperado IllegalArgumentException ao tentar criar NomeFantasia de Empresa com menos de 3 caracteres:");
		assertEquals("O campo Nome Fantasia de Empresa deve conter no mínimo 3 caracteres.", thrown.getMessage());
	}

//Telefone
	@Test
	void deve_aceitar_lista_telefone_valida() {
		Set<Telefone> telefones = new HashSet<>();
		telefones.add(new Telefone("55", "11", "967976463"));
		telefones.add(new Telefone("55", "11", "968904450"));
		empresaBefore.setTelefones(telefones);
		assertEquals(telefones, empresaBefore.getTelefones());

	}

	@Test
	void nao_deve_aceitar_lista_telefone_null() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setTelefones(null),
				"Esperado IllegalArgumentException ao tentar criar lista de Telefone Null em Empresa");
		assertEquals("O campo Telefone de Empresa deve ser preenchido.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_lista_telefone_vazio() {
		Set<Telefone> telefones = new HashSet<>();
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setTelefones(telefones),
				"Esperado IllegalArgumentException ao tentar criar lista de Telefone vazia em Empresa");
		assertEquals("O campo Telefone de Empresa não deve ser vazio.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_lista_telefone_maior_que_tres_contatos() {
		Set<Telefone> telefones = new HashSet<>();
		telefones.add(new Telefone("55", "11", "967945524"));
		telefones.add(new Telefone("55", "11", "55285908"));
		telefones.add(new Telefone("55", "11", "969945526"));
		telefones.add(new Telefone("55", "11", "960945527"));
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setTelefones(telefones),
				"Esperado IllegalArgumentException ao tentar criar lista de Telefone maior que três contatos em Empresa");
		assertEquals("O campo Telefone de Empresa deve conter no maximo três registros de contato.",
				thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_lista_telefone_menor_que_dois_contatos() {
		Set<Telefone> telefones = new HashSet<>();
		telefones.add(new Telefone("55", "11", "968945525"));
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setTelefones(telefones),
				"Esperado IllegalArgumentException ao tentar criar lista de Telefone vazia em Empresa");
		assertEquals("O campo Telefone de Empresa deve conter no mínimo dois registros de contato.",
				thrown.getMessage());
	}

//Endereco
	@Test
	void deve_aceitar_lista_endereco_valida() {
		Set<Endereco> enderecos = new HashSet<>();
		enderecos.add(new Endereco("04852505", 83));
		empresaBefore.setEnderecos(enderecos);
		assertEquals(enderecos, empresaBefore.getEnderecos());
	}

	@Test
	void nao_deve_aceitar_lista_endereco_null() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setEnderecos(null),
				"Esperado IllegalArgumentException ao tentar criar lista de Endereco Null em Empresa");
		assertEquals("O campo Endereço de Empresa deve ser preenchido.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_lista_endereco_vazio() {
		Set<Endereco> enderecos = new HashSet<>();
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setEnderecos(enderecos),
				"Esperado IllegalArgumentException ao tentar criar lista de Endereco vazia em Empresa");
		assertEquals("O campo Endereço de Empresa não deve ser vazio.", thrown.getMessage());
	}

	@Test
	void nao_deve_aceitar_lista_endereco_maior_que_dois_registros() {
		Set<Endereco> enderecos = new HashSet<>();
		enderecos.add(new Endereco("04854522", 81));
		enderecos.add(new Endereco("04256505", 82));
		enderecos.add(new Endereco("04852511", 83));
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setEnderecos(enderecos),
				"Esperado IllegalArgumentException ao tentar criar lista de Endereco vazia em Empresa");
		assertEquals("O campo Endereço de Empresa deve conter no maximo dois registros de localidade.",
				thrown.getMessage());
	}

//Equals
	@Test
	void deve_aceitar_objeto_com_valores_iguais() {
		Empresa empresaA = new Empresa("17081431000122");
		Empresa empresaB = new Empresa("17081431000122");
		assertEquals(true, empresaA.equals(empresaB));
	}

	@Test
	void deve_aceitar_objeto_valores_endereco_memoria_iguais() {
		Empresa empresaA = new Empresa("17081431000122");
		assertEquals(true, empresaA.equals(empresaA));
	}

	@Test
	void nao_deve_aceitar_equals_com_objeto_null() {
		Empresa empresaA = new Empresa("17081431000122");
		assertEquals(false, empresaA.equals(null));
	}

	@Test
	void nao_deve_aceitar_equals_objeto_de_classes_diferente() {
		Empresa empresaA = new Empresa("17081431000122");
		assertEquals(false, empresaA.equals(new Object()));
	}

//HashCode
	@Test
	void deve_ter_hashCode_iguais() {
		int hashcodeA = new Empresa("17081431000122").hashCode();
		int hashcodeB = new Empresa("17081431000122").hashCode();
		assertEquals(hashcodeA, hashcodeB);
	}

	@Test
	void nao_deve_ter_hashCode_iguais() {
		int hashcodeA = new Empresa("17081431000122").hashCode();
		int hashcodeB = new Empresa("76698308000114").hashCode();
		assertNotEquals(hashcodeA, hashcodeB);
	}

//AUDITORIA
//DataAlteração
	@Test
	void aceitar_data_alteracao_valida() {
		empresaBefore.setDataAlteracao(data);
		assertEquals(data, empresaBefore.getDataAlteracao());
	}

	@Test
	void nao_deve_aceitar_data_alteracao_mes_maior_que_atual() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> empresaBefore.setDataAlteracao(new LocalDateTime(2022,01,27,0,0,0,0)),
				"Esperado IllegalArgumentException ao tentar definir Data de Aleração mês maior que atual em Auditoria");
		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
	}
	
//	@Test
//	void nao_deve_aceitar_data_alteracao_mes_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao tentar definir Data de Aleração mês menor que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_ano_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao tentar definir Data de Aleração ano maior que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_ano_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao tentar definir Data de Aleração ano menor que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_dia_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao tentar definir Data de Aleração dia maior que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_dia_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao tentar definir Data de Aleração dia menor que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_hora_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao definir Horário de Data Alteração menor que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_hora_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao tentar definir Horário de Data Alteração maior que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_minutos_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao definir Minutos de Data Alteração menor que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_minutos_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao definir Minutos de Data Alteração maior que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_alteracao_com_valor_mes_maior_que_doze() {
//		DateTimeException thrown = assertThrows(DateTimeException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Expected doThing() to throw, but it didn't");
//		assertEquals("Invalid value for MonthOfYear (valid values 1 - 12): 2020", thrown.getMessage());
//	}
//
////DataCriação
//	@Test
//	void aceitar_data_criacao_valido() {
//		empresaBefore.setDataCriacao();
//		assertEquals(now().withNano(0), empresaBefore.getDataCriacao());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_mes_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Esperado IllegalArgumentException ao definir Data Criação mês menor que atual em Auditoria");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_mes_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Esperado IllegalArgumentException ao definir Data Criação mês maior que atual em Auditoria");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_ano_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Esperado IllegalArgumentException ao definir Data Criação ano maior que atual em Auditoria");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_ano_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Esperado IllegalArgumentException ao definir Data Criação ano menor que atual em Auditoria");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_com_valor_mes_maior_que_doze() {
//		DateTimeException thrown = assertThrows(DateTimeException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Expected doThing() to throw, but it didn't");
//		assertEquals("Invalid value for MonthOfYear (valid values 1 - 12): 2020", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_dia_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Expected doThing() to throw, but it didn't");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_dia_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Esperado IllegalArgumentException ao definir Data Criação dia menor que atual em Auditoria");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_hora_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Esperado IllegalArgumentException ao tentar definir Horário de Data Criação menor que atual em Auditoria");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_hora_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Esperado IllegalArgumentException ao tentar definir Horário de Data Alteração maior que atual em Auditoria");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_minutos_menor_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataCriacao(),
//				"Esperado IllegalArgumentException ao definir Minutos de Data Criação menor que atual em Auditoria");
//		assertEquals("A Data Criação informada de Auditoria é invalida.", thrown.getMessage());
//	}
//
//	@Test
//	void nao_deve_aceitar_data_criacao_minutos_maior_que_atual() {
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//				() -> empresaBefore.setDataAlteracao(),
//				"Esperado IllegalArgumentException ao definir Minutos de Data Criação maior que atual em Auditoria");
//		assertEquals("A Data Alteração informada de Auditoria é invalida.", thrown.getMessage());
//	}

//toString
	@Test
	void deve_conter_valores_dos_campos_tostring() {
		final String CNPJ = "17081431000122";
		final String NOME = "VIVO";
		final String RAZAO = "Vivo Telecomunicações";
		final String USERCRIACAO = "João";
		final String USERALTERACAO = "José";
		LocalDateTime DATA_CRIACAO = LocalDateTime.now();
		final LocalDateTime DATA_ALTERACAO = LocalDateTime.now();

		Set<Endereco> enderecos = new HashSet<>();
		enderecos.add(new Endereco("04852505", 83));

		Set<Telefone> telefones = new HashSet<>();
		telefones.add(new Telefone("55", "11", "967976463"));
		telefones.add(new Telefone("55", "11", "968984543"));

		Empresa empresa = new Empresa(CNPJ);
		empresa.setNomeFantasia(NOME);
		empresa.setRazaoSocial(RAZAO);
		empresa.setUsuarioCriacao(USERCRIACAO);
		empresa.setUsuarioAlteracao(USERALTERACAO);
		empresa.setEnderecos(enderecos);
		empresa.setTelefones(telefones);
		empresa.setDataCriacao(DATA_CRIACAO);
		empresa.setDataAlteracao(DATA_ALTERACAO);

		assertTrue(empresa.toString().contains(CNPJ));
		assertTrue(empresa.toString().contains(NOME));
		assertTrue(empresa.toString().contains(RAZAO));
		assertTrue(empresa.toString().contains(USERCRIACAO));
		assertTrue(empresa.toString().contains(USERALTERACAO));
		assertTrue(empresa.toString().contains(enderecos.toString()));
		assertTrue(empresa.toString().contains(telefones.toString()));
		assertTrue(empresa.toString().contains(DATA_CRIACAO.toString()));
		assertTrue(empresa.toString().contains(DATA_ALTERACAO.toString()));
	}
	
 @Test
  void example_validation_bean() {
	  ValidatorFactory factory;
      Validator validator;
      factory = buildDefaultValidatorFactory();
      validator = factory.getValidator();
      
      Empresa empresaA = new Empresa(null);
      Set<String> erros = new HashSet<>();
      Set<ConstraintViolation<Empresa>> violations = validator.validate(empresaA);
      for (ConstraintViolation<Empresa> violation : violations) {
          erros.add(violation.getMessageTemplate());
      }
  }
}