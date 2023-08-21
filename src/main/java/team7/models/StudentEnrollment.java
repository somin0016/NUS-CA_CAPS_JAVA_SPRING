package team7.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
public class StudentEnrollment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	@JsonIgnore
	private Course course;
	
	@ManyToOne
	@JsonIgnore
	private Student student;
	
	/***
	    0 = PENDING
	    1 = ACCEPTED
	    2 = REJECTED
	    3 = CANCELLED
	 ***/
	@Column(columnDefinition = "TINYINT NOT NULL")
	private Integer enrollmentStatus = 0;
	
	@Column(columnDefinition = "DATE NOT NULL")
	private LocalDate enrollmentDate;
	
	@Transient
	private String studentGrade;
	
	@Transient
	private Integer studentCredits;
}
