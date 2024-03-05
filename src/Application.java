package src;
import java.io.File;
import java.util.Scanner;


/**
 * Projet d'Algorithmique et Structures de Données 3
 * @author HIGNARD Katel et DOEUVRE Guillaume - Groupe 584J
 *
 * Application.java
 * Implémentation des fonctionnalités permettant la compression d'un fichier.
 */
 
public class Application{

	private static Scanner scan = new Scanner(System.in);

	/**
	 * choosePGM
	 * Permet de choisir une image PGM à charger
	 * @return le chemin de l'image PGM à charger
	 */
	private static String choosePGM(){
		
		
		String pwd = System.getProperty("user.dir");
		File doss  = new File(pwd+"/pgm/");
      	File[] fic_doss = doss.listFiles();
		int i=0;
		for(File f:fic_doss){
			if(f.isFile()){ 
          		System.out.println(i+")\t"+ f.getName()); 
        	}
        	i++;
        }

		while(!(0<=i && i<fic_doss.length)){
			System.out.print("Quelle image souhaitez vous compresser ? ");
			i = scan.nextInt();
		}

		return fic_doss[i].getName();
	}
	
	/**
	 * chooseCompressMethod
	 * Permet de choisir la méthode de compression
	 * @return  true pour la compression lambda
	 *			false pour la compression dynamique
	 */
	private static boolean chooseCompressMethod(){
		int rep = 0;
		boolean isRepCorrect = (rep == 1 || rep==2);
		
		while(!isRepCorrect){
			
			System.out.println("Faites un choix de compression : \n 1 : Compression lambda \n 2 : Compression Rho");
			rep = scan.nextInt();
			isRepCorrect = (rep == 1 || rep==2);
			
		}
		return (rep==1);
	}
	
	/**
	 * chooseP
	 * Permet de choisir la valeur de p (taux de compression)
	 * @return le taux de compression choisit
	 */
	private static int choosePercentCompress(){
		int rep = 0;
		boolean isRepCorrect = (0<rep && rep<100);
		
		while(!isRepCorrect){
			System.out.println("Choisissez le taux de compression à réaliser entre 1 et 99 :");
			rep = scan.nextInt();
			isRepCorrect = (0<rep && rep<100);
		}
		return rep;
	}
	
	/**
	 * askStop
	 * Permet de demander à l'utilisateur si il souhaite continuer à compresser des images
	 * @return 	true si il souhaite arreter
	 *			false si il souhaite continuer
	 */
	private static boolean askStop(){
		String answer = "";
		
		while(!(answer.equals("oui") || answer.equals("non"))){
			System.out.println("Voulez vous compresser une autre image (oui/non)?");
			answer = scan.nextLine();
		}
		return !answer.equals("oui");
	}
	
	/**
	 * compressRho
	 * Réalise la compression lambda et créer les fichiers pgm et texte correspondant respectivement à l'image et au quadtree
	 * @param pgm l'image à compresser
	 * @param pathPGM le chemin où stocker l'image compressée
	 * @param pathTXT me chemin ou stocker le texte correspondant au quadtree
	 */
	private static void compressLambda(PGM pgm, String pathPGM,String pathTXT){
			pgm.compressLambda();
			System.out.println("Résultat de la compression Lambda :");
			pgm.printStat();
			pgm.toPGM(pathPGM);
			pgm.quadToTXT(pathTXT);
			
			System.out.println("Le résultats a été stocké dans le dossier pgm_compressed");	
	}
	
	/**
	 * compressRho
	 * Réalise la compression rho et créer les fichiers pgm et texte correspondant respectivement à l'image et au quadtree
	 * @param pgm l'image à compresser
	 * @param p le pourcentage de compression
	 * @param pathPGM le chemin où stocker l'image compressée
	 * @param pathTXT me chemin ou stocker le texte correspondant au quadtree
	 */
	private static void compressRho(PGM pgm,int p, String pathPGM,String pathTXT){
			pgm.compressRho(p);
			System.out.println("Résultat de la compression Rho à "+p+"% :");
			pgm.printStat();
			pgm.toPGM(pathPGM);
			pgm.quadToTXT(pathTXT);

			System.out.println("Le résultats a été stocké dans le dossier pgm_compressed");	
	}
	
	/**
	 * notInteractive
	 * Réalise les compressions lambda ou rho selon le choix de l'utilisateur et sur le fichier qui sera selectionné celui-ci. 
	 */
	private static void interactive(){
		boolean stop = false;
			while(!stop){
				String pwd = System.getProperty("user.dir");
				String  img = choosePGM() ,
						pathInit = pwd+"/pgm/"+img;
				img = img.substring(0,img.length()-5);
				PGM pgm = new PGM(pathInit);
				
				boolean isCompressLambda = chooseCompressMethod();
				
				if(isCompressLambda){
					String  pathPGM = pwd+"/pgm_compressed/"+img+"_compressed_lambda.pgm",
							pathTXT = pwd+"/pgm_compressed/"+img+"_compressed_lambda.txt";
					compressLambda(pgm, pathPGM,pathTXT);
				}else{
					int percent = choosePercentCompress();
					String  pathPGM = pwd+"/pgm_compressed/"+img+"_compressed_rho_"+percent+".txt",
							pathTXT = pwd+"/pgm_compressed/"+img+"_compressed_rho_"+percent+".pgm";
					compressRho(pgm,percent, pathPGM,pathTXT);
				}
				stop = askStop();
			}
	}
	
	/**
	 * notInteractive
	 * Réalise les compressions lambda et rho sur le fichier d'entrée
	 * @param args contenant en args[0] le nom du fichier et en args[1] le pourcentage de compression à réaliser
	 */
	private static void notInteractive(String[] args){
		String pwd = System.getProperty("user.dir");
			String 	pathInit = pwd+"/pgm/"+args[0]+".pgm",
					pathQuadtreeLambdaFinal = pwd+"/pgm_compressed/"+args[0]+"_compressed_lambda.txt",
					pathLambdaFinal = pwd+"/pgm_compressed/"+args[0]+"_compressed_lambda.pgm",
					pathQuadtreeRhoFinal = pwd+"/pgm_compressed/"+args[0]+"_compressed_rho_"+args[1]+".txt",
					pathRhoFinal = pwd+"/pgm_compressed/"+args[0]+"_compressed_rho_"+args[1]+".pgm";
			
			PGM pgm_lambda = new PGM(pathInit);
			PGM pgm_rho = new PGM(pathInit);
			
			compressLambda(pgm_lambda,pathLambdaFinal,pathQuadtreeLambdaFinal);
			compressRho(pgm_rho,Integer.parseInt(args[1]),pathRhoFinal,pathQuadtreeRhoFinal);	
	}
	
	
	/**
	 * Interface utilisateur principal
	 */
	public static void main(String[] args){

		if(args.length == 0){
			interactive();
		}else{
			notInteractive(args);
		}
	}
}
