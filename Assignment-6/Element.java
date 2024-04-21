// ELEMENTO DEL LOG

public class Element implements Comparable<Element>
{
	public final int id; // numero della riga
	public final String line; // contenuto della riga

	public Element(int id, String line)
	{
		this.id = id;
		this.line =line;
	}

	// confronta l'elemento corrente con il parametro
	@Override
	public int compareTo(Element o)
	{
		return Integer.compare(this.id, o.id);
	}
}