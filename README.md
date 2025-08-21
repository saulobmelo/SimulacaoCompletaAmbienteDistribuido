# Relatório do Projeto: Simulação Completa de Ambiente Distribuído

## Objetivo

Este projeto tem como objetivo simular um ambiente distribuído, abordando comunicação entre processos, sincronização temporal, eleição de líderes, tolerância a falhas, políticas de acesso e monitoramento. A implementação contempla diferentes protocolos e tecnologias para ilustrar conceitos fundamentais de sistemas distribuídos.

## Estrutura do Projeto

```
src/
  Main.java
  start_all.bat
  common/
    AuthService.java
    LamportClock.java
    Message.java
  core/
    BullyElection.java
    GlobalCoordinator.java
    HeartbeatManager.java
    RingElection.java
    SnapshotManager.java
  net/
    MulticastListener.java
    MulticastSender.java
    TcpClient.java
    TcpServer.java
  rmi/
    RemoteNode.java
    RemoteNodeImpl.java
  scripts/
    compile.bat
    start_node.bat
src.iml
```

## Descrição dos Componentes

### Arquivo Principal

- **Main.java**  
  Ponto de entrada do sistema. Gerencia inicialização dos nós, integração dos módulos e controle dos grupos.

### Comunicação

- **net/TcpServer.java & net/TcpClient.java**  
  Implementam comunicação TCP entre nós do mesmo grupo, com suporte a autenticação por token.

- **net/MulticastSender.java & net/MulticastListener.java**  
  Realizam comunicação UDP multicast entre grupos, utilizada para coordenação de snapshots globais e troca de mensagens de controle.

### Sincronização Temporal

- **common/LamportClock.java**  
  Implementa relógio lógico de Lamport para ordenação de eventos distribuídos.

### Mensagens e Autenticação

- **common/Message.java**  
  Estrutura de mensagens trocadas entre os nós.

- **common/AuthService.java**  
  Geração e validação de tokens HMAC para autenticação de mensagens sensíveis.

### Eleição de Líderes

- **core/BullyElection.java**  
  Algoritmo Bully para eleição de líder no Grupo A.

- **core/RingElection.java**  
  Algoritmo Ring para eleição de líder no Grupo B.

- **core/GlobalCoordinator.java**  
  Gerencia eleição do supercoordenador entre líderes dos grupos.

### Tolerância a Falhas e Monitoramento

- **core/HeartbeatManager.java**  
  Implementa mecanismo de heartbeat para monitoramento dos nós e substituição automática após falhas consecutivas.

### Snapshot Global

- **core/SnapshotManager.java**  
  Coordena e registra snapshots globais do estado dos nós, integrando comunicação multicast.

### RMI (Grupo B)

- **rmi/RemoteNode.java & rmi/RemoteNodeImpl.java**  
  Interface e implementação para comunicação remota via RMI entre nós do Grupo B.

### Scripts de Automação

- **scripts/compile.bat**  
  Script para compilar todos os arquivos Java do projeto.

- **scripts/start_node.bat**  
  Script para iniciar um nó individual em uma nova janela.

- **start_all.bat**  
  Script para compilar e iniciar todos os nós automaticamente.

## Funcionamento Geral

1. **Inicialização:**  
   Os nós são inicializados via `Main.java`, podendo ser executados manualmente ou pelo script `start_all.bat`.

2. **Comunicação:**  
   - Intra-grupo: TCP (com autenticação).
   - Intergrupo: UDP Multicast.
   - Grupo B: RMI.

3. **Sincronização:**  
   Relógios de Lamport garantem ordenação lógica dos eventos.

4. **Eleição:**  
   Cada grupo elege seu líder por algoritmos distintos (Bully e Ring). Os líderes disputam o papel de supercoordenador.

5. **Monitoramento:**  
   Heartbeat detecta falhas e aciona substituição automática de nós.

6. **Snapshot:**  
   O supercoordenador dispara snapshots globais, registrados por todos os nós.

7. **Políticas de Acesso:**  
   Mensagens sensíveis exigem tokens HMAC válidos.

## Como Compilar e Executar

### Compilação

```bat
cd src
javac -d out Main.java common/*.java core/*.java net/*.java rmi/*.java
```
Ou execute:
```bat
start_all.bat
```

### Execução Manual

```bat
java -cp out Main A1 A 5001 A2,A3
java -cp out Main A2 A 5002 A1,A3
java -cp out Main A3 A 5003 A1,A2
java -cp out Main B1 B 6001 B2,B3
java -cp out Main B2 B 6002 B1,B3
java -cp out Main B3 B 6003 B1,B2
```
Ou execute todos os nós automaticamente:
```bat
start_all.bat
```

## Pontos de Destaque

- **Modularidade:** Cada funcionalidade está separada em módulos específicos.
- **Automação:** Scripts facilitam compilação e execução dos testes.
- **Tolerância a Falhas:** Heartbeat e substituição automática aumentam a robustez.
- **Políticas de Segurança:** Autenticação por token para mensagens sensíveis.
- **Flexibilidade:** Suporte a diferentes protocolos de eleição e comunicação.

## Observações Finais

- O projeto está preparado para integração com gRPC (Grupo A), mas a implementação depende de arquivos e dependências adicionais.
- Para testes, recomenda-se simular falhas, snapshots e eleições, observando os logs gerados.
- Ajuste os parâmetros de inicialização conforme a topologia desejada.