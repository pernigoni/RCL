import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class FailSafeIterator
{
	public static void main(String[] args)
	{
		ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();
		map.put("ONE", 1);
		map.put("TWO", 2);
		map.put("THREE", 3);
		map.put("FOUR", 4);

		Iterator <String> it = map.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String) it.next();
			System.out.println(key + ": " + map.get(key));
			// NOTA: non ha creato una copia separata
			// stamperà anche 7
			map.put("SEVEN", 7);
		}
	}
}