package com.cubanengineer.test.sharefiles.models;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

  public Role() {

  }

  public Role(ERole pname) {
    this.name = pname;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer pid) {
    this.id = pid;
  }

  public ERole getName() {
    return name;
  }

  public void setName(ERole pname) {
    this.name = pname;
  }
}