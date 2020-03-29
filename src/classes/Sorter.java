package classes;

public class Sorter {
	public static void insertion_sort(Symbol[] inputArray, int size){
		  if(size>1) {
			  int index1, index2; 
			  Symbol key;
			    for ( index1 = 1; index1 < size; index1++) 
			    {  
			        key = inputArray[index1];  
			        index2 = index1 - 1;
			        while (index2 >= 0 && inputArray[index2].get_path().length()>key.get_path().length()) 								
			        {  
			            inputArray[index2 + 1] = inputArray[index2];  						//took this from previous git repo
			            index2--;  															//https://github.com/saiforahi/sorting_algorithms/blob/master/sorting_algorithms/InsertionSort.cpp
			        }
			        inputArray[index2 + 1] = key;  
			        
			    }  
		  	}
	  	}
}
