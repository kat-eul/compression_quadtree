package src;
import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.util.Scanner;
/**
 * Projet d'Algorithmique et Structures de Données 3
 * @author HIGNARD Katel et DOEUVRE Guillaume - Groupe 584J
 *
 * PGM.java
 * Représentation d'une image PGM sous forme de quadtree, stockant également les informations générales de l'images (commentaire,...)
 */
 
public class PGM{
/*
 * Attributs
 */
	private Quadtree qtree;
	
	private String magicNumber;
	private String comment;
	private int size;
	private int maxValue;
	
	private int nbNodeInit;
	
/*
 * Méthodes
 */
	/** CONSTRUCTEURS ***********************************************************
	 * @param pathPGM le chemin de l'image à traiter
	 */
	public PGM(String pathPGM){
		
		File pgm = new File(pathPGM);
		try{
		Scanner scan = new Scanner(pgm);
		
		// Les 4 premières lignes du fichiers sont les informations générales
		this.magicNumber = scan.nextLine();
		this.comment = scan.nextLine();
		this.size = scan.nextInt();
		scan.nextInt();
		this.maxValue = scan.nextInt();
		
		int pixel[][] = extractPixel(scan);

		scan.close();
		
		this.qtree = new Quadtree(pixel,this.size,0,0);
		}
		catch(Exception e){
			System.out.println("Fichier non existant");
		}
		
		this.nbNodeInit = this.qtree.nodeNumber();	
	}

	/** GETTER ***********************************************************/
	public String getMagicNumber(){return this.magicNumber;}
	public String getComment(){return this.comment;}
	public int getSize(){return this.size;}
	public int getMaxValue(){return this.maxValue;}
	public int getNbNodeInit(){return this.nbNodeInit;}	
	
	
	/** METHODES GENERALES **********************************************/
	
	/**
	 * toString
	 * Retourne une représentation textuelle de l'image PGM
	 * @return la chaîne de caractère
	 */
	public String toString(){
		String infos;
		infos = "Magic Number : " + this.magicNumber +"\n";
		infos += "Commentaire : " + this.comment + "\n";
		infos += "Taille : " + this.size + "x" + this.size + "\n";
		infos += "Valeur maximale : " + this.maxValue +"\n";
		return infos + "\n" + this.qtree.toString();
	}
	
	/**
	 * print
	 * Affiche dans le terminal la représentation textuelle du l'image PGM
	 */
	public void print(){
		System.out.println(this.toString());
	}
	
	/**
	 * printStat
	 * Affiche les statistiques liées à la compression
	 */
	public void printStat(){
		System.out.println("Nombre de noeuds initial : " + this.nbNodeInit);
		System.out.println("Nombre de noeuds final : " + this.nodeNumber());
		System.out.println("Taux de compression réalisé : " + (float)this.nodeNumber()/(float)this.nbNodeInit);
	}
	
	/**
	 * nodeNumber
	 * @return le nombre de noeuds dans le quadtree
	 */
	public int nodeNumber(){
		return this.qtree.nodeNumber();
	}
	
	/**
	 * Extraction des pixels d'une image PGM
	 * @param pathPGM le chemin de l'image
	 * @return un tableau 2D contenant les données des pixels
	 */
	private int[][] extractPixel(Scanner scan){

		int pixel[][] = new int[this.size][this.size];
		int i=0,j=0;
		
		//Lecture des données sur les pixels
		while(scan.hasNextInt() && i<this.size){
			pixel[i][j] = scan.nextInt();
			
			j++;
			if(j>=this.size){
				i++;
				j=0;
			}
		}
		return pixel;
	}
	
	/**
	 * compressLambda
	 * Réalise la compression lambda sur le quadtree
	 */
	public void compressLambda(){
		this.qtree.compressLambda();
	}
	
	/**
	 * compressRho
	 * Compresse le quadtree jusqu'à obtenir un nombre de noeuds inférieur ou égal au taux de compression
	 * @param percent le taux de compression souhaité
	 */
	public void compressRho(int percent){
		this.qtree.compressRho(percent);
	}
	
	/**
	 * toPGM
	 * Création du fichier PGM à partir des données de l'objet
	 * @param path le chemin où stocker la nouvelle image
	 */
	public void toPGM(String path){
		try{
			Writer wr = new FileWriter(path);
			wr.write(this.magicNumber+"\n");
			wr.write(this.comment+"\n");
			wr.write(this.size+" "+this.size+"\n");
			wr.write(this.maxValue+"\n");
			
			
			int pixels[][] = new int[this.size][this.size];
			this.qtree.toPGM(pixels, this.size, 0, 0);
			
			for(int i=0;i<size;i++){
				for(int j=0;j<size;j++){
					wr.write(pixels[i][j]+" ");
				}
			}
			
			
			wr.flush();
			wr.close();
		}
		catch(Exception e){
			System.out.println("Mauvais chemin indiqué pour PGM");
		}
	}
	
	/**
	 * quadToTxt
	 * Création du fichier TXT à partir du qaudtree
	 * @param path le chemin où stocker la nouvelle image
	 */
	public void quadToTXT(String path){
		this.qtree.toTXT(path);
	}
}
