package spamFilter;
import java.util.*;
import java.io.*;
public class SpamFilter {
	  static ArrayList<String> category = new ArrayList<String>();
	  static int counter = 0;
	  public static String spamCat(String text) throws IOException
	  {
		    if(counter == 0)
		    {
		    	populateCategory();
		    	counter = 1;
		    }
			LinkedHashMap<String, ArrayList<Double>> fileNames = new LinkedHashMap<String, ArrayList<Double>>(); //at zero -- Number of lines
			//at position 1 - probability
			//at position 2 - number of unique words
			HashMap<String, ArrayList<Integer>> words = new HashMap<String, ArrayList<Integer>>();
			int totalLines = 0;
			File p1 = new File("Training Data");
			File[] listOfFiles = (p1).listFiles();
			int pos = 0;
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			        if(file.getName().endsWith("~"))
			        	file.delete();
			        else
			        {
			        	fileNames.put(file.getName(), new ArrayList<Double>());
			        	fileNames.get(file.getName()).add(0.0);
			        	words.forEach((key, value) -> value.add(0));
			        	File p2 = new File("Training Data/"+file.getName());
			        	BufferedReader in = new BufferedReader(new FileReader(p2));
			        	String currentLine;
			        	while((currentLine = in.readLine()) != null)
			        	{
			        		totalLines++;
			        		fileNames.get(file.getName()).set(0, fileNames.get(file.getName()).get(0)+1);
			        		currentLine = currentLine.toLowerCase();
			        		StringTokenizer parser = new StringTokenizer(currentLine, " \t\n\r\f.,;:!?'=()\"@#$%^&*");
			        		while(parser.hasMoreTokens()) {
			        				String currentWord = parser.nextToken();
			        				if(!words.containsKey(currentWord))
			        				{
			        					words.put(currentWord, new ArrayList<Integer>());
			        					for(int i = 0; i <= pos; i++)
			        					{
			        						words.get(currentWord).add(0);
			        					}
			        				}
			        				words.get(currentWord).set(pos, words.get(currentWord).get(pos)+1);
			        		}
			        	}
			        }
			    }
			    pos++;
			}
			for(String s : category) //Computing Probability of each class
			{
				fileNames.get(s).add((double)fileNames.get(s).get(0)/(double)(totalLines));
			}		
			//System.out.println(fileNames.toString());
			boolean res=true;
				String str=text;
				str = str.toLowerCase();
				StringTokenizer wrd = new StringTokenizer(str, " \t\n\r\f.,;:!?'=()\"@#$%^&*");
				int strLen = wrd.countTokens();
				fileNames.forEach((key, value) -> value.add(0.0));
				for(String key:words.keySet())
				{
					ArrayList<Integer> occur = words.get(key);
					int i = 0;
					for(i = 0; i < occur.size(); i++)
					{
						if(occur.get(i) != 0)
							fileNames.get(category.get(i)).set(2, fileNames.get(category.get(i)).get(2) + 1);
					}
				}
				ArrayList<Double> prob = new ArrayList<Double>();
				for(String s : category)
					prob.add(fileNames.get(s).get(1));
				//System.out.println("Prob : " + prob.toString());
				while(wrd.hasMoreTokens()) 
				{
					String w = wrd.nextToken();
					if(words.containsKey(w))
					{	
						ArrayList<Integer> freq = words.get(w);
						int i = 0;
						for(String s : category)
						{
							double p = (1 + freq.get(i))/(fileNames.get(s).get(2)+words.size()+strLen);
							prob.set(i, prob.get(i)*p*1000000);
							i++;
							//System.out.println(s + " IN " + prob.toString());
						}
							
					}	
					else
					{
						int i = 0;
						for(String s : category)
						{
							double p = (1)/(fileNames.get(s).get(2)+words.size()+strLen);
							prob.set(i, prob.get(i)*p*1000000);
							i++;
							//System.out.println(s + " OUT " + prob.toString());
						}
					}
				}
				return category.get(maxPos(prob));
		}
		public static int maxPos(ArrayList<Double> a)
		{
			int pos = 0;
			double max = a.get(0);
			for(double n : a)
				if(n > max)
				{
					pos = a.indexOf(n);
					max = n;
				}
			return pos;
		}
		public static void populateCategory()
		{
			File p1 = new File("Training Data");
			File[] listOfFiles = (p1).listFiles();
			int pos = 0;
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			        if(file.getName().endsWith("~"))
			        	file.delete();
			        else
			        {
			        	category.add(file.getName());
			        }
			    }
			}
		}
		public static String read_file(String s)
	 	{
			String str="";
			File file = new File(s);
			FileInputStream fis = null;

			try 
			{
				fis = new FileInputStream(file);
				int content;
				while ((content = fis.read()) != -1) 
				{
					str+=((char)content);
				}

			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			finally
			{
				try 
				{
					if (fis != null)
						fis.close();
				} 
				catch (IOException ex) 
				{
					ex.printStackTrace();
				}
			}
			return str;
		}
		public static void append_text(String f,String s)
	    {
	        try
	        {
	            FileWriter fstream = new FileWriter("Training Data/"+f,true);
	            BufferedWriter fbw = new BufferedWriter(fstream);
	            fbw.write(s);
	            fbw.newLine();
	            fbw.close();
	        }
	        catch (Exception e) 
	        {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
}