package com.cubanengineer.test.sharefiles.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", 
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User() {
  }

  public User(String pusername, String pemail, String ppassword) {
    this.username = pusername;
    this.email = pemail;
    this.password = ppassword;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long pid) {
    this.id = pid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String pusername) {
    this.username = pusername;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String pemail) {
    this.email = pemail;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String ppassword) {
    this.password = ppassword;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> proles) {
    this.roles = proles;
  }
}
