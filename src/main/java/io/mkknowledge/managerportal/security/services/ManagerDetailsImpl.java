package io.mkknowledge.managerportal.security.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.mkknowledge.managerportal.models.Manager;

public class ManagerDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	@JsonIgnore
	private String password;
	
	private String address;
	
	private Date dob;
	
	private String company;
	
	private Date created_At;
	
	private Date updated_At;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public ManagerDetailsImpl(Long id, String email, String firstName, String lastName, String password, Date dob,
			String address, String company, Date created_At, Date updated_At,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.address = address;
		this.dob = dob;
		this.company = company;
		this.created_At = created_At;
		this.updated_At = updated_At;
		this.authorities = authorities;
	}
	
	public static ManagerDetailsImpl build(Manager manager) {
		
		List<GrantedAuthority> authorities = manager.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());
		
		return new ManagerDetailsImpl(
				manager.getId(),
				manager.getEmail(),
				manager.getFirstName(),
				manager.getLastName(),
				manager.getPassword(),
				manager.getDob(),
				manager.getAddress(),
				manager.getCompany(),
				manager.getCreated_At(),
				manager.getUpdated_At(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getCreated_At() {
		return created_At;
	}

	public void setCreated_At(Date created_At) {
		this.created_At = created_At;
	}

	public Date getUpdated_At() {
		return updated_At;
	}

	public void setUpdated_At(Date updated_At) {
		this.updated_At = updated_At;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManagerDetailsImpl other = (ManagerDetailsImpl) obj;
		return Objects.equals(id, other.id);
	}
	
}
