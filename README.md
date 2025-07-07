# Sistema de Reservas em Hotéis

## Descrição

Este sistema tem como objetivo facilitar a gestão de reservas em hotéis, permitindo o controle de hóspedes, quartos, tipos de quarto e pagamentos. O sistema deve oferecer uma interface intuitiva para o gerenciamento de hóspedes, alocação de quartos disponíveis, controle de check-in e check-out, além de acompanhar pagamentos realizados ou pendentes.

Cada quarto está vinculado a um tipo (ex: simples, duplo, suíte) com suas respectivas características (capacidade, preço por diária, etc.). As reservas devem conter as datas de entrada e saída, o quarto reservado, os dados do hóspede e a situação do pagamento.

O sistema também deve oferecer funcionalidades administrativas, como a geração de relatórios com filtros (por período, situação da reserva, tipo de quarto) e a exportação desses relatórios em formatos como PDF e CSV, com formatação clara e organizada.

## Requisitos Funcionais

### 1. Gerenciamento de Hóspedes

- **REQ01**: Permitir o gerenciamento completo de hóspedes (cadastro, edição, exclusão e consulta), armazenando dados como nome completo, CPF, telefone, e-mail e endereço.

### 2. Gerenciamento de Quartos

- **REQ02**: Permitir o gerenciamento de quartos, incluindo definição do número, andar, status (disponível, ocupado, em manutenção) e associação com um tipo de quarto.

### 3. Gerenciamento de Tipos de Quarto

- **REQ03**: Permitir o gerenciamento de tipos de quarto, incluindo nome do tipo, capacidade máxima de pessoas, valor da diária e descrição.

### 4. Gestão de Reservas

- **REQ04**: Permitir o gerenciamento de reservas, vinculando um hóspede a um quarto e a um intervalo de datas (entrada e saída).
- **REQ05**: Validar a disponibilidade do quarto no período solicitado antes de confirmar a reserva.
- **REQ06**: Impedir o cancelamento de reservas cujo check-in já tenha sido realizado.
- **REQ07**: Permitir a reatribuição de datas ou troca de quarto apenas se não houver conflito de disponibilidade.
- **REQ08**: Reservas devem conter status (ativa, concluída, cancelada) e data de criação/modificação.

### 5. Controle de Check-in e Check-out

- **REQ09**: Permitir que um hóspede realize o check-in apenas na data de entrada ou até um dia após essa data.
- **REQ10**: Após o check-in, o status da reserva deve ser atualizado e o quarto marcado como ocupado.
- **REQ11**: O check-out só pode ser realizado após o check-in e até a data de saída.
- **REQ12**: Após o check-out, o status da reserva deve ser atualizado como concluída e o quarto marcado como disponível.

### 6. Gestão de Pagamentos

- **REQ13**: Permitir o registro de pagamentos vinculados a reservas, incluindo valor total, valor pago, data e método de pagamento (dinheiro, cartão, PIX, etc.).
- **REQ14**: Controlar o status do pagamento como "pendente", "parcial" ou "concluído".
- **REQ15**: O check-out só pode ser realizado se o pagamento estiver concluído ou autorizado pela gerência.

### 7. Consulta de Reservas

- **REQ16**: Permitir a consulta de reservas filtrando por período, tipo de quarto, status da reserva, ou nome do hóspede.
- **REQ17**: Apresentar os resultados com informações completas da reserva, incluindo status do pagamento, tipo de quarto e situação atual.

### 8. Relatórios e Exportações

- **REQ18**: Gerar relatórios de ocupação por período, com agrupamento por tipo de quarto e status da reserva.
- **REQ19**: Gerar relatórios de reservas pendentes, concluídas e canceladas.
- **REQ20**: Permitir a exportação de relatórios nos formatos **PDF** e **CSV**, com formatação de colunas, cabeçalhos, totais e agrupamentos.

## Possíveis APIs/Bibliotecas a Serem Usadas

- **JavaFX** – Interface gráfica do sistema.
- **JDBC / Hibernate** – Acesso e manipulação do banco de dados.
- **Apache POI** – Geração de relatórios em formato CSV ou Excel.
- **iText / JasperReports** – Exportação de relatórios em PDF.
- **JFreeChart** – Geração de gráficos simples para visualização de dados.
- **JUnit / Mockito** – Testes unitários e mocks.

## Integrantes do grupo com nome completo

- Pedro Henrique Nascimento Cardoso - pedro.henriquen@ufrpe.br
- Brenno Leite Pereira - brenno.leite@ufrpe.br
- Matheus Eduardo da Silva Bastos - matheus.bastos@ufrpe.br
- Kelven Alves Gomes - kelven.alves@ufrpe.com.br
- Erik Vinicius Gomes de Lima - erik.lima@ufrpe.br
