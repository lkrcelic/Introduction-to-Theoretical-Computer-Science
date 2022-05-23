import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MinDka {
	
	static boolean provjeriPodudarnost(String znak1, String znak2,Set<String> prihvatljivaStanja) {
		boolean prvi = prihvatljivaStanja.contains(znak1);
		boolean drugi = prihvatljivaStanja.contains(znak2);
	
		if(prvi && drugi) {
			return true; 
		}
		else if(!prvi && !drugi) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
    public static void main(String args[]) throws IOException {
    		 //new BufferedReader(new FileReader(new File("primjeri.txt")));
    		 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
    	     String readString;
		
            
           //Skup stanja odvojenih zarezom, leksikografski poredana
            String[] stanjaString = null;
            Set<String> stanja = new TreeSet<String>();
            readString = reader.readLine();
            stanjaString = readString.split(",");
        	Arrays.sort(stanjaString);
        	//iz stringa u set
            for(String s : stanjaString)
            	stanja.add(s);
            
            //Skup simbola abecede odvojenih zarezom, leksikografski poredana
            String[] alphabetString = null;
            Set<String> alphabet = new TreeSet<String>();
            readString = reader.readLine();
            alphabetString = readString.split(",");
            Arrays.sort(alphabetString);
            //iz stringa u set
            for(String s : alphabetString)
            	alphabet.add(s);
            
          // Skup prihvatljivih stanja odvojenih zarezom, leksikografski poredana.
            Set<String> prihvatljivaStanja = new TreeSet<String>();
            String[] prihvatljivaStanjaString;
            readString = reader.readLine();
            prihvatljivaStanjaString = readString.split(",");
            //iz stringa u set
            for(String s : prihvatljivaStanjaString)
            	prihvatljivaStanja.add(s);
            
            
            
          //Pocetno stanje
            String pocetnoStanje;
            pocetnoStanje = reader.readLine();
          
            
            //Ostale linije
            Map<Pair<String, String>, String> mapaPrijelaza = new HashMap<>();
            String[] pomArrray; //pomocno polje
            String pomString;
            readString = reader.readLine();
            while (readString != null) {
            	pomString = readString.replaceFirst("->", ",");
            	pomArrray = pomString.split(","); // odvojeni nizovi
                Pair<String, String> pair = Pair.of(pomArrray[0],pomArrray[1]); //stvaramo novi par
                
                if(!mapaPrijelaza.containsKey(pair)) {
                	String s = new String();
                	s = pomArrray[2];
                	mapaPrijelaza.put(pair, s);
                }
                readString = reader.readLine();
            }
            
            
//MINIMIZACIJA DKA
            
//UKLONI NEDOHVATLJIVA STANJA
            String trenutnoStanje;
            Set<String> nedohvatljivaStanja = new TreeSet<String>();
            Set<String> visited = new TreeSet<String>();
            LinkedList<String> queue = new LinkedList<String>();
     
            //mark the current node as visited and enqueue it
            visited.add(pocetnoStanje);
            queue.add(pocetnoStanje);
     
            while (queue.size() != 0)
            {
               //makni iz reda
            	trenutnoStanje = queue.poll();
                
            	//pogledaj sve definiranje prijelaze za trenutno stanje oni koji nisu obideni dodaj u queue
            	for(String ulazniZnak : alphabet) {
            		if(mapaPrijelaza.containsKey(Pair.of(trenutnoStanje, ulazniZnak))) {
            			//prodi kroz listu prijelaza za taj par i dodaj stanja koja nisu obidena u set i u queue
            			String prijelaz = mapaPrijelaza.get(Pair.of(trenutnoStanje, ulazniZnak));
        				if(!visited.contains(prijelaz)) {
        					visited.add(prijelaz);
        					queue.add(prijelaz); 
        				}
            		}
            	}
            }
            //stanja postaju samo ona koja su dohvatljiva
            stanjaString = visited.toArray(new String[0]);
            stanja.removeAll(visited);
            nedohvatljivaStanja.addAll(stanja);
            stanja.clear();
            stanja.addAll(visited);
            
//            System.out.println(nedohvatljivaStanja);
            
//NADI ISTOVJETNA STANJA
            Set<Pair<String, String>> paroviStanja = new HashSet<>();
            
            //Dodavanje podudarnih parova u parove stanja
            for(int i = 0; i < stanjaString.length; i++) {
            	 for(int j = i + 1; j < stanjaString.length; j++) {
            		 if(provjeriPodudarnost(stanjaString[i], stanjaString[j], prihvatljivaStanja))
            			 paroviStanja.add(Pair.of(stanjaString[i], stanjaString[j]));
            	 }
            }
            
            //Prolazak kroz listu parova i pronalazak istovijetnih parova
            String prijelaz1 = null;
            String prijelaz2 = null;
            String pomString2;
            int check;
            Set<Pair<String, String>> paroviStanjaCopy = new HashSet<>();
            paroviStanjaCopy.addAll(paroviStanja);
            boolean promjena = true;
            while(promjena) {
            	promjena = false;
	            for(Pair<String,String> par : paroviStanjaCopy) {// par je oblika stanje1, stanje2
	            	for(String znak : alphabet) {
	            		prijelaz1 = mapaPrijelaza.get(Pair.of(par.first, znak));
	            		prijelaz2 = mapaPrijelaza.get(Pair.of(par.second, znak));
	            		
	            		check = prijelaz1.compareTo(prijelaz2);
            		 	if(check > 0) { //prijelaz1 je veci to nije ok jer u paru mora manji biti prvi
            		 		pomString2 = prijelaz1;
							prijelaz1 = prijelaz2;
							prijelaz2 = pomString2;
            		 	}
            		 	else if(check == 0) {
            		 		continue;
            		 	}
	            		
	            		Pair<String,String> pomPair = Pair.of(prijelaz1, prijelaz2);
	            		if(!paroviStanja.contains(pomPair)) { //ako je novonastali par oznacen micemo i ovaj par
	            			if(paroviStanja.contains(par)) {// trebamo paziti da ne micemo u prazno jer nikad necemo izac iz while petlje
		            			paroviStanja.remove(par);
		            			promjena = true;
		            			break;
	            			}
	            		}
	            		else if(!provjeriPodudarnost(prijelaz1, prijelaz2, prihvatljivaStanja)) {//provjera da li imaju istu podudarnost
	            			if(paroviStanja.contains(par)) {// trebamo paziti da ne micemo u prazno jer nikad necemo izac iz while petlje
		            			paroviStanja.remove(par);
		            			promjena = true;
		            			break;
	            			}
	            		}
	            	}
	            }
            }
            
            
//IZBACIVANJE ISTOVIJETNIH STANJA
            //Izvlacenje stanja koja treba obrisati
            Set<String> istovijetnaStanja = new TreeSet<String>();
            for(Pair<String,String> par : paroviStanja) {
            	istovijetnaStanja.add(par.second);
            }
//            System.out.println(istovijetnaStanja);
            istovijetnaStanja.addAll(nedohvatljivaStanja); //DODAVANJE NEDOHVATLJIVIH STANJA U ISTOVIJETNA RADI LAKSEG CISCENJA
            
            stanja.removeAll(istovijetnaStanja);
            prihvatljivaStanja.removeAll(istovijetnaStanja);
            
            //Micanje svih prihvatljivih stanja
            	prihvatljivaStanja.removeAll(istovijetnaStanja);
            	
           //Provjera pocetnog stanja
            if(!stanja.contains(pocetnoStanje)) {
            	for(Pair<String,String> par : paroviStanja) {
            		if(par.second.equals(pocetnoStanje) && stanja.contains(par.first))
            			pocetnoStanje = par.first;
            	}
            }
            
            //Micanje svih prijelaza koji su viska
            for(String stanje : istovijetnaStanja) {
            	for(String znak : alphabet)
            		mapaPrijelaza.remove(Pair.of(stanje, znak));
            }
            
            //Preimenovanje prijelaza koji su ostali 
            for (Map.Entry<Pair<String, String>, String> entry : mapaPrijelaza.entrySet()) {
                if(!stanja.contains(entry.getValue())) {
                	for(Pair<String,String> par : paroviStanja) {
                		if(par.second.equals(entry.getValue()) && stanja.contains(par.first))
                			entry.setValue(par.first);
                	}
                }
            }

//ISPIS
            //Printaj stanja
            int i = 0;
            for(String s : stanja) {
            	if(i == 0) 
            		System.out.print(s);
            	else
            		System.out.print(","+s);
            	i++;
            }
            System.out.println();
            
            //Printaj abecedu
            i = 0;
            for(String s : alphabet) {
            	if(i == 0) 
            		System.out.print(s);
            	else
            		System.out.print(","+s);
            	i++;
            }
            System.out.println();
            
            //Printaj prihvatljiva stanja
            i = 0;
            for(String s : prihvatljivaStanja) {
            	if(i == 0) 
            		System.out.print(s);
            	else
            		System.out.print(","+s);
            	i++;
            }
            System.out.println();
            
            //Printaj pocetno stanje
            System.out.println(pocetnoStanje);
            
            //Printaj prijelaze
            for(String stanje : stanja) {
            	for(String znak : alphabet) 
            		if(mapaPrijelaza.containsKey(Pair.of(stanje, znak))) {
            			System.out.println(stanje + "," + znak + "->" + mapaPrijelaza.get(Pair.of(stanje, znak)));
            		}
            	
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

