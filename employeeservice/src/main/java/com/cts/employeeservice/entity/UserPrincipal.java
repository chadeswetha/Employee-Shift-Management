//package com.cts.employeeservice.entity;
//
//import java.util.Collection;
//
//
//import java.util.Collections;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//public class UserPrincipal implements UserDetails{
//	
//	private Employee user;
//
//	public UserPrincipal(Employee user) {
//		this.user = user;
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
//	}
//
//	@Override
//	public String getPassword() {
//		return user.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return user.getEmail();
//	}
//	
//	
//	// Add the getEmployee() method
//		public Employee getEmployee() {
//			return user;
//		}
//
//}
package com.cts.employeeservice.entity;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private Employee user;

    public UserPrincipal(Employee user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Add the "ROLE_" prefix here
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Add the getEmployee() method
    public Employee getEmployee() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Or base it on your user's status
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or base it on your user's status
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or base it on your password expiration policy
    }

    @Override
    public boolean isEnabled() {
        return true; // Or base it on your user's status
    }
}
