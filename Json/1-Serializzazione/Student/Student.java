import com.google.gson.*;
import java.util.*;

enum Degree_Type {TRIENNALE, MAGISTRALE}

public class Student
{
	private String firstName;
	private String lastName;
	private int studentID;
	private String email;
	private List<String> courses;
	private Degree_Type Dg;

	public Student(String FName, String LName, int SID, String email, List<String> Clist, Degree_Type DG)
	{
		this.firstName = FName;
		this.lastName = LName;
		this.studentID = SID;
		this.email = email;
		this.courses = Clist;
		this.Dg = DG;
	}

	public String toString()
	{
		return "name:" + firstName + " surname:" + lastName + " ID:" + studentID + " email:" + email + " corsi:" + courses + " Degree:" + Dg;
	}

	// ... metodi getter e setter ...

	public static void main(String[] args)
	{
		List<String> ComputerScienceCourses = Arrays.asList("Reti", "Architetture");
		List<String> MathCourses = Arrays.asList("Analisi", "Statistica");

		Student mario = new Student("Mario", "Rossi", 1254, "mario.rossi@uni1.it", ComputerScienceCourses, Degree_Type.TRIENNALE);
		Student anna = new Student("Anna", "Bianchi", 1328, "anna.bianchi@uni1.it", MathCourses, Degree_Type.MAGISTRALE);

		// istanzio oggetto Gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// converto da Java a JSON
		String marioJson = gson.toJson(mario);
		String annaJson = gson.toJson(anna);
		System.out.println(marioJson);
		System.out.println(annaJson);
	}
}