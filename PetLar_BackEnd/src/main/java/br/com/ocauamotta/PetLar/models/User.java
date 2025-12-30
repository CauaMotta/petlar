package br.com.ocauamotta.PetLar.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Representa a entidade de um usuário no sistema PetLar.
 * <p>
 * Esta classe está mapeada para um documento na coleção "Users" do banco de dados MongoDB.
 * Além disso, implementa a interface
 * {@code UserDetails} do Spring Security, permitindo que esta classe seja usada
 * diretamente no processo de autenticação.
 */
@Getter
@Setter
@Builder
@Document(collection = "Users")
public class User implements UserDetails {

    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String name;

    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
