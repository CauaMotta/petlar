package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço de Autenticação responsável por carregar os detalhes de um usuário
 * durante o processo de autenticação do Spring Security.
 * <p>
 * Esta classe implementa a interface {@code UserDetailsService},
 * sendo obrigatória para a integração com o mecanismo de login do framework.
 */
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private IUserRepository repository;

    /**
     * Carrega os detalhes do usuário a partir do seu email.
     * <p>
     * Este é o método principal invocado pelo Spring Security durante uma tentativa de login.
     * Ele delega a busca ao repositório {@code IUserRepository}.
     *
     * @param username O email do usuário fornecido na tentativa de autenticação.
     * @return Um objeto {@code UserDetails}
     * contendo os detalhes do usuário para verificação de senha e autorizações.
     * @throws UsernameNotFoundException Se o usuário com o username especificado não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }
}
