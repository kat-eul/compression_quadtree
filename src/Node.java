package src;
import java.util.ArrayList;
import java.lang.Math;
/**
 * Projet d'Algorithmique et Structures de Données 3
 * @author HIGNARD Katel et DOEUVRE Guillaume - Groupe 584J
 *
 * Node.java
 * Répsentation d'un noeud d'un AVL
 */

public class Node{
/*
 * Attributs
 */
	private int epsilon;
	private int height;
	private ArrayList<Quadtree> compressibleNode;
	
	private Node l;
	private Node r;
	
/*
 * Méthodes
 */
	
	/** CONSTRUCTEURS ***********************************************************
	 * @param n le noeud d'un quadtree
	 */
	public Node(Quadtree n){
		this.epsilon = n.epsilon();
		this.height = 1;
		this.compressibleNode = new ArrayList<Quadtree>();
		this.compressibleNode.add(n);
		
		this.l = null;
		this.r = null;
	}
	
	/** GETTERS ***********************************************************/
	public int eps(){return this.epsilon;}
	public int height(){return this.height;}
	public ArrayList<Quadtree> compressibleNode(){return this.compressibleNode;}
	public Node L(){return this.l;}
	public Node R(){return this.r;}
	
	/** SETTERS ***********************************************************/
	public void setEps(int eps){this.epsilon=eps;}
	public void setHeight(int h){this.height=h;}
	public void setL(Node node){this.l=node;}
	public void setR(Node node){this.r=node;}
	
	/** FONCTIONS GENERALES ***********************************************/
	/**
	 * toString
	 * @return le format texte du noeud
	 */
	public String toString(){
		if(this.l==null && this.r== null){
			return "["+String.valueOf(this.epsilon)+"]";
		}else if(this.l==null){
			return "(x "+String.valueOf(this.epsilon)+" "+this.r.toString()+")";
		}else if(this.r==null){
			return "("+this.l.toString()+" "+String.valueOf(this.epsilon)+" x)";
		}else{
			return "("+this.l.toString()+" "+String.valueOf(this.epsilon)+" "+this.r.toString()+")";
		}
	}

	/**
	 * print
	 * Affiche le format texte du noeud
	 */
	public void print(){
		System.out.println(this.toString());
	}

	/**
	 * bal
	 * @return la valeur de balance du noeud selon la hauteur de ses fils
	 */
	public int bal(){
		if(this.l==null && this.r==null){
			return 0;
		}else if(this.l==null){
			return this.r.height();
		}else if(this.r==null){
			return 0-this.l.height();
		}else{
			return this.r.height()-this.l.height();
		}
	}
	
	/**
	 * add
	 * Ajout le noeud d'entrée dans l'avl selon sa valeur d'espilon
	 * @param n un noeud d'un quadtree
	 */
	public void add(Quadtree n){
		if(this.epsilon == n.epsilon()){
			this.compressibleNode.add(n);
		}else if(this.epsilon < n.epsilon()){
			if(this.r == null){
				this.r = new Node(n);
				this.updateHeight();
			}else{
				this.r.add(n);
				this.r = this.r.rebalance();
				this.r.updateHeight();
				this.updateHeight();
			}
		}else{
			if(this.l == null){
				this.l = new Node(n);
				this.updateHeight();
			}else{
				this.l.add(n);
				this.l = this.l.rebalance();
				this.l.updateHeight();
				this.updateHeight();
			}
		}
	}
	
	/**
	 * updateHeight
	 * Réévalue la hauteur du noeud
	 */
	public void updateHeight(){
		if(this.l==null && this.r==null){
			this.height = 1;
		}else if(this.l==null){
			this.height = this.r.height()+1;
		}else if(this.r==null){
			this.height = this.l.height()+1;
		}else{
			this.height = Math.max(this.l.height(),this.r.height())+1;
		}
	}
	
	/**
	 * rebalance
	 * Rééquilibre le noeud 
	 * @return la nouvelle racine
	 */
	public Node rebalance(){
		if(this.bal()==-2){
			if(this.l.R() == null || (this.l.L()!=null && this.l.R().height() <= this.l.R().height())){
				return this.rotd();
			}else{
				return this.drotd();
			}
		}else if(this.bal()==2){
			if(this.r.L() == null || (this.r.R()!= null && this.r.L().height() <= this.r.R().height())){
				return this.rotg();
			}else{
				return this.drotg();
			}
		}else{
			return this;
		}
	}
	
	/**
	 * rotd
	 * Réalise une rotation vers la droite
	 */
	public Node rotd(){
		Node tmp = this.l;
		this.l = tmp.R();
		tmp.setR(this);
		tmp.R().updateHeight();
		return tmp;
	}
	
	/**
	 * rotg
	 * Réalise une rotation vers la gauche
	 */
	public Node rotg(){
		Node tmp = this.r;
		this.r = tmp.L();
		tmp.setL(this);
		tmp.L().updateHeight();
		return tmp;
	}
	
	/**
	 * drotd
	 * Réalise une double rotation vers la droite
	 */
	public Node drotd(){
		Node tmp = this.l.R();
		this.l.setR(tmp.L());
		tmp.setL(this.l);
		this.l = tmp.R();
		tmp.setR(this);
		tmp.R().updateHeight();
		tmp.L().updateHeight();
		return tmp;
	}
	
	/**
	 * drotg
	 * Réalise une double rotation vers la gauche
	 */
	public Node drotg(){
		Node tmp = this.r.L();
		this.r.setL(tmp.R());
		tmp.setR(this.r);
		this.r = tmp.L();
		tmp.setL(this);
		tmp.R().updateHeight();
		tmp.L().updateHeight();
		return tmp;
	}
	
	/**
	 * searchAndDeleteMin
	 * Recherche l'epsilon minimal, supprime de l'avl la valeur si il n'y a pas d'autres quadtree pour ce meme epsilon, et retourne le quadtree liée a cette valeur
	 * @return le noeud d'un quadtree avec l'epsilon minimum a compresser
	 */
	public Quadtree searchAndDeleteMin(){
		if(this.l.L() == null){
			Quadtree min = this.l.compressibleNode.get(0);
			this.l.compressibleNode.remove(0);
			if(this.l.compressibleNode.isEmpty()){
				this.l = this.l.R();
			}
			return min;
		}else{
			Quadtree min = this.l.searchAndDeleteMin();
			this.l = this.l.rebalance();
			this.updateHeight();
			return min;
		}
	}
}
