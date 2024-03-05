package src;
import java.io.Writer;
import java.io.FileWriter;
import java.lang.Math;

/**
 * Projet d'Algorithmique et StructTRes de Données 3
 * @author HIGNARD Katel et DOEUVRE Guillaume - Groupe 584J
 *
 * Quadtree.java
 * Représentation d'un quadtree permettant la compression d'images en niveau de noir et blanc
 */
 
public class Quadtree{
/*
 * Attributs
 */
 	private int value;
 	/** coordonnées dans l'image (utile pour la recherche d'un pixel)**/
 	private int x;
 	private int y;
 	
 	/** Les fils du noeud sont au nombre de 4 et stockés dans un tableau :
	 *		children[0] : Haut-Gauche
	 *		children[1] : Haut-BRoit
	 *		children[2] : Bas-BRoit
	 *		children[3] : Bas-Gauche
	 */
	private Quadtree children[];
	

/*
 * Méthodes
 */
	
	/** CONSTRUCTEURS **************************************************
	 * @param data le tableau 2D contenant les valeTRs de chaque pixel de l'image
	 * @param size la taille en pixel de l'image (carrée)
	 * @param (i,j) les indices dans le tableau 2D du pixel le plus en haut à gauche
	 */
	public Quadtree(int[][] data, int size, int i, int j){
		/*C'est un pixel*/
		this.children = new Quadtree[4];
		this.x = j;
		this.y = i;
		if(size==1){
			this.value = data[i][j];
			for(int y=0; y<4 ;y++){
				this.children[y] = null;
			}
		/*C'est une zone*/
		}else{
			
			int n_size = size/2;
			this.children[0] = new Quadtree(data,	n_size,	i,			j);
			this.children[1] = new Quadtree(data,	n_size,	i,			j+n_size);
			this.children[2] = new Quadtree(data,	n_size,	i+n_size,	j+n_size);
			this.children[3] = new Quadtree(data,	n_size,	i+n_size,	j);
			
			this.value = this.lambda();
			this.reduct();
		}
	}

	/** GETTER **********************************************************/
	public int getValue(){ return this.value;}
	public Quadtree getTL(){return this.children[0];}
	public Quadtree getTR(){return this.children[1];}
	public Quadtree getBR(){return this.children[2];}
	public Quadtree getBL(){return this.children[3];}
	
	/** METHODES GENERALES **********************************************/
	
	/**
	 * toString
	 * RetoTRne une représentation textuelle du quadtree sous forme parenthésée
	 * @return la chaîne de caractère
	 */
	public String toString(){
		if(this.isLeaf()){
			return String.valueOf(this.value);
		}else{
			String TL = this.children[0].toString();
			String TR = this.children[1].toString();
			String BR = this.children[2].toString();
			String BL = this.children[3].toString();
			
			return "("+TL+" "+TR+" "+BR+" "+BL+")";
		}
	}
	
	/**
	 * print
	 * Affiche dans le terminal la représentation textuelle du quadtree
	 */
	public void print(){
		System.out.println(this.toString());
	}
	
	/**
	 * isLeaf
	 * @return 	true si le noeud est une feuille
	 *			false sinon
	 */
	public boolean isLeaf(){return this.children[0]==null;}
	
	/**
	 * nodeNumber
	 * @return le nombre de feuille présente dans le quadtree
	 */
	public int nodeNumber(){
		if (this.isLeaf()){
			return 1;
		}
		else {
			return 1+this.children[0].nodeNumber()+this.children[1].nodeNumber()
				+this.children[2].nodeNumber()+this.children[3].nodeNumber();
		}
	}

	/**
	 * isChildrenOfSameColor
	 * @return	true si les 4 fils sont de la même coTLeTR
	 *			false sinon
	 */
	private boolean isChildrenOfSameColor(){
		return	this.children[0].value==this.children[1].value
				&& this.children[0].value==this.children[2].value
				&& this.children[0].value==this.children[3].value;
	}
	
	/**
	 * isFatherOfLeaves
	 * @return 	true si le noeud est le père de 4 feuilles
	 *			false sinon
	 */
	private boolean isFatherOfLeaves(){
		return 	this.children[0].isLeaf()
				&& this.children[1].isLeaf()
				&& this.children[2].isLeaf()
				&& this.children[3].isLeaf();
	}
	
	/**
	 * compress
	 * Compression du noeud sans condition 
	 */
	private void compress(){
		for(int i=0; i<4;i++){
			this.children[i] = null;
		}
	}
	
	/**
	 * reduct
	 * Réduit le noeud si il est le père de 4 feuilles et que ces feuilles sont de la même couleur
	 */
	private void reduct(){
		if(this.isFatherOfLeaves() && this.isChildrenOfSameColor()){
			this.compress();
		}
	}
	
	/**
	 * lambda
	 * @return la moyenne des valeurs des 4 fils selon le calcTL de l'énoncé du projet
	 */
	private int lambda(){
		float lambda = 0;
		for(int i=0;i<4;i++){
			lambda += Math.log(0.1+(float)this.children[i].value);
		}
		lambda = lambda/4;
		lambda = (float)Math.exp((double)lambda);
		return Math.round(lambda);
	}
	
	/**
	 * epsilon
	 * @return retourne la valeur d'epsilon maximale parmis les 4 valeurs d'epsilon entre la moyenne des 4 fils et chacun des fils
	 */
	 public int epsilon(){
	 	int epsilon = 0;
	 	for(int i=0; i<4; i++){
	 		int epsilon_i = Math.abs(this.value-this.children[i].value);
	 		if(epsilon_i > epsilon){
	 			epsilon = epsilon_i;
	 		}
	 	}
	 	return epsilon;
	 }
	 
	/** COMPRESSION *********************************************************/
	
	/**
	 * compressLambda
	 * Compresse toutes les brindilles du quadtree 
	 */
	public void compressLambda(){
		if(!this.isLeaf()){
			if(!this.isFatherOfLeaves()){
				this.children[0].compressLambda();
				this.children[1].compressLambda();
				this.children[2].compressLambda();
				this.children[3].compressLambda();
				this.reduct();
			}else{
				this.compress();
			}
		}

	}


	/**
	 * compressRho
	 * Compresse des brindilles jusqu'à avoir un taux de compression inferieur ou egal au pourcentage d'entré
	 * @param p le pourcentage de compression a effectuer
	 */
	public void compressRho(int p){
		int numberOfNodeToDelete = this.nodeNumber()-(this.nodeNumber()*p/100);
		
		AVL compressibleNodes = new AVL();
		searchCompressibleNode(compressibleNodes);
		
		while(numberOfNodeToDelete > 0 && !compressibleNodes.isEmpty()){
			Quadtree nodeToCompress = compressibleNodes.searchAndDeleteMin();
			
			nodeToCompress.compress();
			Quadtree father = this.searchFather(nodeToCompress.x,nodeToCompress.y);
			numberOfNodeToDelete-=4;	
			if((father != null) && (father.isFatherOfLeaves())){
					compressibleNodes.add(father);
			}
		}
		this.reductTwig();
	}
	
	/**
	 * searchCompressibleNode
	 * Modifie l'AVL afin de contenir tout les noeuds actuellement compressibles soit ce sont des brindilles
	 * @param compressibleNode un avl vide
	 */
	private void searchCompressibleNode(AVL compressibleNode){
		if(this!=null && !this.isLeaf()){
			if(this.isFatherOfLeaves()){
				compressibleNode.add(this);
			}else{
				this.children[0].searchCompressibleNode(compressibleNode);
				this.children[1].searchCompressibleNode(compressibleNode);
				this.children[2].searchCompressibleNode(compressibleNode);
				this.children[3].searchCompressibleNode(compressibleNode);
			}
		}
	}
	
	/**
	 * searchFather
	 * @param (x,y) les coordonnées d'une feuille tel que l'on cherche son père
	 * @return le père correspondant aux coordonnées d'entrées
	 * Précision : Cette fonction doit être appelée sur la racine du quadtree
	 */
	public Quadtree searchFather(int x,int y){
		if(this.isLeaf()){
			return null;
		}else{
			int midSize = (this.children[1].x-this.children[0].x),
				size = midSize*2;
			boolean coordInTL = (this.x<=x && x<this.x+midSize)&&(this.y<=y && y<this.y+midSize),
					coordInTR = (this.x+midSize<=x && x<this.x+size)&&(this.y<=y && y<this.y+midSize),
					coordInBR = (this.x+midSize<=x && x<this.x+size)&&(this.y+midSize<=y && y<this.y+size),
					coordInBL = (this.x<=x && x<this.x+midSize)&&(this.y+midSize<=y && y<this.y+size);
					
			if(coordInTL){
				if(this.children[0].isLeaf()){
					return this;
				}else{
					return this.children[0].searchFather(x,y);
				}
			}else if(coordInTR){
				if(this.children[1].isLeaf()){
					return this;
				}else{
					return this.children[1].searchFather(x,y);
				}
			}else if(coordInBR){
				if(this.children[2].isLeaf()){
					return this;
				}else{
					return this.children[2].searchFather(x,y);
				}
			}else if(coordInBL){
				if(this.children[3].isLeaf()){
					return this;
				}else{
					return this.children[3].searchFather(x,y);
				}
			}else{
				return null;
			}
		}	
	}
	
	/**
	 * reductTwig
	 * Réduit une brindille en ayant réduit ses fils avant si besoin
	 */
	private void reductTwig(){
		if(!this.isLeaf()){
			this.children[0].reductTwig();
			this.children[1].reductTwig();
			this.children[2].reductTwig();
			this.children[3].reductTwig();
			this.reduct();
		}
	}
	
	/** CONVERSION EN FICHIER *************************************/
	
	/**
	 * toPGM
	 * Convertit le quadtree en image sous format PGM
	 */
	public void toPGM(int[][] data, int size,int x,int y){
		if(this.isLeaf()){
			for(int i=x;i<x+size;i++){
				for(int j=y;j<y+size;j++){
					data[i][j] = this.value;
				}
			}
		}else{
			this.children[0].toPGM(data,size/2,x,y);
			this.children[1].toPGM(data,size/2,x,y+(size/2));
			this.children[2].toPGM(data,size/2,x+(size/2),y+(size/2));
			this.children[3].toPGM(data,size/2,x+(size/2),y);
		}
	}
	
	/**
	 * toTXT
	 * Créer le fichier TXT correspondant au quadtree
	 */
	public void toTXT(String path){
		try{
			Writer wr = new FileWriter(path);
			wr.write(this.toString());
			wr.flush();
			wr.close();
		}
		catch(Exception e){
			System.out.println("Mauvais chemin indiqué pour TXT");
		}
	}
}
