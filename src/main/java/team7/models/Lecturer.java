package team7.models;

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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "Lecturer")
public class Lecturer extends Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "lecturer_code",
			columnDefinition = "NVARCHAR(7) NOT NULL")
	@NotBlank(message = "Lecturer Code must not be empty")
	@Size(min = 7, max = 7, message = "Lecturer Code must be 7 characters long")
	private String code;
	
	@Column(columnDefinition = "TINYINT NOT NULL")
	@NotNull(message = "Please select Lecturer Title")
	private Integer title;
	
	@Column(columnDefinition = "NVARCHAR(40) NOT NULL")
	@NotBlank(message = "Lecturer FirstName must not be empty")
	@Size(max = 40, message = "Lecturer FirstName must be maximum 40 characters")
	private String firstName;
	
	@Column(columnDefinition = "NVARCHAR(40) NOT NULL")
	@NotBlank(message = "Lecturer LastName must not be empty")
	@Size(max = 40, message = "Lecturer LastName must be maximum 40 characters")
	private String lastName;
	
	@Column(columnDefinition = "NVARCHAR(15)")
	@NotBlank(message = "Lecturer Designation must not be empty")
	@Size(max = 15, message = "Lecturer Designation must be maximum 15 characters")
	private String designation;
	
	@Column(columnDefinition = "VARCHAR(15)")
	@NotBlank(message = "Lecturer Phone Number must not be empty")
	@Size(max = 15, message = "Lecturer Phone Number must be maximum 15 characters")
	private String phone;
	
	@OneToMany(mappedBy = "lecturer")
	@JsonIgnore
	private List<Course> courses;
	
	@OneToMany(mappedBy = "lecturer")
	@JsonIgnore
	private List<UpdateRequest> updateRequests;
}
