package me.ckhd.opengame.api;

import java.util.Arrays;

public class SelectSort {
	/*public static void main(String[] args) {  
		int a[] = {1,2,3,4,4,4,3,1,2,3 };  
		int c[] = new int[5];//c是用来存放每个数字出现次数的数组  
		for (int j = 0; j < a.length; j++) { 
			c[a[j]]++;
		}  
		 
		System.out.println(Arrays.toString(c));  
		//[0, 2, 2, 3, 3] 原来写错了，【0,2,2,3,2】 4有3次  
		for (int i = 1; i < c.length; i++) {  
		    c[i] = c[i] +c[i-1];  
		}  
		System.out.println(Arrays.toString(c));  
		//[0, 2, 4, 7, 10]  ，这里相对的也写错了。  
	    int[] b = new int[a.length];  
	    for (int i = 0; i < a.length; i++) {  
	        b[c[a[i]]-1] = a[i];  
	        c[a[i]]--;  
	    }  
		System.out.println(Arrays.toString(b));  
	}*/  
	
	public static void main(String[] args) {  
		int a[] = {1,2,3,4,4,4,3,1,2,3 };   
		int c[] = new int[5];  
		for(int i=0;i<c.length;i++){  
			for(int j=0;j<a.length;j++){  
				if(a[j] == i){  
					c[i]++;  
				}  
			}  
		}  
		int index = 0;  
		for(int j=0;j<c.length;j++){  
			for(int i=index;i<index + c[j];i++){  
				a[i] = j;  
			}  
			index +=c[j];  
		}  
		System.out.println(Arrays.toString(a));  
	}
}
