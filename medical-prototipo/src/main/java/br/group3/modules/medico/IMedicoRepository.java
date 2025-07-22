package br.group3.modules.medico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link Medico}.
 * <p>
 * Esta interface estende {@link JpaRepository}, fornecendo automaticamente
 * métodos CRUD (Create, Read, Update, Delete) e funcionalidades de paginação
 * e ordenação para a entidade {@link Medico}.
 * </p>
 * <p>
 * Não é necessário implementar esta interface, pois o Spring Data JPA
 * gera uma implementação em tempo de execução.
 * </p>
 *
 * @author Grupo 3
 * @see Medico
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface IMedicoRepository extends JpaRepository<Medico, Long> {
}