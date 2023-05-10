package br.com.uniamerica.estacionamento.servece;
import br.com.uniamerica.estacionamento.Repository.CondutorRepository;
import br.com.uniamerica.estacionamento.Repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.entity.Condutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CondutorService {

    @Autowired
    private CondutorRepository condutorRepository;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Transactional
    public Condutor cadastrar( Condutor condutor){

        Optional<Condutor> nomeJaCadastrado = condutorRepository.findByNome(condutor.getNome());
        Optional<Condutor> telefoneJaCadastrado = condutorRepository.findByTelefone(condutor.getTelefone());
        Optional<Condutor> cpfJaCadastrado = condutorRepository.findByCpf(condutor.getCpf());

        String cpf = condutor.getCpf();
        String telefone = condutor.getTelefone();
        String nome = condutor.getNome();

        if (nomeJaCadastrado.isPresent()){
            throw new IllegalArgumentException("Já há um condutor cadastrado com esse nome");
        }else if (nome == null || !nome.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("O nome deve conter apenas letras ");
        }

        if (telefoneJaCadastrado.isPresent()){
            throw new IllegalArgumentException("Já há um condutor cadastrado com esse telefone");
        }else if (telefone == null || !telefone.matches("\\(\\d{2}\\)\\d{5}-\\d{4}")) {
            throw new IllegalArgumentException("O telefone deve estar no formato (XX)XXXXX-XXXX");
        }

        if (cpfJaCadastrado.isPresent()){
            throw new IllegalArgumentException("Já há um condutor cadastrado com esse cpf");
        }else if (cpf == null || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new IllegalArgumentException("O CPF deve estar no formato ___.___.___-__");
        }


       return this.condutorRepository.save(condutor);
    }

    @Transactional
    public Condutor update( Condutor condutor){

        Optional<Condutor> id = condutorRepository.findById(condutor.getId());
        Optional<Condutor> nomeJaCadastrado = condutorRepository.findByNome(condutor.getNome());
        Optional<Condutor> telefoneJaCadastrado = condutorRepository.findByTelefone(condutor.getTelefone());
        Optional<Condutor> cpfJaCadastrado = condutorRepository.findByCpf(condutor.getCpf());

        String cpf = condutor.getCpf();
        String telefone = condutor.getTelefone();
        String nome = condutor.getNome();

        if (nomeJaCadastrado.isPresent() && !nomeJaCadastrado.get().getId().equals(condutor.getId())){
            throw new IllegalArgumentException("Já há um condutor cadastrado com esse nome");
        }else if (nome == null || !nome.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("O nome deve conter apenas letras ");
        }

        if (telefoneJaCadastrado.isPresent() && !telefoneJaCadastrado.get().getId().equals(condutor.getId())){
            throw new IllegalArgumentException("Já há um condutor cadastrado com esse telefone");
        }else if (telefone == null || !telefone.matches("\\(\\d{2}\\)\\d{5}-\\d{4}")) {
            throw new IllegalArgumentException("O telefone deve estar no formato (XX)XXXXX-XXXX");
        }

        if (cpfJaCadastrado.isPresent() && !cpfJaCadastrado.get().getId().equals(condutor.getId())){
            throw new IllegalArgumentException("Já há um condutor cadastrado com esse cpf");
        }else if (cpf == null || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new IllegalArgumentException("O CPF deve estar no formato XXX.XXX.XXX-XX");
        }

        return this.condutorRepository.save(condutor);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Condutor> optionalCondutor = this.condutorRepository.findById(id);
        if (optionalCondutor.isPresent()) {
            Condutor condutor = optionalCondutor.get();
            if (this.movimentacaoRepository.existsByCondutor(condutor)) {
                throw new IllegalArgumentException("O condutor está presente em alguma movimentação e não pode ser excluído.");
            } else {
                this.condutorRepository.delete(condutor);
            }
        } else {
            throw new IllegalArgumentException("Condutor não encontrado");
        }
    }
}
