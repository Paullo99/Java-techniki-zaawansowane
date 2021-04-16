package pl.md5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileChecker {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		
		HashMap<String, String> snapshotHashMap = new HashMap<String, String>();
		HashMap<String, String> currentHashMap = new HashMap<String, String>();
		
		try {
			snapshotHashMap = generateMd5HashMap("D:/test");
			writeToFile(snapshotHashMap, "test");
		} catch (IOException e) {
			System.out.println("err");
		}
		
		compare(snapshotHashMap, currentHashMap);
	}
	
	/*
	 * Tworzy skr�t MD5 dla ka�dego pliku dla danej �cie�ki bezwzgl�dnej do katalogu
	 * Zwraca hashmap�: klucz = nazwa pliku, warto�� = skr�t md5
	 */
	public static HashMap<String, String> generateMd5HashMap(String absoluteDirectoryPath) throws NoSuchAlgorithmException, IOException{
		
		HashMap<String, String> md5HashMap = new HashMap<>();

		MessageDigest md = MessageDigest.getInstance("MD5");

		Set<String> set = createSetOfFiles(absoluteDirectoryPath);
		
		for (String fileName : set) {
			InputStream is = Files.newInputStream(Paths.get(absoluteDirectoryPath + "/" + fileName));
			byte[] digest = md.digest(is.readAllBytes());
			String md5Hex = convertByteToHex(digest);
			md5HashMap.put(fileName, md5Hex);
		}
		
		return md5HashMap;		
	}

	/*
	 * Zwraca zbi�r wszystkich plik�w (a dok�adniej ich nazwy) znajduj�cych si� w danym katalogu
	 */
	public static Set<String> createSetOfFiles(String dir) throws IOException {
	    Set<String> fileNameSet=Files.list(Paths.get(dir))
	          .filter(Files::isRegularFile)
	          .map(Path::getFileName)
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    
	    return fileNameSet;
	}
	
	/*
	 * Zwraca skr�t MD5 w postaci heksadecymalnej
	 */
	public static String convertByteToHex(byte[] digest) {
		
		StringBuilder sBuilder = new StringBuilder();

		for (int i = 0; i < digest.length; i++) {
			//operacja logicznego ANDa w celu otrzymania dw�ch liczb heksadecymalnych z 1 bajtu
			String hexString = Integer.toHexString(0xFF & digest[i]);

			sBuilder.append(hexString);
		}
		
		return sBuilder.toString();
	}
	
	/*
	 * Zapisuje hashmap� zawieraj�c� nazw� pliku+rozszerzenie (klucz) oraz md5 (warto��) do pliku
	 * Nazwa pliku to nazwa katalogu z rozszerzeniem .txt
	 */
	public static void writeToFile(HashMap<String, String> map, String directoryName) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(directoryName + ".txt"));
		
		//entry zawiera pary klucz-warto��
		for(Map.Entry<String, String> entry : map.entrySet()){
			//klucz i warto�� rozdzielone s� znakiem '='
			bw.write(entry.getKey() + "=" + entry.getValue());
			bw.newLine();
		}
		
		bw.close();
	}

	/*
	 * Odczytuje zapisan� w pliku .txt hashmap� dla danego katalogu na podstawie jego nazwy
	 */
	public static HashMap<String, String> readFromFile(String directoryName) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(directoryName + ".txt"));
		HashMap<String, String> map = new HashMap<String, String>();
		String line;
		
		while((line = br.readLine())!= null){
			String[] keyAndValue = line.split("=");	
			map.put(keyAndValue[0], keyAndValue[1]);
		}
		
		br.close();
		return map;
	}

	/*
	 * Por�wnuje 2 hashmapy
	 * Zwraca hashmap�: klucz - nazwa pliku, warto�� - true/false
	 * true - je�li by�a modyfikacja, false - brak modyfikacji
	 */
	public static HashMap<String, Boolean> compare(HashMap<String, String> savedMd5HashMap, HashMap<String, String> currentMd5HashMap) {
		HashMap<String, Boolean> wasFileChangedHashMap = new HashMap<>(); 
		
		
        for (Map.Entry<String, String> entry : savedMd5HashMap.entrySet()) {
            String savedMD5 = entry.getValue();
            String currentMD5 = currentMd5HashMap.get(entry.getKey());
            
            if (savedMD5.equals(currentMD5)) 
            	wasFileChangedHashMap.put(entry.getKey(), false);
            else 
            	wasFileChangedHashMap.put(entry.getKey(), true);
        }
        
        return wasFileChangedHashMap;
    }
}
