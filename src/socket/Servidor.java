package socket;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor {
	 public static void main(String[] args) throws IOException {
	     // inicia o servidor
	     new Servidor(12345).executa();
	   }
	   
	   private int porta;
	   private ArrayList<PrintStream> clientes = new ArrayList<PrintStream>();
	   
	   public Servidor (int porta) {
	     this.porta = porta;
	     this.clientes = new ArrayList<PrintStream>();
	   }
	   
	   public void executa () throws IOException {
	     ServerSocket servidor = new ServerSocket(this.porta);
	     System.out.println("Porta 12345 aberta!");
	     
	     while (true) {
	       // aceita um cliente
	       Socket cliente = servidor.accept();
	       System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
	       
	       // adiciona saida do cliente à lista
	       PrintStream ps = new PrintStream(cliente.getOutputStream());
	       this.clientes.add(ps);
	       
	       // cria tratador de cliente numa nova thread
	       TrataCliente tc = new TrataCliente(cliente.getInputStream(), this);
	       new Thread(tc).start();
	     }
	 
	   }
	 
	   public void distribuiMensagem(String msg, Scanner s) {
	     // envia msg para todo mundo
		 if(msg.equals("answer me")){
			int a,b;
			
			for (PrintStream cliente : this.clientes) {
				cliente.println("Enter a first value:");
				a = s.nextInt();
				cliente.println("Enter a second value:");
				b = s.nextInt();
				cliente.println("Answer = "+(a+b));
			}
		 }
		 
		 if(msg.equals("time")){
			 ZoneId fusoHorarioDeSaoPaulo = ZoneId.of("America/Sao_Paulo");
			 ZonedDateTime hour = ZonedDateTime.now(fusoHorarioDeSaoPaulo);
			 
			 for (PrintStream cliente : this.clientes){
		       cliente.println( hour);    	 
		     }
		 }
		 
		 if(msg.equals("hello")){
			 for (PrintStream cliente : this.clientes){
			       cliente.println("Hi!");    	 
			     }
		 }
		 
		 if(msg.equals("are you, ok?")){
			 for (PrintStream cliente : this.clientes){
			       cliente.println("I'm fine");    	 
			     }
		 }
		 
		 if(msg.equals("what are you doing?")){
			 for (PrintStream cliente : this.clientes){
			       cliente.println("What? I'm answering you.");    	 
			     }
		 }
		 
		 if(msg.equals("get me coffee")){
			 for (PrintStream cliente : this.clientes){
			       cliente.println("sudo apt-get install coffee.");
			       for (int i = 0; i < 5; i++) {
			    	   cliente.println(".");
			       }
			       cliente.println("Done");
			       cliente.println("Have an excellent day :)");
			     }
		 }
	 }
}
