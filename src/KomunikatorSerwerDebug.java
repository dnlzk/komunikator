import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import pl.nalazek.komunikator.logika.Pakiet;
import pl.nalazek.komunikator.logika.RozmowaID;
import pl.nalazek.komunikator.logika.Wiadomosc;




public class KomunikatorSerwerDebug {	
	
		public static void main(String[] a) 
		{
			ObjectOutputStream strumW = null;
			ObjectInputStream strumP = null;
			Socket gniazdko = null;
			try {
				URLConnection connection = new URL("http://localhost:8080/a/Obsluga").openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestProperty("Content-Type", "application/octet-stream");
				 strumW = new ObjectOutputStream(connection.getOutputStream());
				 				for(int i = 0; i < 3; i++)
				{strumW.writeObject(new Wiadomosc("dgdgv",new RozmowaID()));
				strumW.flush();
				strumW.reset();
				
				strumP = new ObjectInputStream(connection.getInputStream());
				Pakiet pakiet = (Pakiet)strumP.readObject();
				if(pakiet.getClass() == Pakiet.class)
					System.out.println("Przyjêto odpowiedŸ");}
				 				strumW.close();
								strumP.close();
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				try{
				if(strumW != null) strumW.close();
				if(strumP != null) strumP.close();
				if(gniazdko != null) gniazdko.close();
				}
				catch(Exception e) { }
			}
			
		}
}
