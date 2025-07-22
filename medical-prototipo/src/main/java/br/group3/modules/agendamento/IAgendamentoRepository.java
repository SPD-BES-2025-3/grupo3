package br.group3.modules.agendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link Agendamento}.
 * Fornece métodos CRUD (Create, Read, Update, Delete) para operações de banco de dados
 * com agendamentos.
 *
 * @author Grupo 3
 * @see Agendamento
 */
@Repository
public interface IAgendamentoRepository extends JpaRepository<Agendamento, Long> {
}