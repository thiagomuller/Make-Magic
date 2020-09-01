package com.thiagomuller.hpapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="CHARACTERS")
public class Character {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="characterId", unique = true)
	@ApiModelProperty(notes="Character unique identifier, can't be duplicated. You don't need to send an Id at creation time.", example="1", required=true, position=0)
	private Integer characterId;
	
	@NotBlank(message="Name must not be empty")
	@NotNull(message="Name must not be null")
	@Pattern(regexp = "[a-zA-Z0-9- ]+", message="Name must not contain special characters or numbers")
	@Size(max=50, message="Name can contain up to 50 characters")
	@Column(unique=true)
	@ApiModelProperty(notes="Character name, can't be duplicated. Must not contain special characters", example="Harry Potter", required=true, position=1)
	private String name;

	@Pattern(regexp = "[a-zA-Z0-9- ]+", message="Name must not contain special characters or numbers, except for -")
	@ApiModelProperty(notes="Character role. Must not contain special characters", example="Student", required=false, position=2)
	private String role;
	
	@NotBlank(message="School must not be empty")
	@NotNull(message="School must not be null")
	@Size(max=100, message="Name can contain up to 100 characters")
	@ApiModelProperty(notes="Character school.", example="Hogwarts School of Witchcraft and Wizardry", required=true, position=3)
	private String school;
	
	@NotBlank(message="House id must not be empty")
	@NotNull(message="House id must not be null")
	@ApiModelProperty(notes="Character house id. Validated against Potter Api. It blocks characters insertion if this validations fails", example="5a05dc8cd45bd0a11bd5e071", required=true, position=4)
	private String houseId;
	
	@Pattern(regexp = "[a-zA-Z0-9- ]+", message="Name must not contain special characters or numbers")
	@Size(max=50, message="Name can contain up to 50 characters")
	@ApiModelProperty(notes="Character patronus.", example="Deer", required=false, position=5)
	private String patronus;
	
	@Pattern(regexp = "[a-zA-Z0-9- ]+", message="Name must not contain special characters or numbers")
	@Size(max=30, message="Name can contain up to 30 characters")
	@ApiModelProperty(notes="Character blood status.", example="Muggle-Born", required=false, position=5)
	private String bloodStatus;
	
}
