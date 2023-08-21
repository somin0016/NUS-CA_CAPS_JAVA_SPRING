package team7.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JsonIgnore
	private Lecturer lecturer;
	
	@ManyToOne
	@JsonIgnore
	private Course course;
	
	@Column(columnDefinition = "VARCHAR(255) NOT NULL")
	private String reason;

	/***
	 0 = PENDING
	 1 = ACCEPT
	 2 = REJECT 
	***/
	@Column(columnDefinition = "TINYINT")
	private Integer status = 0;
}
