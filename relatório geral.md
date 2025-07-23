# Sistema de Gerenciamento de Clínicas ou Consultórios Médicos  
### Relatório

**Autores**:  
Ana Carolyne Pereira de Souza  
Brenna de Mesquita Amorim  
Matheus Queiroz Rocha Barbosa  
Marcos Reges Mota  
Maurício Moraes Preto Carvalho

**Instituição**:  
Instituto de Informática – Universidade Federal de Goiás (UFG)  
Emails:  
ana.carolyne@discente.ufg.br, brennaamorim@discente.ufg.br,  
matheus_queiroz@discente.ufg.br, regesmota@discente.ufg.br, moraesmauricio@discente.ufg.br

---

## Abstract

This report presents the development of a management system for clinics and medical offices, focusing on the integration of relational and NoSQL databases. The system aims to improve the organization, storage, and access to medical information, optimizing patient care and clinical data management. It covers object-oriented modeling, class, component, and sequence diagrams, as well as the implementation using Spring Boot, PostgreSQL, MongoDB, Redis, and a graphical interface in FXML. The report highlights the team's task division, the adopted methodology, and the technical objectives to ensure efficiency, scalability, and security in data management.

## Resumo

Este relatório descreve o desenvolvimento de um sistema de gerenciamento para clínicas e consultórios médicos, com ênfase na integração entre bancos de dados relacionais e NoSQL. O sistema busca otimizar o armazenamento, a organização e o acesso a dados clínicos, promovendo maior eficiência no atendimento. São discutidos aspectos da modelagem orientada a objetos, diagramas UML e a implementação usando tecnologias como Spring Boot, PostgreSQL, MongoDB, Redis e FXML. Também são apresentadas a divisão de tarefas e os objetivos do projeto, que visam garantir desempenho, escalabilidade e segurança no gerenciamento das informações médicas.

---

## 1. Descrição do Problema

Clínicas e consultórios médicos enfrentam dificuldades para manter registros consistentes, centralizados e acessíveis em tempo hábil. Muitos desses locais ainda utilizam sistemas manuais ou plataformas fragmentadas, o que gera perda de dados, duplicidade de informações e atrasos em atendimentos. Além disso, a integração entre diferentes tipos de armazenamento (como bancos relacionais e NoSQL) se torna essencial para garantir escalabilidade, flexibilidade e desempenho na gestão de grandes volumes de informações médicas.

## 2. Motivação

A motivação para o desenvolvimento deste projeto está na oportunidade de aplicar conceitos de modelagem orientada a objetos, boas práticas de engenharia de software, persistência de software e estruturas de banco de dados modernos em um contexto real e altamente relevante: o setor de saúde.

A construção de diagramas como o de classes, componentes e sequência permite não apenas entender e representar o funcionamento de um sistema complexo, mas também planejar a integração entre tecnologias distintas, como bancos relacionais e NoSQL, de maneira eficaz e organizada. Isso simula cenários reais enfrentados por desenvolvedores e analistas de sistemas em empresas da área médica.

## 3. Objetivo Geral

O objetivo geral deste projeto é desenvolver um sistema de gerenciamento para clínicas e consultórios médicos, que seja capaz de integrar dados provenientes de diferentes fontes, como bancos de dados relacionais e NoSQL. A proposta visa promover maior eficiência no atendimento, garantir a persistência segura das informações e facilitar a manipulação dos dados médicos e administrativos.

### Objetivos específicos:

- Identificar os principais requisitos funcionais e não funcionais de um sistema de gestão médica;
- Projetar um modelo orientado a objetos que represente adequadamente os dados e processos envolvidos;
- Construir diagramas de classes, componentes e sequência que permitam visualizar a estrutura e o comportamento do sistema;
- Implementar funcionalidades básicas, como o cadastro de pacientes e médicos, o agendamento de consultas e o armazenamento de prontuários;
- Integrar tecnologias distintas de banco de dados para gerenciar diferentes tipos de informações;
- Avaliar o desempenho e a escalabilidade do sistema em diferentes cenários simulados.

### Tecnologias utilizadas:

- **Backend:** Spring Boot (Java) com ORM e PostgreSQL
- **ODM:** Python com Flask e MongoDB
- **Comunicação:** Redis como intermediador entre Java e Python
- **Frontend:** FXML (JavaFX)

---

## 4. Divisão de Tarefas

A equipe foi organizada em dois grupos principais, conforme a afinidade com as etapas do projeto e as competências individuais.

### Ana Carolyne e Brenna

- **Seção 1 - Introdução**: desenvolvimento da justificativa, descrição do problema, motivação e demais aspectos introdutórios do projeto;
- **Seção 2 - Plano**: elaboração do objetivo geral e dos objetivos específicos, bem como a definição das tecnologias e ferramentas que serão utilizadas no desenvolvimento do sistema;
- **Seção 3 - Divisão de tarefas**: organização das atividades do grupo, detalhamento das tarefas (*issues*), atribuição de responsabilidades, prazos e cronograma de execução;
- **Seção 4 - Modelagem inicial**: construção do diagrama de classes com as relações entre os objetos (1:1, 1:N, N:M), definição da arquitetura do sistema por meio de um diagrama de componentes e elaboração de diagramas de sequência para representar operações de integração entre partes do sistema.

### Matheus, Maurício e Marcos

- **Seção 5 - Documentação de código inicial**: preparação do ambiente com instruções sobre como instalar, executar e testar o código inicial do sistema; criação da documentação do código utilizando *JavaDoc* e *Markdown*, assegurando clareza e organização para futuros desenvolvedores;
- **Seção 6 - Testes Unitários**: elaboração dos testes unitários utilizando o framework *JUnit*, com foco nas classes de domínio, repositórios e demais partes essenciais do código-fonte.

Essa divisão busca garantir o equilíbrio na carga de trabalho e o aproveitamento das habilidades técnicas e analíticas de cada integrante, promovendo a colaboração eficiente entre todos os membros da equipe.
