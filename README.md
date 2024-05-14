# Projeto de Agendamento de Consultas

Olá e seja muito bem-vindo ao projeto da disciplina de Projeto Integrador!

1. **Cadastro de Usuário:**
   - Criar uma página para realizar o cadastro de um usuário com pelo menos as seguintes variáveis: id, login, senha, nome, ativo, dataNascimento e dataCadastro (gerada automaticamente).

2. **Login de Usuário:**
   - Criar uma página de login que dará acesso à área administrativa do sistema. O usuário deve fornecer login e senha, que será verificado no banco de dados. Caso os dados estejam incorretos, uma mensagem de erro será exibida.

3. **Recuperação/Troca de Senha:**
   - Criar uma funcionalidade que permita ao usuário recuperar ou trocar sua senha, caso ele saiba o login e a data de nascimento.

4. **Área Administrativa:**
   - Criar a página home que exiba as informações do usuário logado.
   - Criar uma página para realizar o CRUD de usuário.

5. **Cadastro de Agendamentos:**
   - Implementação de uma página para o cadastro de agendamentos. Os dados mínimos necessários incluem: ID, nome do paciente, e-mail, status do agendamento (agendado, cancelado ou realizado), clínica, médico, data e hora do agendamento, e data de cadastro (gerada automaticamente).
   - Agendamentos recém-cadastrados serão automaticamente marcados como "agendado".
   - Verificação para evitar o cadastro de agendamentos duplicados, data, hora e médico. Caso um agendamento já exista, o usuário será notificado.
   - Após o cadastro de um agendamento, o paciente receberá o ID do agendamento.

6. **Cadastro de Médico:**
   - Implementação de uma página para o cadastro de médicos. Os dados mínimos necessários incluem: ID e nome do médico.


## Documentação

5. **Relatório:**
   - Criar um relatório informando as tecnologias utilizadas para desenvolver o software.
  
# Como Executar o Projeto

### Configuração do Banco de Dados

Este projeto já está previamente configurado para utilizar o PostgreSQL como banco de dados padrão.

1. Abra o arquivo `persistence.xml`.
2. Altere a propriedade `<property name="javax.persistence.jdbc.user" value="postgres"/>` onde o value será o user do seu postgres.
3. Altere a propriedade `<property name="javax.persistence.jdbc.password" value="postgres"/>` onde o value será a senha do seu postgres.
4. Reinicie o aplicativo para aplicar as alterações.

### Configuração do Servidor

Este projeto utiliza o Apache Tomcat 9 como servidor padrão. Certifique-se de ter o Tomcat 9 instalado em seu ambiente de desenvolvimento.

1. Baixe e instale o Apache Tomcat 9 em seu ambiente de desenvolvimento, se ainda não estiver instalado.
2. Certifique-se de configurar o Tomcat 9 corretamente no seu ambiente.
4. Inicie o Tomcat 9 e verifique se o aplicativo está sendo executado corretamente.

Certifique-se de ajustar quaisquer configurações adicionais de acordo com o seu ambiente de desenvolvimento específico.

**Nota**: Certifique-se de ter o PostgreSQL instalado e configurado corretamente antes de usar. Além disso, crie um banco de dados chamado `jsf-pi`.

## Tecnologias Utilizadas

- Java
- JSF (JavaServer Faces)
- PrimeFaces
- HTML
- CSS
- JavaScript
- Tomcat (v9)
- Banco de dados relacional (PostgreSQL)

