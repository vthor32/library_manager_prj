# Relatório Teórico do Gerenciador de Biblioteca

## Introdução

Este relatório descreve a implementação de um gerenciador de biblioteca em Java. O sistema permite adicionar livros e patronos (usuários), buscar livros, realizar empréstimos e gerenciar devoluções. O objetivo é facilitar a administração de uma biblioteca, fornecendo funcionalidades para gerenciar e rastrear livros e usuários, além de autenticar administradores e bibliotecários.

## Estrutura do Código

### Objetivo Geral

O sistema visa criar uma solução eficiente para o gerenciamento de uma biblioteca. Inclui funcionalidades para adicionar e editar informações sobre livros e patronos, realizar e monitorar empréstimos, além de autenticar usuários para garantir a segurança do sistema.

### Componentes Principais

O sistema é composto por várias classes, cada uma com responsabilidades específicas para modularizar e organizar o código.

### Classe Principal: LibraryApp

A classe `LibraryApp` é o ponto de entrada do programa. Ela configura a interface gráfica principal e demonstra as principais funcionalidades do gerenciador de biblioteca:

- **Autenticação**: Exibe uma tela de login para autenticação do usuário. Usuários com o papel de admin podem adicionar novos usuários.
- **Navegação**: Permite a navegação entre diferentes painéis (livros, patronos, empréstimos, gerenciamento de usuários, busca).
- **Inicialização da Interface**: Configura a interface gráfica utilizando a biblioteca Swing.
- **Salvar e Carregar Dados**: Oferece funcionalidades automáticas para salvar e carregar dados da biblioteca após cada alteração nos registros.

### Autenticação: AuthManager

A classe `AuthManager` gerencia a autenticação dos usuários, permitindo o login e a adição de novos usuários:

- **Autenticar Usuário**: Verifica as credenciais de login (nome de usuário e senha) para autenticar um usuário.
- **Adicionar Usuário**: Permite que administradores adicionem novos usuários ao sistema, especificando um papel (admin ou bibliotecário). Garante que não haja duplicação de nomes de usuários.
- **Deletar Usuário**: Remove um usuário do sistema.
- **Obter Usuários**: Recupera a lista de todos os usuários.
- **Salvar e Carregar Dados**: Salva e carrega os dados dos usuários em um arquivo JSON.

### Livros: Book

A classe `Book` representa um livro na biblioteca. Inclui atributos como título, autor, ISBN, categoria e disponibilidade:

- **Atributos**: Título, autor, ISBN, categoria, disponibilidade.
- **Métodos de Acesso**: Getters e setters para acessar e modificar os atributos do livro.
- **Verificação de Disponibilidade**: Método para verificar se o livro está disponível para empréstimo.

### Gerenciamento de Livros: LibraryManager

A classe `LibraryManager` gerencia as operações da biblioteca:

- **Adicionar Livro**: Adiciona um novo livro ao sistema.
- **Editar Livro**: Permite a edição das informações de um livro existente.
- **Deletar Livro**: Remove um livro do sistema.
- **Buscar Livros**: Busca livros no sistema usando palavras-chave.
- **Adicionar Patrono**: Adiciona um novo patrono ao sistema.
- **Editar Patrono**: Permite a edição das informações de um patrono existente.
- **Deletar Patrono**: Remove um patrono do sistema.
- **Buscar Patronos**: Busca patronos no sistema usando palavras-chave.
- **Realizar Empréstimo**: Registra um empréstimo associando um livro a um patrono. Garante que um patrono só possa ter um empréstimo ativo por vez.
- **Gerenciar Devoluções**: Atualiza o status de um empréstimo quando o livro é devolvido.
- **Buscar Empréstimos em Atraso**: Retorna uma lista de empréstimos que estão em atraso.
- **Salvar Dados**: Salva os dados de livros, patronos e empréstimos em um arquivo JSON.
- **Carregar Dados**: Carrega os dados de livros, patronos e empréstimos de um arquivo JSON.

### Interface Gráfica: Painéis

#### BookPanel

Gerencia a interface gráfica para os livros da biblioteca:

- **Exibição de Livros**: Mostra uma lista de livros disponíveis na biblioteca.
- **Formulário de Entrada**: Permite a entrada e edição de informações sobre os livros.
- **Ações**: Adicionar, editar e deletar livros, além de limpar a seleção.

#### PatronPanel

Gerencia a interface gráfica para os patronos da biblioteca:

- **Exibição de Patronos**: Mostra uma lista de patronos cadastrados na biblioteca.
- **Formulário de Entrada**: Permite a entrada e edição de informações sobre os patronos.
- **Ações**: Adicionar, editar e deletar patronos, além de limpar a seleção.

#### LoanPanel

Gerencia a interface gráfica para os empréstimos de livros:

- **Exibição de Empréstimos**: Mostra uma lista de empréstimos, destacando os atrasados.
- **Formulário de Entrada**: Permite selecionar livros e patronos para realizar empréstimos.
- **Ações**: Realizar e devolver empréstimos, além de limpar a seleção.
- **Filtro de Empréstimos Abertos**: Possibilidade de filtrar e mostrar apenas empréstimos em aberto.

#### SearchPanel

Gerencia a interface gráfica para buscar livros e patronos:

- **Busca de Livros**: Permite buscar livros por título, autor, ISBN ou categoria.
- **Busca de Patronos**: Permite buscar patronos por nome ou informação de contato.
- **Exibição Inicial**: Mostra todos os livros e patronos cadastrados inicialmente.

#### UserManagementPanel

Gerencia a interface gráfica para adicionar e listar usuários:

- **Exibição de Usuários**: Mostra uma lista de usuários cadastrados no sistema.
- **Formulário de Entrada**: Permite a entrada de informações sobre novos usuários.
- **Ações**: Adicionar e deletar usuários.

### Empréstimos: Loan

A classe `Loan` gerencia o empréstimo de livros:

- **Atributos**: Livro, patrono, data do empréstimo, data de devolução, status de devolução.
- **Métodos de Acesso**: Getters e setters para acessar e modificar os atributos do empréstimo.
- **Verificação de Atraso**: Método para verificar se o empréstimo está em atraso.

## Funcionamento

### Autenticação de Usuários

Ao iniciar o aplicativo, a interface de login é apresentada ao usuário. O `AuthManager` verifica as credenciais fornecidas e, se válidas, permite o acesso ao sistema. Usuários com o papel de admin podem adicionar novos usuários.

### Gerenciamento de Livros

O `LibraryManager` permite adicionar, editar e deletar livros. A interface gráfica (`BookPanel`) fornece formulários para inserir informações sobre os livros e uma lista para visualizar os livros existentes. Os dados são persistidos em um arquivo JSON.

### Gerenciamento de Patronos

De forma similar aos livros, o `LibraryManager` permite adicionar, editar e deletar patronos. A interface gráfica (`PatronPanel`) facilita a entrada e visualização dos dados dos patronos.

### Empréstimos de Livros

Os empréstimos são gerenciados pela classe `Loan` e monitorados pelo `LibraryManager`. A interface gráfica (`LoanPanel`) permite selecionar livros e patronos para realizar e devolver empréstimos. O sistema rastreia a data do empréstimo e a data de devolução, notificando sobre empréstimos em atraso.

### Persistência de Dados

Os dados de livros, patronos e empréstimos são salvos em um arquivo JSON para garantir que as informações sejam preservadas entre execuções do programa. O `LibraryManager` utiliza a biblioteca Gson para serializar e desserializar os dados.

## Integrantes do Grupo

- Felipe Reis Corerato, 14569800
- Sofia Hahn de Pasquali, 14607722
- Vitor Marçal Brasil, 12822653
