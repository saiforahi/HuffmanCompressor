package classes;

import java.util.Collections;
import java.util.Vector;

public class Sorter {
	public static Vector<Symbol> heapSortInternal(Vector<Symbol> characters){
		  makeheap(characters,characters.size());
		  int size=characters.size();
		  for(;size>1;size--) {
			  Collections.swap(characters,0, size-1);
			  ReheapDown(characters,0,size);
		  }
		  return characters;
	  }
	
	private static void makeheap(Vector<Symbol> characters,int size) {
		  for(int index=(size/2)-1;index>=0;index--) {
			  ReheapDown(characters,index,size);
		  }
		  
	  }
	  
	  private static void ReheapDown(Vector<Symbol> characters,int index,int size) {
		  
		  int parent=index;
		  int leftchild=(2*index)+1;
		  if(leftchild<size) {
			  int rightchild=(2*index)+2;
			  int max=leftchild;
			  if(rightchild<size && characters.get(rightchild).get_path().length()>characters.get(leftchild).get_path().length()) {
				  max=rightchild;
			  }
			  if(characters.get(max).get_path().length()<characters.get(parent).get_path().length()) {
				  Collections.swap(characters,max, parent);
				  ReheapDown(characters,max,size);					//recursive call
			  }
		  }
		  
	  }
}
