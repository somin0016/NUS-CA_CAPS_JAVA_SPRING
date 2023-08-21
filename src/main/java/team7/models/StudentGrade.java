package team7.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "student_id"})})
public class StudentGrade {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JsonIgnore
	private Course course;
	
	@ManyToOne
	@JsonIgnore
	private Student student;
	
	@Column(columnDefinition = "VARCHAR(1) NOT NULL")
	private String studentGrade;
	
	@Column(columnDefinition = "TINYINT NOT NULL")
	private Integer studentCredits;
}