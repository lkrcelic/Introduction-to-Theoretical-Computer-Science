
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.io.InputStreamReader;


public class SimPa {	
	 public static void main(String args[]) throws IOException {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        String pomString;
		
		
			//Ucitavanje ulaznih nizova
			String[] inputStrings = null;
			pomString = reader.readLine();
	    	inputStrings = pomString.split("[|]");
	        
	       //Skup stanja automata
	        String[] stanja = null;
	        pomString = reader.readLine();
	    	stanja = pomString.split(",");
	    	Arrays.sort(stanja);
	        
	        //Abeceda automata
	        String[] alphabet = null;
	        pomString = reader.readLine();
	        alphabet = pomString.split(",");
	        Arrays.sort(alphabet);
	        
	      //znakovi stoga
	        String[] stogAlphabet = null;
	        pomString = reader.readLine();
	        stogAlphabet = pomString.split(",");
	        Arrays.sort(stogAlphabet);
	        
	      //Prihvatljva stanja
	        Set<String> prihvatljivaStanja = new HashSet<String>();
	        pomString = reader.readLine();
	        prihvatljivaStanja.addAll(Arrays.asList(pomString.split(",")));
	        
	      //Pocetno stanje
	        String pocetnoStanje = null;
	        pocetnoStanje = reader.readLine();
	        
	        String pocetniZnakStoga = null;
	        pocetniZnakStoga = reader.readLine();
	      
	        
	        //Ostale linije
	        Map<String, Pair<String, String>> mapaPrijelaza = new HashMap<>();
	        String tmpKey;
	        Pair<String, String> valuePair;
	        String[] pomArray; //pomocno polje
	        String[] pomArray2;
	        pomString = reader.readLine();
	        while (pomString != null) {
	        	pomArray = pomString.split("->"); // odvoji ulaz od izlaza
	        	tmpKey = pomArray[0].replaceAll(",", "");
	        	tmpKey = pomArray[0].replaceAll(",", "");
	        	pomArray2 = pomArray[1].split(",");
	        	valuePair = Pair.of(pomArray2[0],pomArray2[1]); //stvaramo novi par
	            mapaPrijelaza.put(tmpKey, valuePair);
	            pomString = reader.readLine();
	        }
	        
	        
//AUTOMAT
	        String trenutnoStanje;
	        StringBuffer stog = new StringBuffer();  
	        boolean failed;
	        for(String ulazniZnakoviString : inputStrings) {
	        	
	        	System.out.print(pocetnoStanje + "#" + pocetniZnakStoga + "|"); // printaj pocetno stanje automata
	        	stog.append(pocetniZnakStoga);
	        	trenutnoStanje = pocetnoStanje;
	        	failed = false;
	        			
	        	for(String ulazniZnak : ulazniZnakoviString.split(",")) {
	    
	        		tmpKey = makeTmpKey(trenutnoStanje, ulazniZnak, stog);
	        		
	        		if(mapaPrijelaza.containsKey(tmpKey)) {
	        			valuePair = mapaPrijelaza.get(tmpKey);
	        			odradiPrijelaz(trenutnoStanje, valuePair, stog);
	        			trenutnoStanje = valuePair.first;
	        		}
	        		else {
	        			failed = true;
	        			tmpKey = makeTmpKey(trenutnoStanje, "$", stog);
	        			while(mapaPrijelaza.containsKey(tmpKey)) {
	        				valuePair = mapaPrijelaza.get(tmpKey);
	        				odradiPrijelaz(trenutnoStanje, valuePair, stog);
	        				trenutnoStanje = valuePair.first;
	        				//provjeri da li sad ima prijelaz za ulazni znak
	        				tmpKey = makeTmpKey(trenutnoStanje, ulazniZnak, stog);
	        				if(mapaPrijelaza.containsKey(tmpKey)) {
	        					failed = false;
	        					valuePair = mapaPrijelaza.get(tmpKey);
		        				odradiPrijelaz(trenutnoStanje, valuePair, stog);
		        				trenutnoStanje = valuePair.first;
	        					break;
	        				}
	        				tmpKey = makeTmpKey(trenutnoStanje, "$", stog);
	        			}
	        			if(failed) {
	        				System.out.print("fail|");
	        				break;
	        			}
	        		}   		
	        	}
//	        	System.out.print("  " + trenutnoStanje + "  ");
	        	if(prihvatljivaStanja.contains(trenutnoStanje) && !failed) {
	        		System.out.println("1");
	        	} else if(failed) {
	        		failed = false;
	        		System.out.println("0");
	        	}
	        	else { //ako postoje epsilon prijealzi
	        		tmpKey = makeTmpKey(trenutnoStanje, "$", stog);
	        		while(mapaPrijelaza.containsKey(tmpKey) && !prihvatljivaStanja.contains(trenutnoStanje)) {
	        			valuePair = mapaPrijelaza.get(tmpKey);
	        			odradiPrijelaz(trenutnoStanje, valuePair, stog);
	        			trenutnoStanje = valuePair.first;
		        		tmpKey = makeTmpKey(trenutnoStanje, "$", stog);
	        		}
	        		
	        		if(prihvatljivaStanja.contains(trenutnoStanje)) {
	        			System.out.println("1");
	        		}
	        		else {
	        			System.out.println("0");
	        		}
	        	}
	        	
	        	stog.delete(0, stog.length());
	        }   
	 }

	public static String makeTmpKey(String trenutnoStanje, String ualzniZnak, StringBuffer stog) {
		if(stog.length() == 0) {
			return trenutnoStanje + ualzniZnak + "$";
		}
		else {
			return trenutnoStanje + ualzniZnak + stog.charAt(0);
		}
	}
	
	//promjeni stog te isprinta prijelaz
	public static void odradiPrijelaz(String trenutnoStanje, Pair<String, String> valuePair, StringBuffer stog) {
		stog.deleteCharAt(0);
		if(!valuePair.second.equals("$")) { //ako sljedeci znak stoga nije prazan niz dodaj niz
			stog.insert(0, valuePair.second);
		}
		trenutnoStanje = valuePair.first;
		
		//printaj stanje 
		if(stog.length() == 0) 
			System.out.print(trenutnoStanje + "#" + "$" + "|"); 
		else
			System.out.print(trenutnoStanje + "#" + stog.toString() + "|"); 
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
