package com.nouros.hrms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "User")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer userid;

  @Column(name = "FIRST_NAME", length = 50)
  private String firstName;

  @Column(name = "MIDDLE_NAME", length = 50)
  private String middleName;

  @Column(name = "LAST_NAME", length = 50)
  private String lastName;
  
  @Column(name = "FULL_NAME", length = 100)
  private String fullName;

  @Column(name = "USERNAME", nullable = false, length = 100)
  private String userName;
    
  public User(Integer customerUserId) {
    super();
    this.userid = customerUserId;
  }

@Override
public String toString() {
	return "User [userid=" + userid + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
			+ lastName + ", fullName=" + fullName + ", userName=" + userName + "]";
}

public User(com.enttribe.usermanagement.user.model.User umUser) {
  this.userid = umUser.getUserid();
  this.firstName = umUser.getFirstName();
  this.lastName = umUser.getLastName();
  this.userName = umUser.getUserName();
}
  
  

}
