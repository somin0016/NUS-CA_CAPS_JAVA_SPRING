package team7.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Student")
public class Student extends Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "NVARCHAR(15) UNIQUE NOT NULL")
	@NotBlank(message = "Student Matriculation Number must not be empty")
	@Size(max = 15, message = "Student Matriculation Number must be maximum 15 characters")
	private String matricNumber;
	
	@Column(columnDefinition = "NVARCHAR(40) NOT NULL")
	@NotBlank(message = "Student FirstName must not be empty")
	@Size(max = 40, message = "Student FirstName must be maximum 40 characters")
	private String firstName;
	
	@Column(columnDefinition = "NVARCHAR(40) NOT NULL")
	@NotBlank(message = "Student LastName must not be empty")
	@Size(max = 40, message = "Student LastName must be maximum 40 characters")
	private String lastName;
	
	@Column(columnDefinition = "VARCHAR(1) NOT NULL")
	@NotNull(message = "Please select Student Gender")
	private String gender;
	
	@Column(columnDefinition = "DATE NOT NULL")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Student DOB must not be empty")
	@Past(message = "Student DOB must be in the past")
	private LocalDate dob;
	
	@Column(columnDefinition = "VARCHAR(20)")
	@NotBlank(message = "Student Phone Number must not be empty")
	@Size(max = 20, message = "Student Phone Number must be maximum 20 characters")
	private String phone;
	
	@OneToMany(targetEntity = StudentEnrollment.class, mappedBy = "student")
	private List<StudentEnrollment> studentEnrollment;
	
	@OneToMany(targetEntity = StudentGrade.class, mappedBy = "student")
	private List<StudentGrade> studentGrade;
}
