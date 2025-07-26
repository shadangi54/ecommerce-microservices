package com.shadangi54.auth.manager;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadangi54.auth.entity.User;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails{

	private static final long serialVersionUID = 6053428449557140233L;
	
	private Long id;
	private String userName;
	private String email;
	
	@JsonIgnore
	private String password;
	
	
	public UserDetailsImpl(Long id, String userName, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	 public static UserDetailsImpl build(User user) {
	        List<GrantedAuthority> authorities = user.getRoles().stream()
	                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
	                .collect(Collectors.toList());

	        return new UserDetailsImpl(
	                user.getId(),
	                user.getUserName(),
	                user.getEmail(),
	                user.getPassword(),
	                authorities);
	    }
	
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}
	
	public String getEmail() {
		return email;
	}
	
	 @Override
	    public boolean equals(Object o) {
	        if (this == o)
	            return true;
	        if (o == null || getClass() != o.getClass())
	            return false;
	        UserDetailsImpl user = (UserDetailsImpl) o;
	        return Objects.equals(id, user.id);
	    }

	 public Long getId() {
		return id;
	 }

}
