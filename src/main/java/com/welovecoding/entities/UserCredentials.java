package com.welovecoding.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Table(
        uniqueConstraints
        = @UniqueConstraint(columnNames = {"CRED_TYPE", "USER_ID"})
)
@Entity
@DiscriminatorColumn(name = "CRED_TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class UserCredentials implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String token;

  @ManyToOne(cascade = CascadeType.ALL)
  @NotNull
  private User user;

  @Column(name = "CRED_TYPE", insertable = false, updatable = false)
  private String credType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

}
