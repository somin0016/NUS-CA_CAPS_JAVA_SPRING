package team7.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "NVARCHAR(50) UNIQUE NOT NULL")
	@NotBlank(message = "Course Name must not be empty")
	@Size(min = 2, max = 50, message = "Course Name must be 2-50 characters long")
	private String courseName;
	
	@Column(columnDefinition = "NVARCHAR(255)")
	@Size(max = 255, message = "Maximum 255 characters")
	private String description;
	
	@Column(columnDefinition = "SMALLINT NOT NULL")
	@NotNull(message = "Course Credits must not be empty")
	@Min(value = 5, message = "Minimum Course Credits: 5")
	@Max(value = 100, message = "Maximum Course Credits: 100")
	private Integer courseCredits;
	
	@Column(columnDefinition = "SMALLINT NOT NULL")
	@NotNull(message = "Course Capacity must not be empty")
	@Min(value = 5, message = "Minimum Course Capacity: 5")
	@Max(value = 100, message = "Maximum Course Capacity: 100")
	private Integer courseCapacity;
	
	@Column(columnDefinition = "SMALLINT NOT NULL")
	@NotNull(message = "Course Fee must not be empty")
	@Min(value = 50, message = "Minimum Course Fees: 500")
	private Integer courseFee;
	
	@Column(columnDefinition = "DATE NOT NULL")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Course Start Date must not be empty")
	@Future(message = "Course Start Date must be in the future")
	private LocalDate courseStartDate;
	
	@Column(columnDefinition = "SMALLINT NOT NULL")
	@NotNull(message = "Course Duration must not be empty")
	@Min(value = 0, message = "Minimum Course Duration: 0")
	private Integer courseDuration;
	
	/***
	    1 = AVAILABLE
	    2 = LOW_VACANCY
	    3 = FULL
	    4 = CANCELLED  
    ***/
	@Column(columnDefinition = "TINYINT NOT NULL")
	private Integer courseStatus = 1;
	
	@OneToMany(targetEntity = StudentEnrollment.class, mappedBy = "course")
	@JsonIgnore
	private List<StudentEnrollment> enrollment;
	
	@OneToMany(targetEntity = StudentGrade.class, mappedBy="course")
	@JsonIgnore
	private List<StudentGrade> courseStudentGrade;
	
	@OneToMany(targetEntity = UpdateRequest.class, mappedBy="course")
	@JsonIgnore
	private List<UpdateRequest> updateRequest;
	
	@ManyToOne
	@JsonIgnore
	private Lecturer lecturer;
	
	@Transient
	private LocalDate courseEndDate;
	
	@Transient
	private Long courseVacancy;
	
	@Transient
	private LocalDate enrollmentStartDate;
	
	@Transient
	private LocalDate enrollmentEndDate;
}
