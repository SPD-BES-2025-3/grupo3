package br.group3.modules.paciente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de Data Access Object (DAO) para a entidade {@link Paciente}.
 * <p>Esta interface estende {@link JpaRepository}, fornecendo métodos CRUD
 * básicos e funcionalidades de paginação e ordenação para a entidade Paciente.
 * Não são necessários métodos adicionais aqui, pois a funcionalidade padrão
 * do Spring Data JPA já atende às necessidades básicas de persistência para Paciente.</p>
 *
 * @author Grupo 3
 * @see Paciente
 * @see JpaRepository
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}