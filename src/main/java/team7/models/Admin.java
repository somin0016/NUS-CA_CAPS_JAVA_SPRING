package team7.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Admin")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "NVARCHAR(20) UNIQUE NOT NULL")
	private String username;
	
	@Column(columnDefinition = "NVARCHAR(72) NOT NULL")
	private String password;
	
	@Column(columnDefinition = "NVARCHAR(40) NOT NULL")
	private String firstName;
	
	@Column(columnDefinition = "NVARCHAR(40) NOT NULL")
	private String lastName;
	
	@Column(columnDefinition = "NVARCHAR(30) NOT NULL")
	private String email;
}
