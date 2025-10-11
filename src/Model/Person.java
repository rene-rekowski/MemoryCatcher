package Model;

/**
 * represent a person that the User know
 */
public class Person {
	private String name;
	private String description;
	
	public Person(String name) {
		this.name = name;
		
	}
	public Person(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
