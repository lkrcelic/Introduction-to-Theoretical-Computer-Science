import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class SimEnka{
    public static void main(String args[]) throws IOException {
		try {
    		String path;
    		 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	     String readString;
    		 String pom;
		
		
    		//Ucitavanje ulaznih nizova
    		String[] inputStrings = null;
    		readString = reader.readLine();
        	inputStrings = readString.split("[|]");
            
           //Skup stanja automata
            String[] stanja = null;
            readString = reader.readLine();
        	stanja = readString.split(",");
        	Arrays.sort(stanja);
            
            //Abeceda automata
            String[] alphabet = null;
            readString = reader.readLine();
            alphabet = readString.split(",");
            Arrays.sort(alphabet);
            
          //Prihvatljva stanja
            String[] prihvatljivaStanja = null;
            readString = reader.readLine();
            prihvatljivaStanja = readString.split(",");
            
          //Pocetno stanje
            String pocetnoStanje;
            pocetnoStanje = reader.readLine();
          
            
            //Ostale linije
            Map<Pair<String, String>, List<String>> mapaPrijelaza = new HashMap<Pair<String,String>, List<String>>();
            String[] pomArrray; //pomocno polje
            readString = reader.readLine();
            while (readString != null) {
            	List<String> pomLista = new ArrayList<>();
            	pom = readString.replaceFirst("->", ",");
            	pomArrray = pom.split(","); // odvojeni nizovi
                Pair<String, String> pair = Pair.of(pomArrray[0],pomArrray[1]); //stvaramo novi par
                for(int i = 2; i < pomArrray.length; i++) { //preskacemo prva dva jer su oni u pair-u
                	pomLista.add(pomArrray[i]);
                }
                mapaPrijelaza.put(pair, pomLista);
                readString = reader.readLine();
            }
            
            
            //Automat
            SortedSet<String> setTrenutnihStanja = new TreeSet<String>();
            String[] poljeStarihStanja;
            Pair<String,String> pomPair;		            
            String[] setTrenutnihStanjaClone;
            LinkedList<String> epsilonLista = new LinkedList<>();
            String stanje;
            
          //prolazim kroz ulazne nizove
            for(String inputString : inputStrings) { 
            	
            	setTrenutnihStanja.add(pocetnoStanje);
            	epsilonLista.add(pocetnoStanje);
            	
            	//nadi epsilon okruzenje
            	while(!epsilonLista.isEmpty()) {
		            stanje = epsilonLista.pollFirst();
		            pomPair = Pair.of(stanje, "$");
		            if(mapaPrijelaza.containsKey(pomPair)) {
		            	for(String s : mapaPrijelaza.get(pomPair)) {
		            		if(!setTrenutnihStanja.contains(s)) {
		            			setTrenutnihStanja.add(s);
		            			epsilonLista.add(s);
		            		}
		            	}
		            }
            	}
            	
            	//printaj pocetno stanje
            	for (String s : setTrenutnihStanja) {
            		
            		if(setTrenutnihStanja.first() == s)
            			System.out.print(s);
            		else
						System.out.print("," + s);
            	}
            	
            	//prolazimo kroz znakove ulaznog niza
            	for(String znak : inputString.split(",")) { 
	            	poljeStarihStanja = setTrenutnihStanja.toArray(new String[0]);
	            	setTrenutnihStanja.clear();
	            	
	            	//Za svako stanje i znak odrediti sljedeca stanja
	            	for(String staroStanje : poljeStarihStanja) {
	            		
	            		//provjera da li ima prijelaz starostanje,znak
	            		if(!znak.equals("$")) {
		            		pomPair = Pair.of(staroStanje,znak);
		            		if(mapaPrijelaza.containsKey(pomPair)) {
		            			setTrenutnihStanja.addAll(mapaPrijelaza.get(pomPair));
		            		}
		            		
		            		
			            	//Odredi sve epsilon prijelaze za trenutna stanja
		            		setTrenutnihStanjaClone = setTrenutnihStanja.toArray(new String[0]);
			            	for(String s1 : setTrenutnihStanjaClone) {
			            		epsilonLista.add(s1);
				            	while(!epsilonLista.isEmpty()) {
						            stanje = epsilonLista.pollFirst();
						            pomPair = Pair.of(stanje, "$");
						            if(mapaPrijelaza.containsKey(pomPair)) {
						            	for(String s2 : mapaPrijelaza.get(pomPair)) {
						            		if(!setTrenutnihStanja.contains(s2)) {
						            			setTrenutnihStanja.add(s2);
						            			epsilonLista.add(s2);
						            		}
						            	}
						            }
				            	}
			            	}
	            		}
		            	
	            	}
	            	//Dodaj # samo ako ako nema niceg
	            	if(setTrenutnihStanja.contains("#"))
	            		setTrenutnihStanja.remove("#");
	            	if(setTrenutnihStanja.size() == 0 )
	            		setTrenutnihStanja.add("#");
	            	
	            	//Printaj set
	            	System.out.print("|");
	            	for (String s : setTrenutnihStanja) {
	            		if(setTrenutnihStanja.first() == s)
	            			System.out.print(s);
	            		else
	            			System.out.print("," + s);
	            	}
            	}  
            	System.out.println();
            	setTrenutnihStanja.clear();
            }
		} catch (Exception e) {
			 System.out.println(e);
		}
    }
}
class Pair<U, V>
{
    public final U first;       // the first field of a pair
    public final V second;      // the second field of a pair
 
    // Constructs a new pair with specified values
    private Pair(U first, V second)
    {
        this.first = first;
        this.second = second;
    }
 
    @Override
    // Checks specified object is "equal to" the current object or not
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
 
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
 
        Pair<?, ?> pair = (Pair<?, ?>) o;
 
        // call `equals()` method of the underlying objects
        if (!first.equals(pair.first)) {
            return false;
        }
        return second.equals(pair.second);
    }
 
    @Override
    // Computes hash code for an object to support hash tables
    public int hashCode()
    {
        // use hash codes of the underlying objects
        return 31 * first.hashCode() + second.hashCode();
    }
 
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
 
    // Factory method for creating a typed Pair immutable instance
    public static <U, V> Pair <U, V> of(U a, V b)
    {
        // calls private constructor
        return new Pair<>(a, b);
    }

}

