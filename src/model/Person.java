package model;

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
	public String getName() {
		return this.name;
	}
	public void setName(String name){
		this.name =name;		
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
