package src;

/**
 * Projet d'Algorithmique et Structures de Données 3
 * @author HIGNARD Katel et DOEUVRE Guillaume - Groupe 584J
 *
 * AVL.java
 * Représentation d'un AVL
 */
 
public class AVL{
/*
 * Attributs
 */
	private Node node;
	
/*
 * Méthodes
 */
	/** CONSTRUCTEURS *******************************************************/
	public AVL(){
		this.node = null;
	}
	
	/** FONCTIONS GENERALES **************************************************/
	
	/**
	 * add
	 * Ajoute le quadtree à l'avl selon sa valeur d'epsilon
	 * @param
	 */
	public void add(Quadtree n){
		if(this.node==null){
			this.node = new Node(n);
		}else{
			this.node.add(n);
			if(this.node.R()!=null){
				this.node.setR(this.node.R().rebalance());
				this.node.R().updateHeight();
			}
			if(this.node.L()!=null){
				this.node.setL(this.node.L().rebalance());
				this.node.L().updateHeight();
			}
			this.node = this.node.rebalance();
			this.node.updateHeight();
		}
	}

	/**
	 * print
	 * Affiche l'avl
	 */
	public void print(){
		this.node.print();
	}

	/**
	 * isEmpty
	 * @return  true si l'avl est vide
	 *			faux sinon
	 */
	public boolean isEmpty(){
		return this.node == null;
	}

	/**
	 * searchAndDeleteMin
	 * Recherche l'epsilon minimal, supprime de l'avl la valeur si il n'y a pas d'autres quadtree pour ce meme epsilon, et retourne le quadtree liée a cette valeur
	 * @return le noeud d'un quadtree avec l'epsilon minimum a compresser
	 */
	public Quadtree searchAndDeleteMin(){
		if(this.node == null){
			return null;
		}else if(this.node.L() == null){
			Quadtree qtreeMin = this.node.compressibleNode().get(0);
			this.node.compressibleNode().remove(0);

			if(this.node.compressibleNode().isEmpty()){
				this.node = this.node.R();
			}
			return qtreeMin;
		}else{

			Quadtree min = this.node.searchAndDeleteMin();
			if(this.node.L()!=null){
				this.node.setL(this.node.L().rebalance());
				this.node.L().updateHeight();
			}
			this.node = this.node.rebalance();
			this.node.updateHeight();
			return min;
		}
	}
}
