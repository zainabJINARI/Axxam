package com.example.demo.models;

import java.sql.Date;

import com.example.demo.entities.Reaction;
import com.example.demo.enums.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CustomerModel {
	private Long id;
	private String fullName;
	private String email;

}
