package br.com.uniamerica.estacionamento.Repository;

import br.com.uniamerica.estacionamento.entity.Condutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CondutorRepository extends JpaRepository<Condutor, Long> {


     List<Condutor> findByAtivo(boolean ativo);
}