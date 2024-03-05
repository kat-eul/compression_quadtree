# Projet d'Algorithmique et Structure de Données 3 (Licence 3 - Informatique Nantes Université)
## Compilation
```
javac src/*.java
java src/Application
```

Il est possible de lancer le programme avec un fichier présent dans le dossier pgm sans indiquer l'extension avec une valeur comme par exemple ``` java src/Application boat 50```. Cela vous donnera les resultat dans pgm_compressed soient la compression lambda et la compression rho à 50% du fichier passé en entrée.
Sans passer de paramètre d'entrée, l'application vous proposera via le terminal des images à compresser et vous déciderez quelle compression appliquer.

## Présentation du projet

Il nous a été demandé de concevoir une application permettant la compression d'image PGM de format n*n, n étant une puissance de 2.
Nous devions réaliser cette compression via l'implémentation d'un Quadtree.

## Présentation de notre solution

La compression lambda compresse la dernier étage du quadtree, il suffit donc d'accéder aux noeuds de l'avant dernier étage du quadtree et de les compresser.

La compression rho permet de compresser les zones ayant le moins de contraste.

Nous avions en première solution implémenté une liste contenant les noeuds du quadtree n'ayant que des feuilles en tant que fils. Malheureusement, cette solution n'était pas optimisée puisqu'elle devait trouver le minimum parmis la liste. Sur des grandes images cette liste pouvait avoir de très nombreux éléments.

Pour améliorer notre temps d'éxécution, nous avons implémenté un AVL contenant les noeuds du Quadtree et la valeur rho (différence maximale entre la valeur du noeud et la valeur de ses fils). Cette solution permet de savoir en permanence la zone du quadtree la moins contrastée et donc celle qu'il faudra compresser.

## Critiques
Par manque de temps, l'implémentation d'un AVL a été baclée. Nous avons séparé la racine de l'AVL du reste des noeuds ce qui est peu intuitif et pourrais poser des bugs si nous souhaitions modifier le code.

Il serait possible de réaliser des interfaces AVL et Quadtree, afin de pouvoir réutiliser ces notions avec d'autres types de données.