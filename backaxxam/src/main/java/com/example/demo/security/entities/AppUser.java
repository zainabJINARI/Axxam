package com.example.demo.security.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import lombok.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AppUser {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	
	 @Column(unique = true)
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String email;
	private String fullname;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Collection<AppRole> appRoles= new ArrayList<>();
	
	@Column(name = "created_at", nullable = false)
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = new Date(); // Set to current date/time before persisting
        }
    }


}
