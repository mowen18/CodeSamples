
/*
 * DS 2  Homework Assignment 2   verison 1.0   expand me!
 * 
 * Instructions:
 * 
 * This class is a simple variation on the BinarySearchST class from section 3.1
 * Like the BinarySearchST, this class stores a sybmol table in ordered form in two parallel arrays
 * You can likely leverage some of the code from the BinarySearchST class for this assignment, however
 * be sure that you understand it well enough to be able to recreate it (or simple variations) on
 * an exam
 * 
 * many of the basic API functions are provided for you. For example: contains, get.
 * 
 * put and delete rely on two functions shiftRight, shiftLeft  that you need to complete  . See the
 * Misc. Note below
 * 
 * Your assignment is to complete the ToDo items:some are coding questions, the others require you
 * to construct test cases.  Search for ToDo.  The header comments for each ToDo contain more 
 * specific instructions for that function
 * 
 * As provided, the program will run, but the most of the test cases will fail.
 * One exception is the rank function tests.  These should all pass because a linear-time
 * version of the rank function is supplied. One of the ToDos  is to replace the linear-time rank
 * function with a logarithmic time version (i.e. binary search). 
 * Since the rank function is used by other functions in the class,
 * if your new version of rank is incorrect, other parts of the program may break.
 * 
 * You might consider leaving the linear-time rank function in place while you work on the other ToDos first
 * and then complete the rank ToDo last
 * 
 * 
 * Misc. Note.  put could increase the size of the symbol table.  You will need to add code to handle this  
 * There are two 'reasonable' places to put such code; one of these places would be the put function
 * Likewise, delete will decrease the size of the table and you will need to add code for this as well.
 * One place (of the two places) to do this would be in the  delete  function
 * 
 * Your code may have to resize the underlying arrays 
 * - a resize instance method has been provided for you - look through the file!
 * 
 * You may not modify methods in this file except as indicated. If you are unsure, ask.
 * You may add additional methods for modularity
 * You may not use other Java data structures (e.g. ArrayList, HashSet, etc)
 * 
 */
package homework;  

import stdlib.StdOut;

public class SortedArrayST<Key extends Comparable<Key>, Value> {
	private static final int MIN_SIZE = 2;
	private Key[] keys;      // the keys array
	private Value[] vals;    // the values array
	private int N = 0;       // size of the symbol table, 
	// may be less than the size of the arrays

	/**
	 * Constructor
	 * 
	 * Initializes an empty symbol table.
	 */
	public SortedArrayST() {
		this(MIN_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * Initializes an empty symbol table of given size.
	 */
	@SuppressWarnings("unchecked")   // this tells the compiler that the Generic code below is actually okay
	public SortedArrayST(int size) {
		keys = (Key[])(new Comparable[size]);
		vals = (Value[])(new Object[size]);
	}

	/**
	 * Constructor
	 * 
	 * Initializes a symbol table with given sorted key-value pairs.
	 * If given keys list is not sorted in (strictly) increasing order,
	 * then the input is discarded and an empty symbol table is initialized.
	 */
	public SortedArrayST(Key[] keys, Value[] vals) {
		this(keys.length < MIN_SIZE ? MIN_SIZE : keys.length);
		N = (keys.length == vals.length ? keys.length : 0);
		int i;
		for (i = 1; i < N && keys[i].compareTo(keys[i - 1]) > 0; i++);
		if (i < N) { // input is not sorted
			System.err.println("SortedArrayST(Key[], Value[]) constructor error:");
			System.err.println("Given keys array of size " + N + " was not sorted!");
			System.err.println("Initializing an empty symbol table!");
			N = 0;
		} else {
			for (i = 0; i < N; i++) {
				this.keys[i] = keys[i];
				this.vals[i] = vals[i];
			}
		}
	}

	/**
	 * keysArray
	 * 
	 * Returns the keys array of this symbol table.
	 */
	public Comparable<Key>[] keysArray() {
		return keys;
	}

	/**
	 * valsArray
	 * 
	 * Returns the values array of this symbol table.
	 */
	public Object[] valsArray() {
		return vals;
	}

	/**
	 * size
	 * 
	 * Returns the number of keys in this symbol table.
	 */
	public int size() {
		return N;
	}

	/**
	 * checkFor
	 * 
	 * Returns whether the given key is contained in this symbol table at index r.
	 */
	private boolean checkFor(Key key, int r) {
		return (r >= 0 && r < N && key.equals(keys[r]));
	}

	/**
	 * get
	 * 
	 * Returns the value associated with the given key in this symbol table.
	 */
	public Value get(Key key) {
		int r = rank(key);
		if (checkFor(key, r)) return vals[r];
		else return null;
	}

	/**
	 * put
	 * 
	 * Inserts the specified key-value pair into the symbol table, overwriting the old 
	 * value with the new value if the symbol table already contains the specified key.
	 * Deletes the specified key (and its associated value) from this symbol table
	 * if the specified value is null.
	 */
	public void put(Key key, Value val) {
		int r = rank(key);
		if (!checkFor(key, r)) {
			shiftRight(r);       //  make space for new key/value pair
			keys[r] = key;       //  put the new key in the table

		}
		vals[r] = val;           //  ?
	}

	/**
	 * delete
	 * 
	 * Removes the specified key and its associated value from this symbol table     
	 * (if the key is in this symbol table). 
	 */
	public void delete(Key key) {
		int r = rank(key);
		if (checkFor(key, r)) {
			shiftLeft(r);        // remove the specified key/value 

		}
	}

	/**
	 * contains
	 * 
	 * return true if key is in the table
	 */
	public boolean contains(Key key) {
		return ( this.get(key)!= null);
	}

	/**
	 * resize
	 * 
	 * resize the underlying arrays to the specified size
	 * copy old contents to newly allocated storage
	 */
	@SuppressWarnings("unchecked")  // tell the compiler the following Generic code is okay
	private void resize(int capacity) {
		if (capacity <= N) throw new IllegalArgumentException ();
		Key[] tempk = (Key[]) new Comparable[capacity];
		Value[] tempv = (Value[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			tempk[i] = keys[i];
			tempv[i] = vals[i];
		}
		vals = tempv;
		keys = tempk;
	}

	
	/**
	 * shiftLeft
	 * 
	 * preconditions:
	 *     r >=0 
	 *     N > 0 
	 * postcondition:
	 *     the keys (and values) at indices x > r shifted to the left by one
	 *     in effect, removing the key and value at index r 
	 *     'clear' the original 'last' elements by setting them to null 
	 *  this function does NOT need to decrease the size of the underlying arrays 
	 */
	private void shiftLeft(int r) {
		// ToDo 1.0:  implement this function, see also: ToDo 1.1, 1.2
		if (r>=0 && N > 0) {
			for(int i = r; i < N-1; i++)
			{
			keys[i] = keys[i+1];
			vals[i] = vals[i+1];
			}
			N--;
		}

	}
	
	
	
	
	/**
	 * shiftRight
	 * 
	 * preconditons ?
	 * 
	 * Shifts the keys (and values) at indices r and larger to the right by one
	 * The key and value at position r do not change.
	 * This function must call the resize method (if needed) to  increase the size of the
	 * underlying keys,vals arrays
	 * 
	 */
	private void shiftRight(int r) {
		if(r >= keys.length)
		{
			
			resize(2*keys.length);
			
			keys[N] = keys[r];
			vals[N] = vals[r];
			N++;
			return;
		}
		int i = rank(keys[r]);
		
		resize(2*keys.length);
		for(int j = N; j> i; j--)
		{
			keys[j] = keys[j-1];
			vals[j] = vals[j-1];
			
		}
		N++;
		

		//return; // ToDo 2.0 : implement this function, see also: ToDo 2.1, 2.2

	}

	/**
	 * floor
	 * 
	 * floor returns the largest key in the symbol table that is less than or equal to key.
	 * it returns null if there is no such key.
	 * must be logarithmic time for full credit.   Hint :  rank
	 */
	public Key floor(Key key) {
		int i = rank(key);
		if (i < N && key.compareTo(keys[i]) == 0)
			return keys[i];
		if( i == 0)
			return null;
		else return keys[i-1];
        // ToDo3:  implement this function

	}
	/**
	 * countRange
	 * 
	 * countRange returns the number of keys in the table within the range [key1, key2] (inclusive)
	 * note that keys may not be in order (key1 may be larger than key2): your code should still consider
	 * this a valid range and report the result.
	 * must run in logarithmic time for full credit.  hint: rank
	 */
	public int countRange(Key key1, Key key2) {
		
		int r1 = rank(key1);
		int r2 = rank(key2);
		if (r1 > r2) {
			Key hi = key1;
			Key lo = key2;
			if (contains(hi))
				return rank(hi) - rank(lo) + 1;
			else
				return rank(hi) - rank(lo);
		}
		else if (r1 < r2) {
			Key hi = key2;
			Key lo = key1;
			if (contains(hi))
				return rank(hi) - rank(lo) + 1;
			else
				return rank(hi) - rank(lo);
		}
		return 0;
		 // ToDo4:  implement this function
	}

	/**
	 * rank returns the number of keys in this symbol table that is less than the given key. 
	 */
	public int rank(Key key) {
		return logTimeRank(key);

		// ToDo5 : replace the above call to linearTimeRank with 
		//         a logarithmic time implementation
	}
	
	//log time rank
	private int logTimeRank(Key key) {
		if(key == null)return 0;
		int lo = 0, hi = N -1;
		while(lo <= hi)
		{
			int mid = lo + (hi - lo) / 2;
			int cmp = key.compareTo(keys[mid]);
			if (cmp > 0) lo = mid + 1;
			else if (cmp < 0) hi = mid - 1;
			else return mid;
		}
		return lo;
	}

	/**
	 * Linear time implementation of rank
	 */
	private int linearTimeRank(Key key) {
		int r;
		for (r = 0; r < N && key.compareTo(keys[r]) > 0; r++);
		return r;
	}



	/**
	 * Driver program
	 * 
	 * run all the tests
	 */
	public static void main(String[] args) {

		allShiftLeftTests();
		StdOut.println("Shift-left tests done");
		
		allShiftRightTests();
		StdOut.println("Shift-right tests done");

		allFloorTests();
		StdOut.println("Floor tests done");
		
		allCountRangeTests();
		StdOut.println("CountRange tests done");
		
		allRankTests();
		StdOut.println("Rank tests done");




	}

	public static void allShiftLeftTests() {
		// Testing the delete shiftLeft implementation by calling delete
		// Inputs: String of keys, String of values, key to delete, expected keys & values
		testDelete("ABDFK","12345", "A","BDFK","2345");    // delete first
		testDelete("ABDFK","12345", "B","ADFK","1345");    // delete from 'middle'
		testDelete("ABDFK","12345", "K","ABDF","1234");    // delete last
        testDelete("ABDFK","12345", "W", "ABDFK","12345"); // 1 'delete' non-present key larger than existing largest - no shifts should occur
        testDelete("BDEFK","12345", "A", "BDEFK", "12345");// 2 'delete' non-present key smaller than existing smallest - no shifts should occur
		// done - ToDo 1.1, 1.2  add two  additional test cases for delete
		//    include comments to describe what your case is checking for
		//    try to think of an 'extreme' case

	}
	public static void allShiftRightTests() {

		// Testing the shiftRight implementation  by calling  put
		//  Inputs: String of keys, String of values, key,value to insert, expected keys & values
		testPut("AEIOU","13456", "B","2", "ABEIOU","123456");
		testPut("CEIOU","23456", "B", "1", "BCEIOU","123456");//1 - putting at beginning 
		testPut("CEIOU","23456", "Z", "7", "CEIOUZ","234567");//2 - putting at end


		// ToDo 2.1 , 2.2   add two  additional test cases for put
		//    include comments to describe what your case is checking for
		//    try to think of an 'extreme' case

	}
	public static void allFloorTests() {
		// Testing the floor function  
		//  Inputs:String of keys, String of values, key to test, expected answer
		testFloor("AEIOU","13456", "B","A");  
		testFloor("AEIOU","13456", "E","E");
		testFloor("AEIOU","13456", "Z","U");
		testFloor("VWXYZ","13456", "A",null);
	}

	public static void allCountRangeTests() {
		// Testing the countRange function  
		//  Inputs: String of keys, String of values, range: [key1,key2]  expected answer
		testCountRange("BEIOU","13456", "B","U", 5);   // whole Range
		testCountRange("BEIOU","13456", "U","B", 5);   // whole Range
		testCountRange("BEIOU","13456", "A","Z",5);    // whole Range
		testCountRange("BEIOU","13456", "C","P",3);    // partial Range

	}
	public static void allRankTests() {

		// Testing the rank function
		// Inputs: String of keys, String of values, Key to search on, expected answer
		testRank("BDFK","1234", "A",0);
		testRank("BDFK","1234","B",0);
		testRank("BDFK","1234","C",1);
		testRank("BDFK","1234","D",1);
		testRank("BDFK","1234","K",3);
		testRank("BDFK","1234","Z",4);

	}


	/*  testing suite  
	 * 
	 *   nothing for you to change below
	 * 
	 * */

	/*
	 * 	from
	 * 
	 *    a Utility function used by the testing framework to
	 *    build and return a symbol table from a pair of strings.
	 *    The individual characters of each string are extracted as substrings of length 1
	 *    and then stored in parallel arrays.
	 *    The parallel arrays are used as input to the SortArrayST constructor
	 *    The characters in the keyData need to be in sorted order.
	 *    
	 */
	public static SortedArrayST<String,String> from (String keyData, String valData) {
		int n = keyData.length();
		if ( n != valData.length()) throw new NullPointerException(" from: mismatch sizes");
		String[] keys = new String[n];
		String[] vals = new String[n];
		for (int i=0; i < n; i++ ) {
			keys[i] = keyData.substring(i, i+1);  // ith key is ith char-string of keyData string
			vals[i] = valData.substring(i, i+1);  // ith key is ith char-string of valData string
		}
		return new SortedArrayST(keys,vals);
	}


	/*
	 * testRank
	 * 
	 * Test the rank function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the rank function, compare to the expected answer
	 */
	public static void testRank( String keyData, String valData, String theKey, int expected) {
		SortedArrayST<String, String> x = from(keyData,valData);
		int actual = x.rank(theKey);
		if ( actual == expected)  // test passes
			StdOut.format("rankTest: Correct  Keys: %s, searchKey %s    actual: %d expected: %d\n", keyData, theKey, actual,expected);
		else
			StdOut.format("rankTest: *Error*  Keys: %s, searchKey %s    actual: %d expected: %d\n", keyData, theKey, actual,expected);

	}
	/*
	 * testPut
	 * 
	 * Test the Put function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the put function, compare to the expected answer
	 */
	public static void testPut(String keyInData, String valInData, 
			String putKey, String putVal, 
			String keyOutData, String valOutData) {
		SortedArrayST<String, String> actual = from(keyInData,valInData);
		SortedArrayST<String, String> expected = from(keyOutData, valOutData);
		actual.put(putKey, putVal);
		String actualKeys = actual.keysString();
		String actualVals = actual.valsString();
		String expectedKeys = expected.keysString();
		String expectedVals = expected.valsString();


		if ( actualKeys.equals(expectedKeys) && actualVals.equals(expectedVals) )  // test passes
			StdOut.format("testPut: Correct  actual keys %s  expected keys %s\n", actualKeys,expectedKeys);
		else {
			StdOut.format("testput: *Error*  actual keys %s  expected keys %s\n", actualKeys,expectedKeys);
			StdOut.format("                  actual vals %s  expected keys %s\n", actualVals,expectedVals);
		}

	}
	/*
	 * testDelete
	 * 
	 * Test the delete function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the delete function, compare to the expected answer
	 */
	public static void testDelete(String keyInData, String valInData, String delKey, 
			String keyOutData, String valOutData) {
		SortedArrayST<String, String> actual = from(keyInData,valInData);
		SortedArrayST<String, String> expected = from(keyOutData, valOutData);
		actual.delete(delKey);
		String actualKeys = actual.keysString();
		String actualVals = actual.valsString();
		String expectedKeys = expected.keysString();
		String expectedVals = expected.valsString();


		if ( actualKeys.equals(expectedKeys) && actualVals.equals(expectedVals) )  // test passes
			StdOut.format("testDelete: Correct  actual keys %s  expected keys %s\n", actualKeys,expectedKeys);
		else {
			StdOut.format("testDelete: *Error*  actual keys %s  expected keys %s\n", actualKeys,expectedKeys);
			StdOut.format("                     actual vals %s  expected vals %s\n", actualVals,expectedVals);
		}
	}
	/*
	 * testFloor
	 * 
	 * Test the floor function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the floor function, compare to the expected answer
	 * 
	 */
	public static void testFloor( String keyData, String valData, String theKey, String expected) {
		SortedArrayST<String, String> x = from(keyData,valData);
		String actual = x.floor(theKey);
		//report result
		if ( expected == null) {
			if (actual == null)
				StdOut.format("floorTest: Correct  String %s Answer: null\n", keyData);
			else
				StdOut.format("floorTest: *Error*  String %s Expected: null Actual: %s\n", keyData,actual);
			return;
		}
		if (actual == null && expected != null ) { // error

			StdOut.format("floorTest: *Error*  String %s Expected: %s Actual: null\n", keyData,expected);
			return;
		}

		if ( actual.equals(expected))  // test passes
			StdOut.format("floorTest: Correct  String %s Actual: %s\n", keyData,actual);
		else
			StdOut.format("floorTest: *Error*  String %s Expected %s Actual: %s\n", keyData,expected,actual);
	}
	/*
	 * testCountRange
	 * 
	 * Test the countRange function. 
	 * build a symbol table from the input key,val strings
	 * (keyData characters must be in sorted order)
	 * then call the countRange function, compare to the expected answer
	 * 	testCountRange("BEIOU","13456", "B","U", 5);   // whole Range
	 */
	public static void testCountRange( String keyData, String valData, String key1,String key2, int expected) {
		SortedArrayST<String, String> x = from(keyData,valData);
		int actual = x.countRange(key1,key2);
		if ( actual == expected)  // test passes
			StdOut.format("countRangeTest: Correct  Keys: %s, key1: %s  key2: %s     actual: %d expected: %d\n", keyData, key1,key2, actual,expected);
		else
			StdOut.format("countRangeTest: *Error*  Keys: %s, key1: %s  key2: %s     actual: %d expected: %d\n", keyData, key1,key2, actual,expected);

	}
	/* keysString
	 * 
	 * returns the keys of the table as a single String
	 * used by the testing suite
	 */
	public  String keysString(){
		StringBuilder S = new StringBuilder();
		S.append('[');
		for (int i=0; i < N; i++) 
			S.append(keys[i]);
		S.append(']');
		return S.toString();
	}
	/* valsString
	 * 
	 * returns the values of the table as a single String
	 * used by the testing suite
	 */
	public  String valsString(){
		StringBuilder S = new StringBuilder();
		S.append('[');
		for (int i=0; i < N; i++) 
			S.append(vals[i]);
		S.append(']');
		return S.toString();
	}



}
